package com.example.diamondguesthouse.di

/**
 * [org.koin.core.qualifier.named] keys for [com.example.diamondguesthouse.core.EntityModelMapper]
 * bindings. Multiple mappers share the same erased type; qualifiers avoid ambiguous resolution
 * (similar to Hilt @Binds + @Named / custom qualifiers).
 */
object MapperQualifiers {
    const val ROOM = "entity_mapper_room"
    const val CUSTOMER = "entity_mapper_customer"
    const val ROOM_WITH_CUSTOMERS = "entity_mapper_room_with_customers"
}
