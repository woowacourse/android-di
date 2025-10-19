package com.example.di

import com.example.di.annotation.*
import org.junit.Assert.*
import org.junit.Test

@Qualifier
annotation class Potato

@Qualifier
annotation class Pepperoni

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

@AppScoped
class AppScopedObject

@ActivityScoped
class ActivityScopedObject

@ViewModelScoped
class ViewModelScopedObject

@Unscoped
class UnscopedObject

class DIContainerTest {
    @Test
    fun `register로 등록된 피자를 get으로 불러올 수 있다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class) { PotatoPizza() }

        // When
        val pizza = di.get(Pizza::class)

        // Then
        assertTrue(pizza is PotatoPizza)
        assertEquals("포테이토 피자!", pizza.taste())
    }

    @Test
    fun `Inject 생성자가 있는 클래스는 자동으로 의존성을 주입받는다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class) { PepperoniPizza() }

        // When
        val store = di.get(PizzaStore::class)

        // Then
        assertEquals("페퍼로니 피자!", store.serve())
    }

    @Test
    fun `Inject 필드가 있는 객체는 자동으로 주입된다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class) { PotatoPizza() }
        val warehouse = Warehouse()

        // When
        val pizzaStore = di.get(PizzaStore::class)
        warehouse.pizzaStore = pizzaStore

        // Then
        assertEquals("포테이토 피자!", warehouse.pizzaStore.serve())
    }

    @Test
    fun `Qualifier가 붙은 피자를 구분하여 주입할 수 있다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class, Potato::class) { PotatoPizza() }
        di.register(Pizza::class, Pepperoni::class) { PepperoniPizza() }

        // When
        val bike = di.get(DeliveryBike::class)

        // Then
        assertEquals("배달 완료: 포테이토 피자!", bike.deliver())
    }

    @Test
    fun `AppScoped는 항상 같은 인스턴스를 반환한다`() {
        // Given
        val di = DIContainer()

        // When
        val first = di.get(AppScopedObject::class)
        val second = di.get(AppScopedObject::class)

        // Then
        assertSame(first, second)
    }

    @Test
    fun `ActivityScoped는 owner별로 다른 인스턴스를 가진다`() {
        // Given
        val di = DIContainer()
        val activity1 = Any()
        val activity2 = Any()

        // When
        val a1First = di.get(ActivityScopedObject::class, owner = activity1)
        val a1Second = di.get(ActivityScopedObject::class, owner = activity1)
        val a2First = di.get(ActivityScopedObject::class, owner = activity2)

        // Then
        // 같은 owner는 같은 인스턴스
        assertSame(a1First, a1Second)
        // 다른 owner는 다른 인스턴스
        assertNotSame(a1First, a2First)

        // And
        // clear 후 새 인스턴스 생성 확인
        di.clearScope(activity1)
        val a1New = di.get(ActivityScopedObject::class, owner = activity1)
        assertNotSame(a1First, a1New)
    }

    @Test
    fun `ViewModelScoped도 owner별로 다른 인스턴스를 가진다`() {
        // Given
        val di = DIContainer()
        val vm1 = Any()
        val vm2 = Any()

        // When
        val v1First = di.get(ViewModelScopedObject::class, owner = vm1)
        val v1Second = di.get(ViewModelScopedObject::class, owner = vm1)
        val v2First = di.get(ViewModelScopedObject::class, owner = vm2)

        // Then
        assertSame(v1First, v1Second)
        assertNotSame(v1First, v2First)

        // And
        di.clearScope(vm1)
        val v1New = di.get(ViewModelScopedObject::class, owner = vm1)
        assertNotSame(v1First, v1New)
    }

    @Test
    fun `Unscoped는 매번 새로운 인스턴스를 생성한다`() {
        // Given
        val di = DIContainer()

        // When
        val first = di.get(UnscopedObject::class)
        val second = di.get(UnscopedObject::class)

        // Then
        assertNotSame(first, second)
    }

    @Test
    fun `registerOwnerAware로 등록된 팩토리는 owner를 전달받아 동작한다`() {
        // Given
        val di = DIContainer()
        val owner = Any()
        var receivedOwner: Any? = null

        di.registerFactory(Pizza::class) { o ->
            receivedOwner = o
            PotatoPizza()
        }

        // When
        val pizza = di.get(Pizza::class, owner = owner)

        // Then
        assertTrue(pizza is PotatoPizza)
        assertSame(owner, receivedOwner)
    }

    @Test
    fun `등록된 factory가 존재하면 스코프 어노테이션 없이도 resolveFactory를 통해 인스턴스가 생성된다`() {
        // Given
        val di = DIContainer()
        di.register(Pizza::class, Potato::class) { PotatoPizza() }

        // When
        val pizza = di.get(Pizza::class, qualifier = Potato::class)

        // Then
        assertTrue(pizza is PotatoPizza)
    }
}
