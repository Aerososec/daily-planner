package com.example.dailyplanner.presentaion.taskScreen.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dailyplanner.DailyPlannerApp
import com.example.dailyplanner.R
import com.example.dailyplanner.databinding.FragmentCreateTaskBinding
import com.example.dailyplanner.di.factory.ViewModelFactory
import com.example.dailyplanner.domain.model.Task
import com.example.dailyplanner.presentaion.taskScreen.viewModel.CreateTaskViewModel
import com.example.dailyplanner.presentaion.utils.formatStringDate
import com.example.dailyplanner.presentaion.utils.toLocalDateTime
import com.example.dailyplanner.presentaion.utils.toMillis
import com.example.dailyplanner.presentaion.utils.toStartOfDay
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class CreateTaskFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var binding: FragmentCreateTaskBinding

    private var selectedDate: LocalDate? = null
    private var selectedStartTime: LocalTime? = null
    private var selectedEndTime: LocalTime? = null
    private var tasks = listOf<Task>()
    private var currentIndex = 0
    private var taskId : Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DailyPlannerApp).mainComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[CreateTaskViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTaskBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            parseArgs()
        }

        binding.etTaskDate.setOnClickListener {
            showDatePicker()
        }
        binding.etStartTime.setOnClickListener {
            showTimePicker(binding.etStartTime)
        }
        binding.etEndTime.setOnClickListener {
            showTimePicker(binding.etEndTime)
        }

        binding.btnCreateTask.setOnClickListener {
            val name = binding.etTaskName.text.toString()
            val description = binding.etTaskDescription.text.toString()
            if (selectedDate != null && selectedEndTime != null && selectedStartTime != null){
                val startDayMillis = selectedDate?.toStartOfDay()  ?: 0
                val startMillis = LocalDateTime.of(selectedDate, selectedStartTime).toMillis()
                val endMillis = LocalDateTime.of(selectedDate, selectedEndTime).toMillis()
                if (validateTask(startDayMillis, startMillis, endMillis)){
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.insertTask(startMillis, endMillis, name, description, taskId)
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasks.collect { list ->
                    tasks = list
                    currentIndex = 0

                    if (tasks.isNotEmpty()) {
                        setUpTaskInfo(tasks[currentIndex])
                        taskId = tasks[currentIndex].id
                    }

                    updateNavigation()
                }
            }
        }

        binding.btnNextTask.setOnClickListener {
            if (currentIndex < tasks.lastIndex) {
                currentIndex++
                setUpTaskInfo(tasks[currentIndex])
                taskId = tasks[currentIndex].id
                updateNavigation()
            }
        }

        binding.btnPrevTask.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                setUpTaskInfo(tasks[currentIndex])
                taskId = tasks[currentIndex].id
                updateNavigation()
            }
        }

        binding.btnDeleteTask.setOnClickListener {
            val task = tasks[currentIndex]
            viewLifecycleOwner.lifecycleScope.launch{
                viewModel.deleteTask(task)
            }

            if (tasks.size == 1) parentFragmentManager.popBackStack()

        }
    }

    private fun validateTask(
        startDay : Long,
        startMillis: Long,
        endMillis: Long
    ): Boolean {
        val startOfToday: Long = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        if (startDay < startOfToday) {
            binding.etTaskDate.error = "Введите корректную дату"
            return false
        }
        binding.etTaskDate.error = null

        val now = System.currentTimeMillis()

        if (startMillis < now) {
            binding.etStartTime.error = "Задачу нельзя начать в прошлом"
            return false
        }
        binding.etStartTime.error = null

        if (endMillis <= startMillis) {
            binding.etEndTime.error = "Задача не может закончиться раньше, чем началась"
            return false
        }
        binding.etEndTime.error = null

        return true
    }

    private fun updateNavigation() {
        binding.tvTaskPosition.text =
            if (tasks.isEmpty()) "0 / 0"
            else "${currentIndex + 1} / ${tasks.size}"

        binding.btnPrevTask.isEnabled = currentIndex > 0
        binding.btnNextTask.isEnabled = currentIndex < tasks.lastIndex

        binding.layoutTaskNavigation.isVisible = tasks.isNotEmpty()
    }

    private fun setUpTaskInfo(task: Task) {
        binding.apply {
            etTaskName.setText(task.name)
            etTaskDescription.setText(task.description)

            val startDateTime = task.dateStart.toLocalDateTime()
            val endDateTime = task.dateFinish.toLocalDateTime()

            selectedDate = startDateTime.toLocalDate()
            selectedStartTime = startDateTime.toLocalTime()
            selectedEndTime = endDateTime.toLocalTime()

            etTaskDate.setText(selectedDate?.formatStringDate())

            etStartTime.setText(
                selectedStartTime?.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            )

            etEndTime.setText(
                selectedEndTime?.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            )
        }
    }

    private fun showDatePicker(){
        val today = LocalDate.now()

        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                binding.etTaskDate.setText(selectedDate?.formatStringDate())
            },
            today.year,
            today.monthValue - 1,
            today.dayOfMonth
        )

        dialog.show()
    }

    private fun showTimePicker(time : TextInputEditText){
        val now = LocalTime.now()

        val dialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = LocalTime.of(hourOfDay, minute)
                setSelectedTime(time, selectedTime)
                time.setText(
                    selectedTime.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
                )

            },
            now.hour,
            now.minute,
            true
        )

        dialog.show()
    }

    private fun setSelectedTime(time : TextInputEditText, selectedTime : LocalTime){
        when(time){
            binding.etStartTime -> {
                selectedStartTime = selectedTime
            }
            binding.etEndTime -> {
                selectedEndTime = selectedTime
            }
        }
    }

    private suspend fun parseArgs(){
        taskId = 0
        val args = arguments ?: return

        if (!args.containsKey(START_PERIOD_KEY) || !args.containsKey(END_PERIOD_KEY)) return

        val startPeriod = args.getLong(START_PERIOD_KEY)
        val endPeriod = args.getLong(END_PERIOD_KEY)

        viewModel.loadTasksForPeriod(startPeriod, endPeriod)
    }



    companion object{
        const val START_PERIOD_KEY = "START_PERIOD_KEY"
        const val END_PERIOD_KEY = "END_PERIOD_KEY"

        fun newInstance(startPeriod : Long? = null, endPeriod : Long? = null) : CreateTaskFragment {
            return CreateTaskFragment().apply {
                arguments = Bundle().apply {
                    startPeriod?.let {
                        putLong(START_PERIOD_KEY, it)
                    }
                    endPeriod?.let {
                        putLong(END_PERIOD_KEY, it)
                    }
                }
            }
        }
    }
}