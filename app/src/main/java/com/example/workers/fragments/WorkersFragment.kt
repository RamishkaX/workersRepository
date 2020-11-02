package com.example.workers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.workers.R
import com.example.workers.presenters.SpecialityPresenter
import com.example.workers.presenters.WorkersFragmentPresenter
import com.example.workers.views.WorkersFragmentView

class WorkersFragment : MvpAppCompatFragment(), WorkersFragmentView {

    @InjectPresenter
    lateinit var workersFragmentPresenter: WorkersFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        workersFragmentPresenter.test()
        val view = inflater.inflate(R.layout.fragment_workers, null)

        return view
    }



    override fun showError(textResource: Int) {
        Toast.makeText(context, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}