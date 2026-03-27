package com.example.diamondguesthouse.data.repo

import com.example.diamondguesthouse.core.EntityModelMapper
import com.example.diamondguesthouse.data.local.dao.CustomerDao
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import com.example.diamondguesthouse.di.MapperQualifiers
import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.repo.CustomerRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [CustomerRepository::class])
class CustomerRepositoryImpl(
    private val customerDao: CustomerDao,
    @Named(MapperQualifiers.CUSTOMER)
    private val customerMapper: EntityModelMapper<CustomerEntity, CustomerModel>,
) : CustomerRepository {

    override suspend fun findActiveCustomerByCnicOrPassport(
        cnic: String?,
        passportNo: String?,
        currentTime: Long,
    ): CustomerModel? =
        customerDao.findActiveCustomerByCnicOrPassport(cnic, passportNo, currentTime)
            ?.let { customerMapper.entityToModel(it) }

    override suspend fun getCustomersByCnicOrPassport(
        cnic: String?,
        passportNo: String?,
    ): List<CustomerModel> =
        customerDao.getCustomerByCnicOrPassport(cnic, passportNo)
            .map { customerMapper.entityToModel(it) }
}
