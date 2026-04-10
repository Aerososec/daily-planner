package com.example.dailyplanner.presentaion.calendarScreen.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyplanner.DailyPlannerApp
import com.example.dailyplanner.R
import com.example.dailyplanner.databinding.FragmentCalendarWithTasksBinding
import com.example.dailyplanner.di.factory.ViewModelFactory
import com.example.dailyplanner.presentaion.calendarScreen.GetTasksViewModel
import com.example.dailyplanner.presentaion.calendarScreen.recyclerView.TasksForHourAdapter
import com.example.dailyplanner.presentaion.utils.formatStringDate
import com.example.dailyplanner.presentaion.utils.toStartOfDay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class CalendarWithTasksFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentCalendarWithTasksBinding
    private lateinit var viewModel: GetTasksViewModel
    private lateinit var adapter: TasksForHourAdapter

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
        lifecycleScope.launch {
            viewModel.schedule.collect {
                adapter.submitList(it)
            }
        }

        chooseDay()
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
        adapter = TasksForHourAdapter()
        binding.rvHourlyTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }
    }

    private fun setCurrentDate() {
        val today = LocalDate.now()
        loadTasksForDay(today)
        binding.calendarView.date = today.toStartOfDay()
        binding.tvCurrentDate.text = "Сегодня, " + today.formatStringDate()
    }
}