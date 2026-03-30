package com.example.diamondguesthouse.data.mapper

import com.example.diamondguesthouse.core.EntityModelMapper
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import com.example.diamondguesthouse.data.local.entities.RoomEntity
import com.example.diamondguesthouse.data.local.entities.RoomWithCustomers
import com.example.diamondguesthouse.di.MapperQualifiers
import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.models.RoomModel
import com.example.diamondguesthouse.domain.models.RoomWithCustomersModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
@Named(MapperQualifiers.ROOM_WITH_CUSTOMERS)
class RoomWithCustomersModelMapper(
    @Named(MapperQualifiers.ROOM) private val roomMapper: EntityModelMapper<RoomEntity, RoomModel>,
    @Named(MapperQualifiers.CUSTOMER) private val customerMapper: EntityModelMapper<CustomerEntity, CustomerModel>,
) : EntityModelMapper<RoomWithCustomers, RoomWithCustomersModel> {
    override fun entityToModel(entity: RoomWithCustomers): RoomWithCustomersModel =
        RoomWithCustomersModel(
            room = roomMapper.entityToModel(entity.room),
            customers = entity.customers.map { customerMapper.entityToModel(it) },
        )

    override fun modelToEntity(model: RoomWithCustomersModel): RoomWithCustomers =
        RoomWithCustomers(
            room = roomMapper.modelToEntity(model.room),
            customers = model.customers.map { customerMapper.modelToEntity(it) },
        )
}
