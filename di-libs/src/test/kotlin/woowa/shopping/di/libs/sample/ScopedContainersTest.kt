package woowa.shopping.di.libs.sample

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.startDI
import woowa.shopping.di.libs.inject.inject
import woowa.shopping.di.libs.qualify.named
import woowa.shopping.di.libs.scope.startScope

class ScopedContainersTest {
    class Service

    interface Repository

    class RepositoryImpl(val service: Service) : Repository

    @AfterEach
    fun tearDown() {
        Containers.clearContainersForTest()
    }

    @Test
    fun `Scope 동안 싱글톤 객체인지 테스트`() {
        // given
        startDI {
            container {
                scope(named("scope")) {
                    scoped<Service> { Service() }
                    scoped<Repository> { RepositoryImpl(get()) }
                }
            }
        }
        // when
        val scope = startScope(named("scope"))
        val service1 = scope.get<Service>()
        val service2 = scope.get<Service>()
        // then
        service1 shouldBe service2
    }

    @Test
    fun `서로 다른 Scope 에서는 다른 객체인지 테스트`() {
        // given
        startDI {
            container {
                scope(named("scope")) {
                    scoped<Service> { Service() }
                    scoped<Repository> { RepositoryImpl(get()) }
                }
                scope(named("scope2")) {
                    scoped<Service> { Service() }
                    scoped<Repository> { RepositoryImpl(get()) }
                }
            }
        }
        // when
        val scope = startScope(named("scope"))
        val scope2 = startScope(named("scope2"))
        val service1 = scope.get<Service>()
        val service2 = scope2.get<Service>()
        // then
        service1 shouldNotBe service2
    }

    @Test
    fun `Scope 가 종료되면 객체를 불러올 수 없다`() {
        // given
        startDI {
            container {
                scope(named("scope")) {
                    scoped<Service> { Service() }
                    scoped<Repository> { RepositoryImpl(get()) }
                }
            }
        }
        // when
        val scope = startScope(named("scope"))
        scope.cancel()
        // then
        shouldThrow<IllegalArgumentException> {
            scope.get<Service>()
        }
    }

    @Test
    fun `Scope 에 등록된 객체가 없으면 SingleTone 객체를 가져와 객체에 넣어준다`() {
        // given
        startDI {
            container {
                single { Service() }
                scope(named("scope")) {
                    scoped<Repository> { RepositoryImpl(get()) }
                }
            }
        }
        // when
        val service by inject<Service>()
        val scope = startScope(named("scope"))
        val repository = scope.get<Repository>() as RepositoryImpl
        // then
        repository.service shouldBe service
    }
}