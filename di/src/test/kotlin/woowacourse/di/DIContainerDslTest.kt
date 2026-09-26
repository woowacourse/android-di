package woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DIContainerDslTest {
    @Test
    fun `DSL로 의존성을 등록하고 조회한다`() {
        val registeredService = Service()
        val container =
            diContainer {
                register(registeredService)
                bind<ServiceContract, ServiceImplementation>(TestQualifier::class)
            }

        val service = container.get<Service>()
        val implementation = container.get<ServiceContract>(TestQualifier::class)

        assertThat(service).isSameInstanceAs(registeredService)
        assertThat(implementation).isInstanceOf(ServiceImplementation::class.java)
    }

    class Service

    interface ServiceContract

    class ServiceImplementation : ServiceContract

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestQualifier
}
