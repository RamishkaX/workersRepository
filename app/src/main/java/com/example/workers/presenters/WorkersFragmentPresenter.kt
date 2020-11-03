package com.example.workers.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.workers.R
import com.example.workers.handlers.DBHandler
import com.example.workers.views.WorkersFragmentView

@InjectViewState
class WorkersFragmentPresenter : MvpPresenter<WorkersFragmentView>() {
    fun loadWorkers(specialityId: Int, dbHandler: DBHandler) {
        viewState.startLoading()
        val userModelList = dbHandler.getWorkersFilterBySpeciality(specialityId = specialityId, db = dbHandler.readableDatabase)

        if (userModelList.isNullOrEmpty()) {
            viewState.endLoading()
            viewState.showError(R.string.DB_error)
            return
        }

        viewState.endLoading()
        viewState.setupWorkersList(workersList = userModelList)
    }
}