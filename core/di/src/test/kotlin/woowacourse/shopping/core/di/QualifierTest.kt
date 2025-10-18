package woowacourse.shopping.core.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.core.di.DependencyContainer.instance
import woowacourse.shopping.core.di.DependencyContainer.register
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class QualifierTest {
    @Before
    fun resetDependencyContainer() {
        val field =
            DependencyContainer::class
                .memberProperties
                .first { it.name == "dependencies" }
                .apply { isAccessible = true }

        @Suppress("UNCHECKED_CAST")
        val map = field.getter.call() as MutableMap<*, *>
        map.clear()
    }

    @Test
    fun `Qualifier 이름이 일치하는 의존성이 생성자 주입된다`() {
        // given
        register(Repository::class, "default") { DefaultRepository() }

        val service = instance(ServiceWithQualifiedConstructor::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `Qualifier 이름이 일치하는 의존성이 필드 주입된다`() {
        // given
        register(Repository::class, "default") { DefaultRepository() }

        val service = instance(ServiceWithQualifiedField::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `등록되지 않은 Qualifier를 사용하면 예외가 발생한다`() {
        // given
        register(Repository::class, "default") { DefaultRepository() }

        assertThrows(IllegalStateException::class.java) {
            instance(ServiceWithUnknownQualifier::class)
        }
    }

    interface Repository

    class DefaultRepository : Repository

    class ServiceWithQualifiedConstructor
        @Inject
        constructor(
            @Qualifier("default") val repository: Repository,
        )

    class ServiceWithQualifiedField {
        @Inject
        @Qualifier("default")
        lateinit var repository: Repository
    }

    class ServiceWithUnknownQualifier {
        @Inject
        @Qualifier("unknown")
        lateinit var repository: Repository
    }
}
