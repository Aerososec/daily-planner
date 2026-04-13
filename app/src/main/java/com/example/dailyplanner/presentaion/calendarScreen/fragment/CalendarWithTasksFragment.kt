package com.example.dailyplanner.presentaion.calendarScreen.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyplanner.DailyPlannerApp
import com.example.dailyplanner.R
import com.example.dailyplanner.databinding.FragmentCalendarWithTasksBinding
import com.example.dailyplanner.di.factory.ViewModelFactory
import com.example.dailyplanner.presentaion.calendarScreen.GetTasksViewModel
import com.example.dailyplanner.presentaion.calendarScreen.recyclerView.TasksForHourAdapter
import com.example.dailyplanner.presentaion.taskScreen.fragment.CreateTaskFragment
import com.example.dailyplanner.presentaion.utils.formatStringDate
import com.example.dailyplanner.presentaion.utils.toStartOfDay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class CalendarWithTasksFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentCalendarWithTasksBinding
    private lateinit var viewModel: GetTasksViewModel
    private lateinit var taskAdapter: TasksForHourAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DailyPlannerApp).mainComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[GetTasksViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarWithTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setCurrentDate()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.schedule.collectLatest { list ->
                    taskAdapter.submitList(list)
                }
            }
        }

        chooseDay()

        binding.fabAddTask.setOnClickListener {
            openCreateTaskFragmentForCreate()
        }

        timePeriodClick()
    }

    private fun openCreateTaskFragment(fragment: CreateTaskFragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openCreateTaskFragmentForPeriod(startPeriod: Long, endPeriod: Long){
        val fragment = CreateTaskFragment.newInstance(startPeriod, endPeriod)
        openCreateTaskFragment(fragment)
    }

    private fun openCreateTaskFragmentForCreate(){
        val fragment = CreateTaskFragment.newInstance()
        openCreateTaskFragment(fragment)
    }


    private fun chooseDay() {
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            val selectedDay = LocalDate.of(year, month + 1, day)
            loadTasksForDay(selectedDay)
        }
    }

    private fun loadTasksForDay(selectedDay: LocalDate) {
        viewModel.getTaskForDay(selectedDay)
    }

    private fun setUpRecyclerView() {
        taskAdapter = TasksForHourAdapter()
        binding.rvHourlyTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun timePeriodClick(){
        taskAdapter.timePeriodClick = { taskInHour ->
            if (taskInHour.tasks.isNotEmpty()){
                if(taskInHour.startPeriod != null && taskInHour.endPeriod != null)
                    openCreateTaskFragmentForPeriod(taskInHour.startPeriod, taskInHour.endPeriod)
            }
        }
    }

    private fun setCurrentDate() {
        val today = LocalDate.now()
        loadTasksForDay(today)
        binding.calendarView.date = today.toStartOfDay()
        binding.tvCurrentDate.text = "Сегодня, " + today.formatStringDate()
    }
}