package com.example.workers.models

import com.beust.klaxon.Json

data class SpecialityModel(@Json(name = "specialty_id") var id: Int,
                           @Json(name = "name") var name: String) {
}