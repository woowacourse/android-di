package com.cksckckcks.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame

class ScopedAutoDiTest {
    private val screen = ScopeType("screen")

    @Test
    fun `생성자와 필드에 같은 스코프의 등록된 의존성을 주입한다`() {
        val container = DiContainer()
        var createdCount = 0
        container.register(Dependency::class, scope = screen) {
            createdCount++
            Dependency()
        }
        val scope = container.openScope(screen, "cart")
        val autoDi = AutoDi(scope)

        val constructorTarget = autoDi.createInstance(ConstructorTarget::class)
        val propertyTarget = autoDi.createInstance(PropertyTarget::class)

        assertSame(scope.getInstance(Dependency::class), constructorTarget.dependency)
        assertSame(constructorTarget.dependency, propertyTarget.dependency)
        assertEquals(1, createdCount)
    }

    @Test
    fun `자동 생성한 의존성을 현재 스코프에 저장하고 재사용한다`() {
        val container = DiContainer()
        val scope = container.openScope(screen, "cart")
        val autoDi = AutoDi(scope)

        val constructorTarget = autoDi.createInstance(ConstructorTarget::class)
        val propertyTarget = autoDi.createInstance(PropertyTarget::class)

        assertSame(constructorTarget.dependency, propertyTarget.dependency)
        assertSame(constructorTarget.dependency, scope.getInstance(Dependency::class))
        assertNull(container.getInstance(Dependency::class))
    }

    @Test
    fun `중첩된 의존성도 같은 스코프에서 재사용한다`() {
        val container = DiContainer()
        val scope = container.openScope(screen, "cart")
        val autoDi = AutoDi(scope)

        val root = autoDi.createInstance(AutoDiTest.SharedDependencyRoot::class)
        val second = autoDi.createInstance(AutoDiTest.SharedDependencyRoot::class)

        assertSame(root.left.shared, root.right.shared)
        assertSame(root.left, second.left)
        assertSame(root.right, second.right)
        assertNull(container.getInstance(AutoDiTest.SharedDependency::class))
    }

    @Test
    fun `다른 스코프에는 별도의 의존성을 주입한다`() {
        val container = DiContainer()
        container.register(Dependency::class, scope = screen) { Dependency() }
        val first = AutoDi(container.openScope(screen, "cart-1")).createInstance(ConstructorTarget::class)
        val second = AutoDi(container.openScope(screen, "cart-2")).createInstance(ConstructorTarget::class)

        assertNotSame(first.dependency, second.dependency)
    }

    @Test
    fun `등록하지 않은 의존성도 다른 스코프와 공유하지 않는다`() {
        val container = DiContainer()
        val first = AutoDi(container.openScope(screen, "cart-1")).createInstance(ConstructorTarget::class)
        val second = AutoDi(container.openScope(screen, "cart-2")).createInstance(ConstructorTarget::class)

        assertNotSame(first.dependency, second.dependency)
        assertNull(container.getInstance(Dependency::class))
    }

    @Test
    fun `기본 스코프의 의존성은 서로 다른 스코프에도 공유한다`() {
        val container = DiContainer()
        container.register(Dependency::class) { Dependency() }
        val firstScope = container.openScope(screen, "cart-1")
        val secondScope = container.openScope(screen, "cart-2")
        val first = AutoDi(firstScope).createInstance(ConstructorTarget::class)
        val second = AutoDi(secondScope).createInstance(PropertyTarget::class)

        firstScope.close()

        assertSame(first.dependency, second.dependency)
        assertSame(first.dependency, container.getInstance(Dependency::class))
        assertSame(first.dependency, secondScope.getInstance(Dependency::class))
    }

    @Test
    fun `생성자와 필드의 Qualifier를 해석하여 각각의 스코프에서 주입한다`() {
        val container = DiContainer()
        container.register(AutoDiTest.Storage::class, AutoDiTest.Local::class, screen) { AutoDiTest.LocalStorage() }
        container.register(AutoDiTest.Storage::class, AutoDiTest.Memory::class) { AutoDiTest.MemoryStorage() }
        val scope = container.openScope(screen, "cart")
        val autoDi = AutoDi(scope)

        val constructorTarget = autoDi.createInstance(AutoDiTest.ConstructorTarget::class)
        val propertyTarget = autoDi.createInstance(AutoDiTest.PropertyTarget::class)

        assertIs<AutoDiTest.LocalStorage>(constructorTarget.storage)
        assertIs<AutoDiTest.MemoryStorage>(propertyTarget.storage)
        assertSame(scope.getInstance(AutoDiTest.Storage::class, AutoDiTest.Local::class), constructorTarget.storage)
        assertSame(container.getInstance(AutoDiTest.Storage::class, AutoDiTest.Memory::class), propertyTarget.storage)
        assertFailsWith<IllegalArgumentException> { autoDi.createInstance(AutoDiTest.UnqualifiedTarget::class) }
    }

    @Test
    fun `필요한 스코프가 없는 주입 요청은 오류를 낸다`() {
        val container = DiContainer()
        container.register(Dependency::class, scope = screen) { Dependency() }

        val error =
            assertFailsWith<IllegalArgumentException> {
                AutoDi(container.applicationScope).createInstance(ConstructorTarget::class)
            }

        assertContains(error.message.orEmpty(), "screen")
    }

    @Test
    fun `닫힌 스코프로는 주입할 수 없고 다시 연 스코프에서는 새로 생성한다`() {
        val container = DiContainer()
        val scope = container.openScope(screen, "cart")
        val autoDi = AutoDi(scope)
        val first = autoDi.createInstance(ConstructorTarget::class)

        scope.close()

        assertFailsWith<IllegalStateException> { autoDi.createInstance(ConstructorTarget::class) }
        assertFailsWith<IllegalStateException> { autoDi.createInstance(PropertyTarget::class) }
        val reopened = container.openScope(screen, "cart")
        val second = AutoDi(reopened).createInstance(ConstructorTarget::class)
        assertNotSame(first.dependency, second.dependency)
    }

    @Test
    fun `스코프에서도 생성자와 필드의 순환 의존성을 검증한다`() {
        val container = DiContainer()
        val scope = container.openScope(screen, "cart")
        val autoDi = AutoDi(scope)

        val constructorError = assertFailsWith<IllegalArgumentException> { autoDi.createInstance(AutoDiTest.DirectCycleA::class) }
        val propertyError = assertFailsWith<IllegalArgumentException> { autoDi.createInstance(AutoDiTest.FieldCycleA::class) }

        assertContains(constructorError.message.orEmpty(), "DirectCycleA → DirectCycleB → DirectCycleA")
        assertContains(propertyError.message.orEmpty(), "FieldCycleA → FieldCycleB → FieldCycleA")
        assertNull(scope.getInstance(AutoDiTest.DirectCycleA::class))
        assertNull(scope.getInstance(AutoDiTest.FieldCycleA::class))
    }

    class Dependency

    class ConstructorTarget(
        val dependency: Dependency,
    )

    class PropertyTarget {
        @InjectProperty
        lateinit var dependency: Dependency
    }
}
