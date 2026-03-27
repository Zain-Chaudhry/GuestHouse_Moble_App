package com.example.diamondguesthouse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers WHERE (cnic = :cnic OR passportNo = :passportNo) AND checkOutTime > :currentTime LIMIT 1")
    suspend fun findActiveCustomerByCnicOrPassport(
        cnic: String?,
        passportNo: String?,
        currentTime: Long,
    ): CustomerEntity?

    @Query("SELECT * FROM customers WHERE cnic = :cnic OR passportNo = :passportNo LIMIT 1")
    suspend fun getCustomerByCnicOrPassport(cnic: String?, passportNo: String?): List<CustomerEntity>
}
