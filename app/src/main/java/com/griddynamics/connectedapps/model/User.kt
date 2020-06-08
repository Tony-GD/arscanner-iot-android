package com.griddynamics.connectedapps.model

data class User(
    val id: String?,
    val givenName: String?,
    val familyName: String?,
    val email: String?,
    val photoUrl: String?
)

val EmptyUser = User(id = "", givenName = "", familyName = "", email = "", photoUrl = "")