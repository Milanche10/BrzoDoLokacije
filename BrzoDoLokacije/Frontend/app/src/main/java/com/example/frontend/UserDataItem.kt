package com.example.frontend

import java.net.IDN

/*
data class UserDataItem(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String
)*/
data class UserDataItem(
    var id : Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var token: String? = null

)