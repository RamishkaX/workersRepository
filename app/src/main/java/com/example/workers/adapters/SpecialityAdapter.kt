package com.example.workers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.workers.R
import com.example.workers.models.SpecialityModel
import com.example.workers.presenters.SpecialityPresenter

class SpecialityAdapter(var specialityPresenter: SpecialityPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var specialityList: ArrayList<SpecialityModel> = ArrayList()

    fun setupSpeciality(specialityModel: ArrayList<SpecialityModel>) {
        specialityList.clear()
        specialityList.addAll(specialityModel)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_speciality, parent, false)

        return SpecialityViewHolder(itemView = itemView, specialityPresenter = specialityPresenter, specialityModel = specialityList)
    }

    override fun getItemCount(): Int {
        return specialityList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SpecialityViewHolder) {
            holder.bind(specialityModel = specialityList[position])
        }
    }

    class SpecialityViewHolder(itemView: View, specialityPresenter: SpecialityPresenter, specialityModel: ArrayList<SpecialityModel>): RecyclerView.ViewHolder(itemView) {
        private var txtSpeciality: TextView = itemView.findViewById(R.id.speciality_txt_speciality)

        init {
            itemView.setOnClickListener { v: View? ->
                specialityPresenter.setSpeciality(specialityId = specialityModel[adapterPosition].id)
            }
        }

        fun bind(specialityModel: SpecialityModel) {
            txtSpeciality.text = specialityModel.name
        }
    }
}