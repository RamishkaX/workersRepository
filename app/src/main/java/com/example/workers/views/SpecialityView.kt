package com.example.workers.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.workers.handlers.DBHandler
import com.example.workers.models.*

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface SpecialityView: MvpView {
    fun startLoading()
    fun endLoading()
    fun setupSpecialityList(specialityList: ArrayList<SpecialityModel>)
    fun showError(textResource: Int)
    fun showError(text: String)
    fun specialityLoaded(dbHandler: DBHandler, workModel: WorkModel)
    fun specialityNotLoaded(text: String)
}