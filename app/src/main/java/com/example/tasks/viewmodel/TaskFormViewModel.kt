package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.models.PriorityModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.models.TaskModel
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorities = MutableLiveData<List<PriorityModel>>()
    var priorities = mPriorities

    val mPrioritiesIdList: MutableList<Int> = arrayListOf()

    private val mSaveSuccess = MutableLiveData<ValidationListener>()
    val saveSuccess: LiveData<ValidationListener> = mSaveSuccess

    private val mTask = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = mTask

    fun save(description: String, priority: Int, completed: Boolean, dueDate: String, taskId: Int) {
        val task = TaskModel().apply {
            this.complete = completed
            this.description = description
            this.dueDate = dueDate
            this.priorityId = priority
            this.id = taskId
        }

        if (task.id != 0) {
            mTaskRepository.update(task, object : ApiListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mSaveSuccess.value = ValidationListener()
                }

                override fun onError(msg: String) {
                    mSaveSuccess.value = ValidationListener(msg)
                }
            })
        } else {
            mTaskRepository.create(task, object : ApiListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mSaveSuccess.value = ValidationListener()
                }

                override fun onError(msg: String) {
                    mSaveSuccess.value = ValidationListener(msg)
                }
            })
        }
    }

    fun loadPriorities() {
        mPriorities.value = mPriorityRepository.list()
    }

    fun load(taskId: Int) {
        mTaskRepository.getSingle(taskId, object : ApiListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                mTask.value = model
            }

            override fun onError(msg: String) {
                TODO("Not yet implemented")
            }

        })
    }
}