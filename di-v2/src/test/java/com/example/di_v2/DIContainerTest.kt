package com.example.di_v2

import com.example.di_v2.annotation.ActivityScoped
import com.example.di_v2.annotation.AppScoped
import com.example.di_v2.annotation.Inject
import com.example.di_v2.annotation.Qualifier
import com.example.di_v2.annotation.Unscoped
import com.example.di_v2.annotation.ViewModelScoped
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
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

    @Test
    fun `AppScoped는_항상_같은_인스턴스를_반환한다`() {
        val di = DIContainer()

        val first = di.resolve(AppScopedObject::class)
        val second = di.resolve(AppScopedObject::class)

        assertSame(first, second)
    }

    @Test
    fun `ActivityScoped는_owner별로_다른_인스턴스를_가지고_clearScope로_정리된다`() {
        // Given
        val di = DIContainer()
        val activity1 = Any()
        val activity2 = Any()

        // When
        val a1First = di.resolve(ActivityScopedObject::class, owner = activity1)
        val a1Second = di.resolve(ActivityScopedObject::class, owner = activity1)
        val a2First = di.resolve(ActivityScopedObject::class, owner = activity2)

        // Then
        // 같은 owner는 같은 인스턴스
        assertSame(a1First, a1Second)
        // 다른 owner는 다른 인스턴스
        assertNotSame(a1First, a2First)

        // clearScope 후 다시 생성되는지 확인
        di.clearScope(activity1)
        val a1New = di.resolve(ActivityScopedObject::class, owner = activity1)
        assertNotSame(a1First, a1New)
    }

    @Test
    fun `ViewModelScoped도_owner별로_다른_인스턴스를_가지고_clearScope로_정리된다`() {
        // Given
        val di = DIContainer()
        val viewModel1 = Any()
        val viewModel2 = Any()

        // Then
        val v1First = di.resolve(ViewModelScopedObject::class, owner = viewModel1)
        val v1Second = di.resolve(ViewModelScopedObject::class, owner = viewModel1)
        val v2First = di.resolve(ViewModelScopedObject::class, owner = viewModel2)

        // Then
        assertSame(v1First, v1Second)
        assertNotSame(v1First, v2First)

        di.clearScope(viewModel1)
        val v1New = di.resolve(ViewModelScopedObject::class, owner = viewModel1)
        assertNotSame(v1First, v1New)
    }

    @Test
    fun `Unscoped는_매번_새로운_인스턴스를_생성한다`() {
        val di = DIContainer()
        val first = di.resolve(UnscopedObject::class)
        val second = di.resolve(UnscopedObject::class)

        assertNotSame(first, second)
    }
}
