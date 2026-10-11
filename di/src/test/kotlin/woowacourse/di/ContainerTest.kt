package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
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

class ScopedService

class ScopedConsumer(
    val service: ScopedService,
)

class RootService

class ConsumerOfRootService(
    val service: RootService,
)

class RootConsumerOfScopedService(
    val service: ScopedService,
)

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

    @Test
    fun `외부에서 새 스코프 종류를 추가해 인스턴스를 재사용한다`() {
        val container = Container()
        val customScope = ScopeType("custom")
        container.bind(ScopedService::class, ScopedService::class, scope = customScope)
        val firstScope = container.openScope(customScope)
        val secondScope = container.openScope(customScope)

        val firstInstance = firstScope.resolve(ScopedService::class)

        assertSame(firstInstance, firstScope.resolve(ScopedService::class))
        assertNotSame(firstInstance, secondScope.resolve(ScopedService::class))
    }

    @Test
    fun `자식 스코프는 부모에 등록한 앱 객체를 공유한다`() {
        val container = Container()
        val rootService = RootService()
        container.register(RootService::class, rootService)
        val screenScope = container.openScope(ScopeType("screen"))
        val viewModelScope = container.openScope(ScopeType("view_model"), screenScope)

        val consumer = viewModelScope.create(ConsumerOfRootService::class)

        assertSame(rootService, consumer.service)
        assertSame(rootService, container.resolve(RootService::class))
    }

    @Test
    fun `부모 스코프를 닫으면 자식 스코프의 캐시도 비우고 조회를 막는다`() {
        val container = Container()
        val screenScopeType = ScopeType("screen")
        val viewModelScopeType = ScopeType("view_model")
        container.bind(ScopedService::class, ScopedService::class, scope = viewModelScopeType)
        val screenScope = container.openScope(screenScopeType)
        val viewModelScope = container.openScope(viewModelScopeType, screenScope)
        viewModelScope.resolve(ScopedService::class)

        screenScope.close()

        assertTrue(screenScope.isClosed)
        assertTrue(viewModelScope.isClosed)
        assertEquals(0, screenScope.cachedInstanceCount)
        assertEquals(0, viewModelScope.cachedInstanceCount)
        assertThrows(IllegalStateException::class.java) { viewModelScope.resolve(ScopedService::class) }
    }

    @Test
    fun `짧은 스코프 의존성이 필요하면 현재 스코프에서 오류를 낸다`() {
        val container = Container()
        val viewModelScope = ScopeType("view_model")
        container.bind(ScopedService::class, ScopedService::class, scope = viewModelScope)

        val error =
            assertThrows(IllegalStateException::class.java) {
                container.create(RootConsumerOfScopedService::class)
            }

        assertEquals("ScopedService: view_model 스코프가 열려 있지 않습니다.", error.message)
    }
}
