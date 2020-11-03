package com.example.workers.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.workers.models.UserModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface WorkersFragmentView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupWorkersList(workersList: ArrayList<UserModel>)
    fun openUserFragment(userId: Int)
    fun showError(textResource: Int)
    fun showError(text: String)
}