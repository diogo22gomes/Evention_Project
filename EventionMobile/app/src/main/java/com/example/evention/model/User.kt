package com.example.evention.model

import java.util.Date

data class User(
    val userID: String,
    val username: String,
    val phone: Int?,
    val email: String,
    val password: String?,
    val status: Boolean,
    val createdAt: Date,
    val loginType: String,
    val userType: UserType,
    val usertype_id: String,
    val address: Address?,
    val profilePicture: String?
)

data class UserType(
    val userTypeID: String,
    val type: String
)

data class Address(
    val addressID: String,
    val road: String,
    val roadNumber: Int,
    val postCode: String,
    val NIF: String?,
    val localtown_id: String,
)

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)