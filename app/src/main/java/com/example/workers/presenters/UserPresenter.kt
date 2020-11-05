package com.example.workers.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.workers.views.UserView

@InjectViewState
class UserPresenter : MvpPresenter<UserView>() {
    fun test(userId: Int) {
        viewState.showError(text = userId.toString())
    }
}