package com.example.diamondguesthouse.core

interface EntityModelMapper<Entity, Model> {
    fun entityToModel(entity: Entity): Model
    fun modelToEntity(model: Model): Entity
}
