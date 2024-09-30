package com.example.diamondguesthouse.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diamondguesthouse.data.model.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    // Retrieve all customer records
    @Query("SELECT * FROM customers")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    // Insert customer record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)


    @Query("SELECT * FROM Customers WHERE (cnic = :cnic OR passportNo = :passportNo) AND checkOutTime > :currentTime LIMIT 1")
    suspend fun findActiveCustomerByCnicOrPassport(cnic: String?, passportNo: String?, currentTime: Long): CustomerEntity?
    // Retrieve customer by CNIC or Passport
    @Query("SELECT * FROM customers WHERE cnic = :cnic OR passportNo = :passportNo LIMIT 1")
    suspend fun getCustomerByCnicOrPassport(cnic: String?, passportNo: String?): List<CustomerEntity>
//
//    // Get check-ins and check-outs for today
//    @Query("SELECT COUNT(*) FROM customers WHERE checkInDate = :today")
//     fun getCheckIns(today: Long): Int
//
//    @Query("SELECT COUNT(*) FROM customers WHERE checkOutDate = :today")
//    fun getCheckOuts(today: Long): Int


}
