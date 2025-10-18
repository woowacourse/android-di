package woowacourse.shopping.core.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.core.di.NewDependencyContainer.instance
import woowacourse.shopping.core.di.NewDependencyContainer.register
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class FieldInjectionTest {
    @Before
    fun resetDependencyContainer() {
        @Suppress("UNCHECKED_CAST")
        val dependencies =
            NewDependencyContainer::class
                .memberProperties
                .first { it.name == "dependencies" }
                .apply { isAccessible = true }
                .getter
                .call() as MutableMap<*, *>

        dependencies.clear()
    }

    @Test
    fun `클래스 타입은 등록 없이 자동 주입된다`() {
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
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `변경 불가능한 필드는 주입될 수 없다`() {
        // given
        register(Repository::class) { DefaultRepository() }

        // then
        assertThrows(IllegalStateException::class.java) {
            instance(ServiceHasImmutableField::class)
        }
    }

    @Test
    fun `값이 이미 할당되어 있어도 주입된다`() {
        // given
        register(Repository::class) { DefaultRepository() }
        val service = instance(ServiceHasPreAssignedField::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    interface Repository

    class DefaultRepository : Repository

    class InitialRepository : Repository

    class ServiceDependsOnConcrete {
        @Inject
        lateinit var repository: DefaultRepository
    }

    class ServiceDependsOnAbstract {
        @Inject
        lateinit var repository: Repository
    }

    class ServiceHasImmutableField {
        @Inject
        val repository: Repository = InitialRepository()
    }

    class ServiceHasPreAssignedField {
        @Inject
        var repository: Repository = InitialRepository()
    }
}
