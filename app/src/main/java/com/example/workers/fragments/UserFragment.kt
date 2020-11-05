package com.example.workers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.workers.R
import com.example.workers.presenters.UserPresenter
import com.example.workers.views.UserView

class UserFragment : MvpAppCompatFragment(), UserView {

    @InjectPresenter
    lateinit var userPresenter: UserPresenter

    companion object {
        fun getNewInstance(args: Bundle?): UserFragment {
            val userFragment = UserFragment()
            userFragment.arguments = args
            return userFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = this.arguments?.get("userId") as Int
        userPresenter.test(userId = userId)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun showError(textResource: Int) {
        Toast.makeText(context, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}