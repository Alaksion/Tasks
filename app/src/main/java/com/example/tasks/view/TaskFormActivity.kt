package com.example.tasks.view

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.models.PriorityModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private var mTaskId = 0
    private lateinit var mViewModel: TaskFormViewModel
    private val mDateFormate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        observe()
        mViewModel.loadPriorities()
        listeners()
        loadBundle()
    }

    private fun observe() {
        mViewModel.priorities.observe(this, androidx.lifecycle.Observer {
            val list = it.map(fun(i: PriorityModel): String {
                mViewModel.mPrioritiesIdList.add(i.id)
                return i.description
            })

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, list
            )
            spinner_priority.adapter = adapter
        })

        mViewModel.saveSuccess.observe(this, androidx.lifecycle.Observer {
            if (!it.getStatus()) {
                Toast.makeText(this, it.getMsg(), Toast.LENGTH_LONG).show()
            } else {

                val message = if (mTaskId == 0) {
                    getString(R.string.task_created)
                } else {
                    getString(R.string.task_updated)
                }

                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                finish()
            }
        })

        mViewModel.task.observe(this, androidx.lifecycle.Observer {
            val dateFormater = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(it.dueDate)

            edit_description.setText(it.description)
            check_complete.isChecked = it.complete
            button_date.text = dateFormater.format(date)
            val priorityIndex = getPriorityIndex(it.priorityId)
            spinner_priority.setSelection(priorityIndex)
        })
    }

    private fun listeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val datePicker = DatePickerDialog(this, this, year, month, day)
        // impedindo que o usuário escolha uma data de vencimento que já passou
        datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePicker.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateButtonText = mDateFormate.format(calendar.time)

        button_date.text = dateButtonText
    }

    override fun onClick(v: View) {
        when (v.id) {
            button_save.id -> handleSave()
            button_date.id -> showDatePicker()
        }
    }

    private fun handleSave() {
        val description = edit_description.text.toString()
        val completed = check_complete.isChecked
        val dueDate = button_date.text.toString()
        val priority = mViewModel.mPrioritiesIdList[spinner_priority.selectedItemPosition]

        mViewModel.save(description, priority, completed, dueDate, mTaskId)
    }

    private fun loadBundle() {
        val bundle = intent.extras

        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            mViewModel.load(mTaskId)
        }
    }

    private fun getPriorityIndex(priorityId: Int): Int {
        return mViewModel.mPrioritiesIdList.indexOf(priorityId)
    }

}
