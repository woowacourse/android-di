package com.shopping.fixture

interface FixtureCar

data class ElectricFixtureCarImpl(
    val name: String,
) : FixtureCar

data class EngineFixtureCarImpl(
    val name: String,
) : FixtureCar

data class SingletonFixtureCar(
    val name: String,
)

data class FactoryFixtureCar(
    val name: String,
)
