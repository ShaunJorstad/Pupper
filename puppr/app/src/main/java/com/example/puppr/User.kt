package com.example.puppr

data class User(
    var fName: String? = "",
    var lName: String? = "",
    var email: String? = "",
    var address: String? = "",
    var phoneNumber: Int? = -1,
    var ageUpper: Int? = -1,
    var ageLower: Int? = -1,
    var preferredBreeds: List<String>? = null
)