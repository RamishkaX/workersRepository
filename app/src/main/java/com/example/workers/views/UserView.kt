package com.example.workers.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.workers.models.UserModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface UserView : MvpView {
    fun startLoading()
    fun endLoading()
    fun setupUser(user: UserModel)
    fun showError(textResource: Int)
    fun showError(text: String)
}