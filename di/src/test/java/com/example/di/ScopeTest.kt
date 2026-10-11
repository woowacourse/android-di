package com.example.di

import com.example.di.annotations.Qualifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ScopeTest {
    @Test
    fun `앱 스코프는 하위 소유자들이 공유하고 종료하면 참조를 제거한다`() {
        val app = Scope("app")
        val di = SamDi(emptyMap(), emptyMap(), mapOf(Repository::class to "app"), app)
        val screen = Scope("screen", app)
        val viewModel = Scope("viewModel", app)
        val repository = di.resolve(Repository::class)

        assertSame(repository, di.resolve(Repository::class, scope = screen))
        assertSame(repository, di.resolve(Repository::class, scope = viewModel))
        assertEquals(1, app.instanceCount)
        screen.close()
        viewModel.close()
        assertSame(repository, di.resolve(Repository::class))
        app.close()
        assertTrue(app.isClosed)
        assertEquals(0, app.instanceCount)
        assertThrows(IllegalStateException::class.java) { di.resolve(Repository::class) }

        val nextApp = Scope("app")
        assertNotSame(repository, di.resolve(Repository::class, scope = nextApp))
        nextApp.close()
    }

    @Test
    fun `ViewModel 스코프는 같은 소유자만 재사용하고 종료 후 새 소유자는 새 객체를 받는다`() {
        verifyIndependentOwners("viewModel")
    }

    @Test
    fun `화면 스코프는 같은 목적지만 재사용하고 종료 후 새 목적지는 새 객체를 받는다`() {
        verifyIndependentOwners("screen")
    }

    private fun verifyIndependentOwners(kind: String) {
        val di = SamDi(emptyMap(), emptyMap(), mapOf(Repository::class to kind))
        val first = Scope(kind)
        val second = Scope(kind)
        val firstRepository = di.resolve(Repository::class, scope = first)
        val secondRepository = di.resolve(Repository::class, scope = second)

        assertSame(firstRepository, di.resolve(Repository::class, scope = first))
        assertNotSame(firstRepository, secondRepository)
        first.close()
        assertTrue(first.isClosed)
        assertEquals(0, first.instanceCount)
        assertFalse(second.isClosed)
        assertSame(secondRepository, di.resolve(Repository::class, scope = second))
        assertThrows(IllegalStateException::class.java) { di.resolve(Repository::class, scope = first) }
        val next = Scope(kind)
        assertNotSame(firstRepository, di.resolve(Repository::class, scope = next))
        second.close()
        next.close()
        assertEquals(0, second.instanceCount)
        assertEquals(0, next.instanceCount)
    }

    @Test
    fun `스코프가 없는 의존성은 매번 생성하고 저장하지 않는다`() {
        val scope = Scope("screen")
        val di = SamDi(emptyMap(), emptyMap())
        assertNotSame(di.resolve(Repository::class, scope = scope), di.resolve(Repository::class, scope = scope))
        assertEquals(0, scope.instanceCount)
        scope.close()
    }

    @Test
    fun `외부에서 새로운 스코프 종류를 등록해도 생성 재사용 종료가 작동한다`() {
        verifyIndependentOwners("session")
    }

    @Test
    fun `같은 스코프에서도 Qualifier별 인스턴스가 섞이지 않는다`() {
        val scope = Scope("app")
        val di = SamDi(
            emptyMap(),
            mapOf(
                (Service::class to First::class) to FirstService::class,
                (Service::class to Second::class) to SecondService::class,
            ),
            mapOf(Service::class to "app"),
            scope,
        )
        val first = di.resolve(Service::class, First())
        val second = di.resolve(Service::class, Second())
        assertSame(first, di.resolve(Service::class, First()))
        assertSame(second, di.resolve(Service::class, Second()))
        assertNotSame(first, second)
        assertEquals(2, scope.instanceCount)
        scope.close()
        assertEquals(0, scope.instanceCount)
    }

    @Test
    fun `provider는 처음 요청할 때만 호출하고 종료 후 새 스코프에서 다시 호출한다`() {
        var creations = 0
        val di = SamDi(
            mapOf(
                Repository::class to {
                    creations++
                    Repository()
                },
            ),
            emptyMap(),
            mapOf(Repository::class to "screen"),
        )
        val first = Scope("screen")
        assertEquals(0, creations)
        val repository = di.resolve(Repository::class, scope = first)
        assertSame(repository, di.resolve(Repository::class, scope = first))
        assertEquals(1, creations)
        first.close()
        val next = Scope("screen")
        assertNotSame(repository, di.resolve(Repository::class, scope = next))
        assertEquals(2, creations)
        next.close()
    }

    @Test
    fun `생성과 종료를 반복해도 하위 인스턴스가 앱 스코프에 누적되지 않는다`() {
        val app = Scope("app")
        val di = SamDi(emptyMap(), emptyMap(), mapOf(Repository::class to "screen"), app)
        repeat(100) {
            val screen = Scope("screen", app)
            di.resolve(Repository::class, scope = screen)
            assertEquals(1, screen.instanceCount)
            screen.close()
            assertEquals(0, screen.instanceCount)
            assertEquals(0, app.instanceCount)
        }
        app.close()
    }

    @Test
    fun `앱 스코프 객체가 더 짧은 화면 스코프 의존성을 소유할 수 없다`() {
        val app = Scope("app")
        val screen = Scope("screen", app)
        val di = SamDi(
            emptyMap(), emptyMap(),
            mapOf(AppConsumer::class to "app", Repository::class to "screen"), app,
        )
        assertThrows(IllegalStateException::class.java) { di.resolve(AppConsumer::class, scope = screen) }
        assertEquals(0, app.instanceCount)
        assertEquals(0, screen.instanceCount)
        screen.close()
        app.close()
    }
}

class Repository
class AppConsumer(val repository: Repository)
interface Service
class FirstService : Service
class SecondService : Service

@Qualifier
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class First

@Qualifier
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Second
