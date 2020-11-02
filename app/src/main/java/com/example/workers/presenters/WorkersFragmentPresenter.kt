package com.example.workers.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.workers.views.WorkersFragmentView

@InjectViewState
class WorkersFragmentPresenter : MvpPresenter<WorkersFragmentView>() {
    fun test() {
        viewState.showError("test")
    }
}