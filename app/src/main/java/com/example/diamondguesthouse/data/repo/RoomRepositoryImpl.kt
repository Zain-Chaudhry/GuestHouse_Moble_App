package com.example.diamondguesthouse.data.repo

import com.example.diamondguesthouse.core.EntityModelMapper
import com.example.diamondguesthouse.data.local.dao.RoomsDao
import com.example.diamondguesthouse.data.local.entities.CustomerEntity
import com.example.diamondguesthouse.data.local.entities.RoomEntity
import com.example.diamondguesthouse.data.local.entities.RoomWithCustomers
import com.example.diamondguesthouse.domain.models.CustomerModel
import com.example.diamondguesthouse.domain.models.RoomModel
import com.example.diamondguesthouse.domain.models.RoomWithCustomersModel
import com.example.diamondguesthouse.di.MapperQualifiers
import com.example.diamondguesthouse.domain.repo.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [RoomRepository::class])
class RoomRepositoryImpl(
    private val roomsDao: RoomsDao,
    @Named(MapperQualifiers.ROOM_WITH_CUSTOMERS)
    private val relationalMapper: EntityModelMapper<RoomWithCustomers, RoomWithCustomersModel>,
    @Named(MapperQualifiers.ROOM)
    private val roomMapper: EntityModelMapper<RoomEntity, RoomModel>,
    @Named(MapperQualifiers.CUSTOMER)
    private val customerMapper: EntityModelMapper<CustomerEntity, CustomerModel>,
) : RoomRepository {

    override fun getAllRoomsWithCustomers(): Flow<List<RoomWithCustomersModel>> =
        roomsDao.getAllRoomsWithCustomers().map { list ->
            list.map { relationalMapper.entityToModel(it) }
        }

    override suspend fun insertRoomWithCustomers(room: RoomModel, customers: List<CustomerModel>) {
        roomsDao.insertRoomWithCustomers(
            roomMapper.modelToEntity(room),
            customers.map { customerMapper.modelToEntity(it) },
        )
    }

    override suspend fun getRoomByNumber(roomNo: String): RoomModel? =
        roomsDao.getRoomByNumber(roomNo)?.let { roomMapper.entityToModel(it) }

    override suspend fun getBookedCountBetween(startDate: Long, endDate: Long): Int =
        roomsDao.getCountOfRoomsBookedBetween(startDate, endDate)

    override suspend fun getTotalIncomeBetween(startDate: Long, endDate: Long): Double =
        roomsDao.getTotalIncomeBetweenDates(startDate, endDate) ?: 0.0
}
