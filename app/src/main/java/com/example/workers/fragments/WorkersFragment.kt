package com.example.workers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.workers.R
import com.example.workers.adapters.SpecialityAdapter
import com.example.workers.adapters.WorkersAdapter
import com.example.workers.handlers.DBHandler
import com.example.workers.models.UserModel
import com.example.workers.presenters.SpecialityPresenter
import com.example.workers.presenters.WorkersFragmentPresenter
import com.example.workers.views.WorkersFragmentView
import com.github.rahatarmanahmed.cpv.CircularProgressView

class WorkersFragment : MvpAppCompatFragment(), WorkersFragmentView {

    @InjectPresenter
    lateinit var workersFragmentPresenter: WorkersFragmentPresenter

    private lateinit var cpvWait: CircularProgressView
    private lateinit var rvWorkers: RecyclerView
    private lateinit var workersAdapter: WorkersAdapter

    companion object {
        fun getNewInstance(args: Bundle?): WorkersFragment {
            val workersFragment = WorkersFragment()
            workersFragment.arguments = args
            return workersFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workers, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cpvWait = view.findViewById(R.id.cpv_worker)
        rvWorkers = view.findViewById(R.id.recycler_workers)

        val dbHandler = DBHandler(context = context!!)
        val specialityId = this.arguments?.get("specialityId") as Int
        workersFragmentPresenter.loadWorkers(specialityId = specialityId, dbHandler = dbHandler)

        workersAdapter = WorkersAdapter()
        rvWorkers.adapter = workersAdapter
        rvWorkers.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvWorkers.setHasFixedSize(true)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun startLoading() {
        cpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        cpvWait.visibility = View.GONE
    }

    override fun setupWorkersList(workersList: ArrayList<UserModel>) {
        workersAdapter.setupWorkers(workersList = workersList)
    }

    override fun openUserFragment(userId: Int) {

    }

    override fun showError(textResource: Int) {
        Toast.makeText(context, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}