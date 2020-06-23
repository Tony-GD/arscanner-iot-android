package com.griddynamics.connectedapps.model

data class User(
    val uid: String?,
    val token: String?,
    val givenName: String?,
    val familyName: String?,
    val email: String?,
    val photoUrl: String?
)

val EmptyUser = User(uid = "", token = "", givenName = "", familyName = "", email = "", photoUrl = "")