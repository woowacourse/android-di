package com.cksckckcks.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ScopedContainerTest {
    private val screen = ScopeType("screen")

    @Test
    fun `같은 스코프에서는 인스턴스를 재사용하고 다른 소유자는 분리한다`() {
        val container = DiContainer()
        var createdCount = 0
        container.register(Dependency::class, scope = screen) {
            createdCount++
            Dependency()
        }
        val first = container.openScope(screen, "cart-1")
        val second = container.openScope(screen, "cart-2")

        val instance = first.getInstance(Dependency::class)

        assertSame(instance, first.getInstance(Dependency::class))
        assertNotSame(instance, second.getInstance(Dependency::class))
        assertEquals(2, createdCount)
    }

    @Test
    fun `같은 종류와 식별자로 열면 같은 스코프를 반환한다`() {
        val container = DiContainer()

        assertSame(container.openScope(screen, "cart"), container.openScope(ScopeType("screen"), "cart"))
    }

    @Test
    fun `스코프를 닫으면 인스턴스와 관리 참조를 제거한다`() {
        val container = DiContainer()
        container.register(Dependency::class, scope = screen) { Dependency() }
        val scope = container.openScope(screen, "cart")
        scope.getInstance(Dependency::class)
        val instances = scope.readPrivateField("instances") as Map<*, *>
        val scopes = container.readPrivateField("scopes") as Map<*, *>
        assertEquals(1, instances.size)
        assertEquals(1, scopes.size)

        scope.close()

        assertTrue(instances.isEmpty())
        assertTrue(scopes.isEmpty())
        assertNull(scope.readPrivateField("container"))
        assertFailsWith<IllegalStateException> { scope.getInstance(Dependency::class) }
        assertFailsWith<IllegalStateException> { scope.saveInstance(Dependency::class, Dependency()) }
    }

    @Test
    fun `같은 식별자의 스코프를 다시 열면 새 인스턴스를 생성한다`() {
        val container = DiContainer()
        container.register(Dependency::class, scope = screen) { Dependency() }
        val first = container.openScope(screen, "cart")
        val instance = first.getInstance(Dependency::class)

        container.closeScope(screen, "cart")
        val second = container.openScope(screen, "cart")
        first.close()

        assertNotSame(first, second)
        assertNotSame(instance, second.getInstance(Dependency::class))
        assertSame(second, container.openScope(screen, "cart"))
    }

    @Test
    fun `화면 스코프 종료는 기본 스코프와 다른 화면에 영향을 주지 않는다`() {
        val container = DiContainer()
        container.register(Dependency::class) { Dependency() }
        container.register(ScreenDependency::class, scope = screen) { ScreenDependency() }
        val first = container.openScope(screen, "cart-1")
        val second = container.openScope(screen, "cart-2")
        val shared = first.getInstance(Dependency::class)
        val secondInstance = second.getInstance(ScreenDependency::class)

        first.close()

        assertSame(shared, container.getInstance(Dependency::class))
        assertSame(shared, second.getInstance(Dependency::class))
        assertSame(secondInstance, second.getInstance(ScreenDependency::class))
    }

    @Test
    fun `외부에서 정의한 스코프를 코어 수정 없이 사용할 수 있다`() {
        val session = ScopeType("session")
        val container = DiContainer()
        container.register(Dependency::class, scope = session) { Dependency() }
        val scope = container.openScope(session, "user-1")
        val instance = scope.getInstance(Dependency::class)

        assertSame(instance, scope.getInstance(Dependency::class))
        scope.close()
        assertFailsWith<IllegalStateException> { scope.getInstance(Dependency::class) }
        assertNotSame(instance, container.openScope(session, "user-1").getInstance(Dependency::class))
    }

    @Test
    fun `식별자가 같아도 스코프 종류가 다르면 분리한다`() {
        val container = DiContainer()
        val session = ScopeType("session")
        val screenScope = container.openScope(screen, "same-id")
        val sessionScope = container.openScope(session, "same-id")
        val screenInstance = Dependency()
        val sessionInstance = Dependency()
        screenScope.saveInstance(Dependency::class, screenInstance)
        sessionScope.saveInstance(Dependency::class, sessionInstance)

        screenScope.close()

        assertSame(sessionInstance, sessionScope.getInstance(Dependency::class))
        assertSame(sessionScope, container.openScope(session, "same-id"))
    }

    @Test
    fun `필요한 종류의 스코프가 없으면 기본 스코프에 저장하지 않는다`() {
        val container = DiContainer()
        var createdCount = 0
        container.register(Dependency::class, scope = screen) {
            createdCount++
            Dependency()
        }

        val error = assertFailsWith<IllegalArgumentException> { container.getInstance(Dependency::class) }

        assertContains(error.message.orEmpty(), "screen")
        assertEquals(0, createdCount)
        container.openScope(screen, "cart").getInstance(Dependency::class)
        assertEquals(1, createdCount)
    }

    @Test
    fun `스코프에서도 Qualifier로 구현체를 구분한다`() {
        val container = DiContainer()
        container.register(AutoDiTest.Storage::class, AutoDiTest.Local::class, screen) { AutoDiTest.LocalStorage() }
        container.register(AutoDiTest.Storage::class, AutoDiTest.Memory::class, screen) { AutoDiTest.MemoryStorage() }
        val scope = container.openScope(screen, "cart")
        val local = scope.getInstance(AutoDiTest.Storage::class, AutoDiTest.Local::class)
        val memory = scope.getInstance(AutoDiTest.Storage::class, AutoDiTest.Memory::class)

        assertSame(local, scope.getInstance(AutoDiTest.Storage::class, AutoDiTest.Local::class))
        assertSame(memory, scope.getInstance(AutoDiTest.Storage::class, AutoDiTest.Memory::class))
        assertNotSame(local, memory)
        assertFailsWith<IllegalArgumentException> { scope.getInstance(AutoDiTest.Storage::class) }
    }

    @Test
    fun `직접 저장한 기본 스코프의 인스턴스도 공유한다`() {
        val container = DiContainer()
        val instance = Dependency()
        container.saveInstance(Dependency::class, instance)

        assertSame(instance, container.openScope(screen, "cart").getInstance(Dependency::class))
    }

    @Test
    fun `생성 실패 후에는 다시 생성할 수 있다`() {
        val container = DiContainer()
        var attempts = 0
        container.register(Dependency::class, scope = screen) {
            attempts++
            check(attempts > 1)
            Dependency()
        }
        val scope = container.openScope(screen, "cart")

        assertFailsWith<IllegalStateException> { scope.getInstance(Dependency::class) }
        val instance = scope.getInstance(Dependency::class)
        assertSame(instance, scope.getInstance(Dependency::class))
        assertEquals(2, attempts)
    }

    @Test
    fun `반복해서 열고 닫아도 스코프가 누적되지 않는다`() {
        val container = DiContainer()
        container.register(Dependency::class, scope = screen) { Dependency() }
        val scopes = container.readPrivateField("scopes") as Map<*, *>

        repeat(100) { index ->
            val scope = container.openScope(screen, "cart-$index")
            scope.getInstance(Dependency::class)
            scope.close()

            assertFailsWith<IllegalStateException> { scope.getInstance(Dependency::class) }
            assertTrue(scopes.isEmpty())
        }
    }

    @Test
    fun `개별 스코프를 모두 닫아도 컨테이너와 기본 스코프는 유지한다`() {
        val container = DiContainer()
        container.register(Dependency::class) { Dependency() }
        container.register(ScreenDependency::class, scope = screen) { ScreenDependency() }
        val first = container.openScope(screen, "cart-1")
        val second = container.openScope(screen, "cart-2")
        val shared = container.getInstance(Dependency::class)
        val screenInstance = first.getInstance(ScreenDependency::class)
        second.getInstance(ScreenDependency::class)

        first.close()
        first.close()
        second.close()

        assertSame(shared, container.getInstance(Dependency::class))
        assertFailsWith<IllegalStateException> { first.getInstance(ScreenDependency::class) }
        assertFailsWith<IllegalStateException> { second.getInstance(ScreenDependency::class) }
        val reopened = container.openScope(screen, "cart-1")
        assertNotSame(screenInstance, reopened.getInstance(ScreenDependency::class))
        assertSame(shared, reopened.getInstance(Dependency::class))
    }

    @Test
    fun `생성 도중 종료된 스코프에 인스턴스를 다시 저장하지 않는다`() {
        val container = DiContainer()
        val scope = container.openScope(screen, "cart")
        container.register(Dependency::class, scope = screen) {
            scope.close()
            Dependency()
        }

        assertFailsWith<IllegalStateException> { scope.getInstance(Dependency::class) }
        assertFailsWith<IllegalStateException> { scope.getInstance(Dependency::class) }
        val reopened = container.openScope(screen, "cart")
        assertNotSame(scope, reopened)
        assertSame(reopened.getInstance(Dependency::class), reopened.getInstance(Dependency::class))
    }

    private fun Any.readPrivateField(name: String): Any? = javaClass.getDeclaredField(name).apply { isAccessible = true }.get(this)

    class Dependency

    class ScreenDependency
}
