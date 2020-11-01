package com.example.workers.models

import com.beust.klaxon.Json

data class JsonModel(@Json(name = "response") var response: ArrayList<UserModel>)