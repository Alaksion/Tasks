package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.models.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = TaskRepository(application)

    private val mTasks = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mTasks

    private val mDeleteSuccess = MutableLiveData<ValidationListener>()
    val deleteSuccess: LiveData<ValidationListener> = mDeleteSuccess

    private val mListSuccess = MutableLiveData<ValidationListener>()
    val listSuccess: LiveData<ValidationListener> = mListSuccess

    private val mTaskStatusSuccess = MutableLiveData<ValidationListener>()
    var taskStatusSuccess: LiveData<ValidationListener> = mTaskStatusSuccess

    private var mTaskFilter = 0

    fun load(taskFilter: Int) {
        mTaskFilter = taskFilter

        val listener = object : ApiListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mTasks.value = model
                mListSuccess.value = ValidationListener()
            }

            override fun onError(msg: String) {
                mTasks.value = arrayListOf()
                mListSuccess.value = ValidationListener(msg)
            }
        }

        when (mTaskFilter) {
            TaskConstants.FILTER.ALL ->
                mRepository.all(listener)

            TaskConstants.FILTER.EXPIRED ->
                mRepository.overdue(listener)

            TaskConstants.FILTER.NEXT -> mRepository.next7Days(listener)
        }
    }

    fun delete(taskId: Int) {
        mRepository.delete(taskId, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                load(mTaskFilter)
            }

            override fun onError(msg: String) {
                mTaskStatusSuccess.value = ValidationListener(msg)
            }
        })
    }

    fun complete(taskId: Int) {
        mRepository.completeTask(taskId, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                load(mTaskFilter)
            }

            override fun onError(msg: String) {
                mTaskStatusSuccess.value = ValidationListener(msg)
            }
        })
    }

    fun undo(taskId: Int) {
        mRepository.undoTask(taskId, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                load(mTaskFilter)
            }

            override fun onError(msg: String) {
                mTaskStatusSuccess.value = ValidationListener(msg)
            }
        })
    }
}