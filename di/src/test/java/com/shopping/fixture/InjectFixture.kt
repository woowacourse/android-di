package com.shopping.fixture

import com.shopping.di.annotation.Inject
import com.shopping.di.annotation.QualifierTag

interface FixtureCar

data class ElectricFixtureCarImpl(
    val name: String = "",
) : FixtureCar

data class EngineFixtureCarImpl(
    val name: String = "",
) : FixtureCar

data class SingletonFixtureCar(
    val name: String = "",
)

data class FactoryFixtureCar(
    val name: String = "",
)

class FieldInjectFixtureCar {
    @Inject
    lateinit var fixtureCar: FixtureCar
}

class FieldInjectQualifierEngineFixtureCar {
    @Inject
    @QualifierTag("engine")
    lateinit var fixtureCar: FixtureCar
}

data class ConstructorInjectFixtureCar(
    val fixtureCar: FixtureCar,
)
