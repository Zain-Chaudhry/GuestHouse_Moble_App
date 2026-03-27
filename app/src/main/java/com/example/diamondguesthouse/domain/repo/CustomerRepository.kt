package com.example.diamondguesthouse.domain.repo

import com.example.diamondguesthouse.domain.models.CustomerModel

interface CustomerRepository {
    suspend fun findActiveCustomerByCnicOrPassport(
        cnic: String?,
        passportNo: String?,
        currentTime: Long,
    ): CustomerModel?

    suspend fun getCustomersByCnicOrPassport(cnic: String?, passportNo: String?): List<CustomerModel>
}
