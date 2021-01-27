package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.models.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = TaskRepository(application)

    private val mTasks = MutableLiveData<List<TaskModel>>()
    var tasks : LiveData<List<TaskModel>> = mTasks

    fun load(){
        mRepository.all(object : ApiListener<List<TaskModel>>{
            override fun onSuccess(model: List<TaskModel>) {
                mTasks.value = model
            }

            override fun onError(msg: String) {
                mTasks.value = arrayListOf()
            }
        })
    }
}