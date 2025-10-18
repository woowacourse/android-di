package woowacourse.shopping.core.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.core.di.DependencyContainer.instance
import woowacourse.shopping.core.di.DependencyContainer.register
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class ConstructorInjectionTest {
    @Before
    fun resetDependencyContainer() {
        @Suppress("UNCHECKED_CAST")
        val dependencies =
            DependencyContainer::class
                .memberProperties
                .first { it.name == "dependencies" }
                .apply { isAccessible = true }
                .getter
                .call() as MutableMap<*, *>

        dependencies.clear()
    }

    @Test
    fun `클래스 타입은 등록 없이 자동 생성된다`() {
        // given
        val service = instance(ServiceDependsOnConcrete::class)

        // then
        assertThat(service.repository).isNotNull()
    }

    @Test
    fun `인터페이스 타입은 등록되지 않으면 생성될 수 없다`() {
        assertThrows(IllegalStateException::class.java) {
            instance(ServiceDependsOnAbstract::class)
        }
    }

    @Test
    fun `인터페이스 타입이 등록되어 있다면 주입된다`() {
        // given
        register(Repository::class) { DefaultRepository() }
        val service = instance(ServiceDependsOnAbstract::class)

        // then
        assertThat(service.repository).isNotNull()
    }

    @Test
    fun `생성자 파라미터에 기본값이 있더라도 의존성이 등록되어있지 않으면 생성되지 않는다`() {
        assertThrows(IllegalStateException::class.java) {
            instance(ServiceWithDefaultParameter::class)
        }
    }

    @Test
    fun `생성자 파라미터에 기본값이 있더라도 의존성이 등록되어 있으면 등록된 의존성을 사용한다`() {
        // given
        register(Repository::class) { DefaultRepository() }
        val service = instance(ServiceWithDefaultParameter::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    interface Repository

    class DefaultRepository : Repository

    class DefaultParameterRepository : Repository

    class ServiceDependsOnConcrete
        @Inject
        constructor(
            val repository: DefaultRepository,
        )

    class ServiceDependsOnAbstract
        @Inject
        constructor(
            val repository: Repository,
        )

    class ServiceWithDefaultParameter
        @Inject
        constructor(
            val repository: Repository = DefaultParameterRepository(),
        )
}
