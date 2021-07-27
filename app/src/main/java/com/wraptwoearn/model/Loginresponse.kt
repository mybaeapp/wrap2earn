package com.wraptwoearn.model

data class Loginresponse(
    val first_name: String,
    val last_name: String,
    val message: String,
    val status: String,
    val user_car_number: String,
    val user_id: Int
)