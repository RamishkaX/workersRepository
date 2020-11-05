package com.example.workers.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.workers.R
import com.example.workers.adapters.SpecialityAdapter
import com.example.workers.fragments.UserFragment
import com.example.workers.fragments.WorkersFragment
import com.example.workers.handlers.DBHandler
import com.example.workers.listeners.OnAdapterItemListener
import com.example.workers.models.*
import com.example.workers.presenters.SpecialityPresenter
import com.example.workers.views.SpecialityView
import com.github.rahatarmanahmed.cpv.CircularProgressView

class MainActivity : MvpAppCompatActivity(), SpecialityView {

    @InjectPresenter
    lateinit var specialityPresenter: SpecialityPresenter

    private lateinit var cpvWait: CircularProgressView
    lateinit var rvSpeciality: RecyclerView
    private lateinit var specialityAdapter: SpecialityAdapter
    private lateinit var flFragment: FrameLayout
    private lateinit var workersFragment: WorkersFragment
    private lateinit var userFragment: UserFragment
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            supportFragmentManager.popBackStackImmediate()
            flFragment.visibility = View.GONE
        }
        else if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cpvWait = findViewById(R.id.cpv_speciality)
        rvSpeciality = findViewById(R.id.recycler_speciality)
        flFragment = findViewById(R.id.fl_fragment)
        val dbHandler = DBHandler(context = applicationContext)
        specialityPresenter.loadUsers(dbHandler)

        specialityAdapter = SpecialityAdapter(specialityPresenter)
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

        specialityAdapter.setupSpeciality(specialityModel = specialityList)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(applicationContext, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun specialityLoaded(dbHandler: DBHandler, workModel: WorkModel) {
        runOnUiThread {
            specialityPresenter.initDB(dbHandler = dbHandler, workModel = workModel)
        }
    }

    override fun specialityNotLoaded(text: String, dbHandler: DBHandler) {
        runOnUiThread {
            specialityPresenter.loadingError(text = text)
            specialityPresenter.loadSpecialityFromDB(dbHandler = dbHandler)
        }
    }

    override fun openWorkersFragment(specialityId: Int) {
        flFragment.visibility = View.VISIBLE
        fragmentTransaction = supportFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putInt("specialityId", specialityId)

        workersFragment = WorkersFragment.getNewInstance(bundle)

        fragmentTransaction.add(R.id.fl_fragment, workersFragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun openUserFragment(userId: Int) {
        fragmentTransaction = supportFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putInt("userId", userId)

        userFragment = UserFragment.getNewInstance(bundle)

        fragmentTransaction.replace(R.id.fl_fragment, userFragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}