package woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class DIContainerTest {
    private val container = DIContainer()

    @Test
    fun `생성자 의존성을 재귀적으로 주입한다`() {
        val consumer = container.get(Consumer::class)

        assertThat(consumer.service).isInstanceOf(Service::class.java)
    }

    @Test
    fun `get은 같은 타입의 인스턴스를 재사용한다`() {
        val first = container.get(Service::class)
        val second = container.get(Service::class)

        assertThat(second).isSameInstanceAs(first)
    }

    @Test
    fun `create는 같은 타입을 새 인스턴스로 생성한다`() {
        val first = container.create(Consumer::class)
        val second = container.create(Consumer::class)

        assertThat(second).isNotSameInstanceAs(first)
        assertThat(second.service).isSameInstanceAs(first.service)
    }

    @Test
    fun `Inject 애노테이션이 붙은 필드에만 의존성을 주입한다`() {
        val target = container.create(FieldInjectionTarget::class)

        assertThat(target.injected).isSameInstanceAs(container.get(Service::class))
        assertThrows(UninitializedPropertyAccessException::class.java) { target.notInjected }
    }

    @Test
    fun `주입 지점의 Qualifier에 해당하는 구현체를 주입한다`() {
        registerQualifiedServices()

        val target = container.create(QualifiedInjectionTarget::class)
        val consumer = container.create(QualifiedConstructorConsumer::class)

        assertThat(target.service).isInstanceOf(BlueService::class.java)
        assertThat(consumer.service).isInstanceOf(RedService::class.java)
    }

    @Test
    fun `Qualifier로 같은 타입의 구현체를 구분해 조회한다`() {
        registerQualifiedServices()

        val blue = container.get(ServiceContract::class, Blue::class)
        val red = container.get(ServiceContract::class, Red::class)

        assertThat(blue).isInstanceOf(BlueService::class.java)
        assertThat(red).isInstanceOf(RedService::class.java)
    }

    @Test
    fun `구현체가 여러 개일 때 Qualifier가 없으면 명확한 예외를 발생시킨다`() {
        registerQualifiedServices()

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                container.get(ServiceContract::class)
            }

        assertThat(exception).hasMessageThat().contains("ServiceContract 의존성이 모호합니다")
        assertThat(exception).hasMessageThat().contains("Blue")
        assertThat(exception).hasMessageThat().contains("Red")
    }

    @Test
    fun `순환 의존성 경로를 오류로 표시한다`() {
        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                container.get(FirstDependency::class)
            }

        assertThat(exception).hasMessageThat().contains("FirstDependency → SecondDependency → FirstDependency")
    }

    private fun registerQualifiedServices() {
        container.bind(ServiceContract::class, BlueService::class, Blue::class)
        container.bind(ServiceContract::class, RedService::class, Red::class)
    }

    class Service

    class Consumer(
        val service: Service,
    )

    class FieldInjectionTarget {
        @Inject
        lateinit var injected: Service

        lateinit var notInjected: Service
    }

    interface ServiceContract

    class BlueService : ServiceContract

    class RedService : ServiceContract

    class QualifiedInjectionTarget {
        @Inject
        @Blue
        lateinit var service: ServiceContract
    }

    class QualifiedConstructorConsumer(
        @Red val service: ServiceContract,
    )

    class FirstDependency(
        val second: SecondDependency,
    )

    class SecondDependency(
        val first: FirstDependency,
    )

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Blue

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Red
}
