package com.example.diamondguesthouse.domain.models

/** Form row for add-record flow (plain data, no Compose state). */
data class CustomerDraftModel(
    val name: String = "",
    val fatherName: String = "",
    val cellNo: String = "",
    val cnic: String? = null,
    val permanentAddress: String = "",
    val selectedGender: String = "Please Select",
    val country: String? = "",
    val passportNo: String? = null,
    val visaUpTill: Long? = 0L,
)
