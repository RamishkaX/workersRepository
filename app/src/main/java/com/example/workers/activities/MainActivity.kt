package com.example.workers.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.workers.R
import com.example.workers.adapters.SpecialityAdapter
import com.example.workers.handlers.DBHandler
import com.example.workers.models.*
import com.example.workers.presenters.SpecialityPresenter
import com.example.workers.views.SpecialityView
import com.github.rahatarmanahmed.cpv.CircularProgressView

class MainActivity : MvpAppCompatActivity(), SpecialityView {

    @InjectPresenter
    lateinit var specialityPresenter: SpecialityPresenter

    private lateinit var cpvWait: CircularProgressView
    private lateinit var rvSpeciality: RecyclerView
    private lateinit var specialityAdapter: SpecialityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cpvWait = findViewById(R.id.cpv_speciality)
        rvSpeciality = findViewById(R.id.recycler_speciality)

        val dbHandler = DBHandler(context = applicationContext)
        specialityPresenter.loadUsers(dbHandler)

        specialityAdapter = SpecialityAdapter()
        rvSpeciality.adapter = specialityAdapter
        rvSpeciality.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rvSpeciality.setHasFixedSize(true)
    }

    override fun startLoading() {
        rvSpeciality.visibility = View.GONE
        cpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        cpvWait.visibility = View.GONE
    }

    override fun setupSpecialityList(specialityList: ArrayList<SpecialityModel>) {
        rvSpeciality.visibility = View.VISIBLE

        specialityAdapter.setupSpeciality(specialityList)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun specialityLoaded(dbHandler: DBHandler, workModel: WorkModel) {
        runOnUiThread {
            specialityPresenter.initDB(dbHandler, workModel)
        }
    }

    override fun specialityNotLoaded(text: String) {
        runOnUiThread {
            specialityPresenter.loadingError(text)
        }
    }
}