package com.example.workers.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.beust.klaxon.Klaxon
import com.example.workers.R
import com.example.workers.handlers.DBHandler
import com.example.workers.models.*
import com.example.workers.views.SpecialityView
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@InjectViewState
class SpecialityPresenter : MvpPresenter<SpecialityView>() {
    fun loadUsers(dbHandler: DBHandler) {
        val jsonURL = "https://gitlab.65apps.com/65gb/static/raw/master/testTask.json"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(jsonURL)
            .build()

        viewState.startLoading()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val klaxon = Klaxon()
                val result = klaxon.parse<JsonModel>(response.body?.string().toString())
                val workModel = createElementsArray(jsonModel = result)

                viewState.specialityLoaded(dbHandler = dbHandler, workModel = workModel)
            }

            override fun onFailure(call: Call, e: IOException) {
                viewState.specialityNotLoaded(text = e.message.toString(), dbHandler = dbHandler)
            }
        })
    }

    fun initDB(dbHandler: DBHandler, workModel: WorkModel) {
        val database = dbHandler.writableDatabase

        dbHandler.onUpgrade(db = database, newVersion = 2, oldVersion = 1)
        dbHandler.insertUser(db = database, users = workModel.workers!!)
        dbHandler.insertSpeciality(db = database, speciality = workModel.speciality!!)
        dbHandler.insertWorkersSpeciality(db = database, workersSpeciality = workModel.workersSpeciality!!)

        viewState.endLoading()
        viewState.setupSpecialityList(specialityList = workModel.speciality!!)
    }

    fun loadSpecialityFromDB(dbHandler: DBHandler) {
        val specialityModelList = dbHandler.getSpecialitys(db = dbHandler.readableDatabase)
        dbHandler.close()
        viewState.endLoading()

        if (specialityModelList.isNullOrEmpty()) {
            viewState.showError(R.string.DB_error)
        } else {
            viewState.setupSpecialityList(specialityModelList)
        }
    }

    fun loadingError(text: String) {
        viewState.endLoading()
        viewState.showError(text = text)
    }

    fun setSpeciality(specialityId: Int) {
        viewState.openWorkersFragment(specialityId = specialityId)
    }

    private fun createElementsArray(jsonModel: JsonModel?): WorkModel {
        val workModel = WorkModel(workers = ArrayList(), speciality = ArrayList(), workersSpeciality = ArrayList())

        var userId: Int = 1
        for (element in jsonModel!!.response) {
            val user = UserModel(
                id = userId++,
                name = reformText(element.name),
                lastName = reformText(element.lastName),
                birthday = formatDate(element.birthday),
                avatarUrl = element.avatarUrl,
                speciality = ArrayList()
            )

            workModel.workers?.add(user)

            for (speciality in element.speciality) {
                if (!workModel.speciality.isNullOrEmpty()) {
                    if (!workModel.speciality!!.contains(speciality)) {
                        workModel.speciality?.add(
                            SpecialityModel(
                                id = speciality.id,
                                name = speciality.name
                            )
                        )
                    }
                } else {
                    workModel.speciality?.add(
                        SpecialityModel(
                            id = speciality.id,
                            name = speciality.name
                        )
                    )
                }
                workModel.workersSpeciality?.add(
                    WorkersSpeciality(
                        userId = user.id,
                        specialityId = speciality.id
                    )
                )
            }
        }

        return workModel
    }

    private fun reformText(text: String): String = text.toLowerCase().capitalize()

    private fun formatDate(dateText: String?): String? {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")

        if (dateText.isNullOrEmpty()) {
            return null
        }

        if (dateText?.indexOf('-') == 2) {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = inputFormat.parse(dateText)

            return dateFormat.format(date)
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = inputFormat.parse(dateText)

            return dateFormat.format(date)
        }
        //element.birthday?: dateFormat.format(element.birthday)
    }
}