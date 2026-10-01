package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Test

interface Service

class FirstService : Service

class SecondService : Service

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class First

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Second

class ConstructorTarget(
    @First val service: Service,
)

class FieldTarget {
    @field:Inject
    @field:Second
    lateinit var service: Service
}

class ConstructorCycleA(
    val cycleB: ConstructorCycleB,
)

class ConstructorCycleB(
    val cycleA: ConstructorCycleA,
)

class FieldCycleA {
    @field:Inject
    lateinit var cycleB: FieldCycleB
}

class FieldCycleB {
    @field:Inject
    lateinit var cycleA: FieldCycleA
}

class ContainerTest {
    @Test
    fun `생성자와 필드의 Qualifier로 구현체를 선택한다`() {
        val container = Container()
        val first = FirstService()
        val second = SecondService()
        container.register(Service::class, first, First::class)
        container.register(Service::class, second, Second::class)

        assertSame(first, container.create(ConstructorTarget::class).service)
        assertSame(second, container.create(FieldTarget::class).service)
    }

    @Test
    fun `구현체가 둘인데 Qualifier가 없으면 명확한 오류를 낸다`() {
        val container = Container()
        container.bind(Service::class, FirstService::class, First::class)
        container.bind(Service::class, SecondService::class, Second::class)

        val error = assertThrows(IllegalArgumentException::class.java) { container.resolve(Service::class) }

        assertEquals(true, error.message?.contains("Qualifier를 지정해야 합니다"))
    }

    @Test
    fun `등록된 구현체의 의존성을 재귀적으로 생성하고 재사용한다`() {
        val container = Container()
        container.bind(Service::class, FirstService::class, First::class)

        assertSame(container.resolve(Service::class, First::class), container.create(ConstructorTarget::class).service)
    }

    @Test
    fun `생성자 순환 의존성의 경로를 오류로 보고한다`() {
        val container = Container()

        val error = assertThrows(IllegalStateException::class.java) { container.create(ConstructorCycleA::class) }

        assertEquals("의존성: ConstructorCycleA - ConstructorCycleB - ConstructorCycleA", error.message)
    }

    @Test
    fun `필드 순환 의존성의 경로를 오류로 보고한다`() {
        val container = Container()

        val error = assertThrows(IllegalStateException::class.java) { container.create(FieldCycleA::class) }

        assertEquals("의존성: FieldCycleA - FieldCycleB - FieldCycleA", error.message)
    }
}
