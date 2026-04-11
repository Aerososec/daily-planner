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
import androidx.lifecycle.ViewModelProvider
import com.example.dailyplanner.DailyPlannerApp
import com.example.dailyplanner.R
import com.example.dailyplanner.databinding.FragmentCreateTaskBinding
import com.example.dailyplanner.di.factory.ViewModelFactory
import com.example.dailyplanner.presentaion.taskScreen.viewModel.CreateTaskViewModel
import com.example.dailyplanner.presentaion.utils.formatStringDate
import com.example.dailyplanner.presentaion.utils.toMillis
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
                val startMillis = LocalDateTime.of(selectedDate, selectedStartTime).toMillis()
                val endMillis = LocalDateTime.of(selectedDate, selectedEndTime).toMillis()
                viewModel.insertTask(startMillis, endMillis, name, description)
            }
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
}