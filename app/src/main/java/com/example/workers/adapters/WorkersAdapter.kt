package com.example.workers.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workers.R
import com.example.workers.models.SpecialityModel
import com.example.workers.models.UserModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URL
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class WorkersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var workersList: ArrayList<UserModel> = ArrayList()

    fun setupWorkers(workersList: ArrayList<UserModel>) {
        this.workersList.clear()
        this.workersList.addAll(workersList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_worker, parent, false)

        return WorkersViewHolder(
            itemView = itemView,
            workersList = workersList,
            resources = Resources.getSystem()
        )
    }

    override fun getItemCount(): Int {
        return workersList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WorkersViewHolder) {
            holder.bind(workerModel = workersList[position])
        }
    }

    class WorkersViewHolder(itemView: View, workersList: ArrayList<UserModel>, val resources: Resources) : RecyclerView.ViewHolder(itemView) {

        private var civAvatar: CircleImageView = itemView.findViewById(R.id.workers_civ_avatar)
        private var txtName: TextView = itemView.findViewById(R.id.workers_txt_name)
        private var txtAge: TextView = itemView.findViewById(R.id.workers_txt_age)

        fun bind(workerModel: UserModel) {
            civAvatar.setImageResource(R.drawable.ic_android_black_24dp)

            try {
                URL(workerModel.avatarUrl)
                Picasso.get().load(workerModel.avatarUrl).into(civAvatar)
            }
            catch (e: Exception) { }

            txtName.text = "${workerModel.name} ${workerModel.lastName}"
            txtAge.text = "-"

            workerModel.birthday.let {
                txtAge.text = "Возраст: ${ageCalc(it)}"
            }
        }

        private fun ageCalc(birthday: String?) : String {
            if (birthday.isNullOrEmpty()) {
                return "-"
            }

            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH)
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val birthdayArray = birthday.split('.')

            if (birthdayArray[1].toInt() >= month && birthdayArray[0].toInt() >= day) {
                return (year - birthdayArray[2].toInt()).toString()
            } else {
                return (year - birthdayArray[2].toInt() - 1).toString()
            }
        }
    }
}