package com.example.workers.models
import com.beust.klaxon.Json
data class UserModel(@Json(ignored = true) var id: Int = 0,
                     @Json(name = "f_name") var name: String,
                     @Json(name = "l_name") var lastName: String,
                     @Json(name = "birthday") var birthday: String?,
                     @Json(name = "avatr_url") var avatarUrl: String?,
                     @Json(name = "specialty") var speciality: ArrayList<SpecialityModel>)