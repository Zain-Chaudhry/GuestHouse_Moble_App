package com.example.diamondguesthouse.data.mapper

import com.example.diamondguesthouse.core.EntityModelMapper
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import com.example.diamondguesthouse.domain.models.CustomerModel

class CustomerEntityModelMapper : EntityModelMapper<CustomerEntity, CustomerModel> {
    override fun entityToModel(entity: CustomerEntity): CustomerModel = CustomerModel(
        customerId = entity.customerId,
        roomNo = entity.roomNo,
        name = entity.name,
        fatherName = entity.fatherName,
        cellNo = entity.cellNo,
        cnic = entity.cnic,
        permanentAddress = entity.permanentAddress,
        selectedGender = entity.selectedGender,
        country = entity.country,
        passportNo = entity.passportNo,
        checkInDate = entity.checkInDate,
        checkOutDate = entity.checkOutDate,
        checkInTime = entity.checkInTime,
        checkOutTime = entity.checkOutTime,
        visaUpTill = entity.visaUpTill,
    )

    override fun modelToEntity(model: CustomerModel): CustomerEntity = CustomerEntity(
        customerId = model.customerId,
        roomNo = model.roomNo,
        name = model.name,
        fatherName = model.fatherName,
        cellNo = model.cellNo,
        cnic = model.cnic,
        permanentAddress = model.permanentAddress,
        selectedGender = model.selectedGender,
        country = model.country,
        passportNo = model.passportNo,
        checkInDate = model.checkInDate,
        checkOutDate = model.checkOutDate,
        checkInTime = model.checkInTime,
        checkOutTime = model.checkOutTime,
        visaUpTill = model.visaUpTill,
    )
}
