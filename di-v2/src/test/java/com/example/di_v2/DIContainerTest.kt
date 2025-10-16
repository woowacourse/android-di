package com.example.di_v2

import com.example.di_v2.annotation.Inject
import com.example.di_v2.annotation.Qualifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@Qualifier
annotation class Potato // 예: A 피자

@Qualifier
annotation class Pepperoni // 예: B 피자

interface Pizza {
    fun taste(): String
}

class PotatoPizza : Pizza {
    override fun taste() = "포테이토 피자!"
}

class PepperoniPizza : Pizza {
    override fun taste() = "페퍼로니 피자!"
}

class PizzaStore
    @Inject
    constructor(
        private val pizza: Pizza,
    ) {
        fun serve() = pizza.taste()
    }

class Warehouse {
    @Inject
    lateinit var pizzaStore: PizzaStore
}

class DeliveryBike
    @Inject
    constructor(
        @Potato private val pizza: Pizza,
    ) {
        fun deliver() = "배달 완료: ${pizza.taste()}"
    }

class DIContainerTest {
    @Test
    fun `register로_등록된_피자를_resolve로_불러올_수_있다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class) { PotatoPizza() }

        // When
        val pizza = di.resolve(Pizza::class)

        // Then
        assertTrue(pizza is PotatoPizza)
        assertEquals("포테이토 피자!", pizza.taste())
    }

    @Test
    fun `Inject_생성자가_있는_클래스는_autoCreate로_자동_생성된다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class) { PepperoniPizza() }

        // When
        val store = di.resolve(PizzaStore::class)

        // Then
        assertEquals("페퍼로니 피자!", store.serve())
    }

    @Test
    fun `Inject_필드가_있는_객체는_inject로_자동_주입된다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class) { PotatoPizza() }
        val warehouse = Warehouse()

        // When
        di.inject(warehouse)

        // Then
        assertEquals("포테이토 피자!", warehouse.pizzaStore.serve())
    }

    @Test
    fun `Qualifier가_붙은_피자를_구분하여_주입할_수_있다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class, Potato::class) { PotatoPizza() }
        di.register(Pizza::class, Pepperoni::class) { PepperoniPizza() }

        // When
        val bike = di.resolve(DeliveryBike::class)

        // Then
        assertEquals("배달 완료: 포테이토 피자!", bike.deliver())
    }
}
