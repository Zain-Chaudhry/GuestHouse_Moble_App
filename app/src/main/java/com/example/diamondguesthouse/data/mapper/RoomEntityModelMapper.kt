package com.example.diamondguesthouse.data.mapper

import com.example.diamondguesthouse.core.EntityModelMapper
import com.example.diamondguesthouse.data.local.entities.RoomEntity
import com.example.diamondguesthouse.di.MapperQualifiers
import com.example.diamondguesthouse.domain.models.RoomModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
@Named(MapperQualifiers.ROOM)
class RoomEntityModelMapper : EntityModelMapper<RoomEntity, RoomModel> {
    override fun entityToModel(entity: RoomEntity): RoomModel = RoomModel(
        roomId = entity.roomId,
        roomNo = entity.roomNo,
        roomPrice = entity.roomPrice,
        checkInDate = entity.checkInDate,
        checkOutDate = entity.checkOutDate,
        checkInTime = entity.checkInTime,
        checkOutTime = entity.checkOutTime,
    )

    override fun modelToEntity(model: RoomModel): RoomEntity = RoomEntity(
        roomId = model.roomId,
        roomNo = model.roomNo,
        roomPrice = model.roomPrice,
        checkInDate = model.checkInDate,
        checkOutDate = model.checkOutDate,
        checkInTime = model.checkInTime,
        checkOutTime = model.checkOutTime,
    )
}
