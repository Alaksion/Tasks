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

    val mPrioritiesIdList : MutableList<Int> = arrayListOf()

    private val mCreateSuccess = MutableLiveData<ValidationListener>()
    val createSuccess : LiveData<ValidationListener> = mCreateSuccess

    fun create(description: String, priority: Int, completed: Boolean, dueDate: String){
        val task = TaskModel().apply {
            this.complete = completed
            this.description = description
            this.dueDate = dueDate
            this.priorityId = priority
        }
        mTaskRepository.create(task, object : ApiListener<Boolean>{
            override fun onSuccess(model: Boolean) {
                mCreateSuccess.value = ValidationListener()
            }

            override fun onError(msg: String) {
                mCreateSuccess.value = ValidationListener(msg)
            }
        })
    }

    fun loadPriorities(){
        mPriorities.value = mPriorityRepository.list()
    }
}