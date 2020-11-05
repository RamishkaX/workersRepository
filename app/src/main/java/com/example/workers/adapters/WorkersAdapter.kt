package com.example.workers.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workers.R
import com.example.workers.listeners.OnAdapterItemListener
import com.example.workers.models.SpecialityModel
import com.example.workers.models.UserModel
import com.example.workers.presenters.WorkersFragmentPresenter
import com.example.workers.tools.ageCalc
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URL
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class WorkersAdapter(var workersFragmentPresenter: WorkersFragmentPresenter, var onAdapterItemListener: OnAdapterItemListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            workersFragmentPresenter = workersFragmentPresenter,
            resources = Resources.getSystem()
        )
    }

    override fun getItemCount(): Int {
        return workersList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WorkersViewHolder) {
            holder.bind(workerModel = workersList[position], onAdapterItemListener = onAdapterItemListener)
        }
    }

    class WorkersViewHolder(itemView: View, val workersList: ArrayList<UserModel>, workersFragmentPresenter: WorkersFragmentPresenter, val resources: Resources) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var civAvatar: CircleImageView = itemView.findViewById(R.id.workers_civ_avatar)
        private var txtName: TextView = itemView.findViewById(R.id.workers_txt_name)
        private var txtAge: TextView = itemView.findViewById(R.id.workers_txt_age)
        private lateinit var onAdapterItemListener: OnAdapterItemListener

        fun bind(workerModel: UserModel, onAdapterItemListener: OnAdapterItemListener) {

            civAvatar.setImageResource(R.drawable.ic_android_black_24dp)

            try {
                URL(workerModel.avatarUrl)
                Picasso.get().load(workerModel.avatarUrl).into(civAvatar)
            }
            catch (e: Exception) { }

            txtName.text = "${workerModel.name} ${workerModel.lastName}"
            txtAge.text = "-"

            workerModel.birthday?.let {
                txtAge.text = "Возраст: ${ageCalc(it)}"
            }

            this.onAdapterItemListener = onAdapterItemListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            this.onAdapterItemListener.onItemClick(workersList[adapterPosition].id)
        }
    }
}