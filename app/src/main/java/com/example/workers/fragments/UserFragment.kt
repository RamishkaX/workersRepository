package com.example.workers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.workers.R
import com.example.workers.handlers.DBHandler
import com.example.workers.models.UserModel
import com.example.workers.presenters.UserPresenter
import com.example.workers.tools.ageCalc
import com.example.workers.views.UserView
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URL

class UserFragment : MvpAppCompatFragment(), UserView {

    @InjectPresenter
    lateinit var userPresenter: UserPresenter

    private lateinit var cpvWait: CircularProgressView
    private lateinit var civAvatar: CircleImageView
    private lateinit var txtName: TextView
    private lateinit var txtAge: TextView
    private lateinit var txtBirthday: TextView
    private lateinit var txtSpeciality: TextView

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
        cpvWait = view.findViewById(R.id.cpv_user)
        civAvatar = view.findViewById(R.id.user_civ_avatar)
        txtName = view.findViewById(R.id.user_txt_name)
        txtAge = view.findViewById(R.id.user_txt_age)
        txtBirthday = view.findViewById(R.id.user_txt_birthday)
        txtSpeciality = view.findViewById(R.id.user_txt_speciality)

        val userId = this.arguments?.get("userId") as Int
        val dbHandler = DBHandler(context = context!!)

        userPresenter.loadUser(userId = userId, dbHandler = dbHandler)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun startLoading() {
        cpvWait.visibility = View.VISIBLE
    }

    override fun endLoading() {
        cpvWait.visibility = View.GONE
    }

    override fun setupUser(user: UserModel) {
        civAvatar.setImageResource(R.drawable.ic_android_black_24dp)

        try {
            URL(user.avatarUrl)
            Picasso.get().load(user.avatarUrl).into(civAvatar)
        }
        catch (e: Exception) { }

        txtName.text = "${user.name} ${user.lastName}"
        txtAge.text = "-"
        txtBirthday.text = "-"

        user.birthday?.let {
            txtAge.text = "Возраст: ${ageCalc(it)}"
            txtBirthday.text = it
        }

        var specialities = ""
        user.speciality.forEach {
            specialities += "${it.name} "
        }

        txtSpeciality.text = specialities
    }

    override fun showError(textResource: Int) {
        Toast.makeText(context, getString(textResource), Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}