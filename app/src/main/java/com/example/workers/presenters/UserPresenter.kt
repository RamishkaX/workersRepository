package com.example.workers.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.workers.R
import com.example.workers.handlers.DBHandler
import com.example.workers.views.UserView

@InjectViewState
class UserPresenter : MvpPresenter<UserView>() {
    fun loadUser(userId: Int, dbHandler: DBHandler) {
        viewState.startLoading()
        val user = dbHandler.getUserById(db = dbHandler.readableDatabase, userId = userId)

        dbHandler.close()
        viewState.endLoading()
        viewState.setupUser(user = user)
    }
}