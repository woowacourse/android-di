package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class QualifierTest {
    private val injector = Injector()

    @Test
    fun `필드의 Qualifier로 같은 인터페이스의 구현체를 선택한다`() {
        registerServices()

        val target = injector.create(FieldTarget::class)

        assertThat(target.remote).isInstanceOf(RemoteService::class.java)
        assertThat(target.local).isInstanceOf(LocalService::class.java)
    }

    @Test
    fun `생성자 파라미터의 Qualifier로 구현체를 선택한다`() {
        registerServices()

        val target = injector.create(ConstructorTarget::class)

        assertThat(target.remote).isInstanceOf(RemoteService::class.java)
        assertThat(target.local).isInstanceOf(LocalService::class.java)
    }

    @Test
    fun `Qualifier마다 싱글톤을 별도로 보관한다`() {
        var remoteCount = 0
        var localCount = 0
        injector.registerSingleton(Service::class, Remote::class) {
            remoteCount++
            RemoteService()
        }
        injector.registerSingleton(Service::class, Local::class) {
            localCount++
            LocalService()
        }

        val remote = injector.create(Service::class, Remote::class)
        val local = injector.create(Service::class, Local::class)

        assertThat(injector.create(Service::class, Remote::class)).isSameAs(remote)
        assertThat(injector.create(Service::class, Local::class)).isSameAs(local)
        assertThat(remote).isNotSameAs(local)
        assertThat(remoteCount).isEqualTo(1)
        assertThat(localCount).isEqualTo(1)
    }

    @Test
    fun `두 구현체가 등록되어 있는데 Qualifier가 없으면 후보를 포함한 오류를 낸다`() {
        registerServices()

        assertThatThrownBy { injector.create(Service::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier가 필요합니다: Service")
            .hasMessageContaining("Service[@Remote]")
            .hasMessageContaining("Service[@Local]")
    }

    @Test
    fun `필드에 Qualifier가 없을 때도 임의의 구현체를 선택하지 않는다`() {
        registerServices()

        assertThatThrownBy { injector.create(UnqualifiedFieldTarget::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier가 필요합니다: Service")
    }

    @Test
    fun `생성자에 Qualifier가 없을 때도 임의의 구현체를 선택하지 않는다`() {
        registerServices()

        assertThatThrownBy { injector.create(UnqualifiedConstructorTarget::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier가 필요합니다: Service")
    }

    @Test
    fun `Qualifier 없는 객체가 캐시에 있어도 여러 등록이 생기면 오류를 낸다`() {
        injector.registerSingleton(Service::class) { LocalService() }
        injector.create(Service::class)
        injector.registerSingleton(Service::class, Remote::class) { RemoteService() }

        assertThatThrownBy { injector.create(Service::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier가 필요합니다: Service")
    }

    @Test
    fun `요청한 Qualifier가 없으면 다른 구현체로 대체하지 않는다`() {
        injector.registerSingleton(Service::class) { LocalService() }

        assertThatThrownBy { injector.create(Service::class, Remote::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("등록되지 않은 의존성입니다: Service[@Remote]")
    }

    @Test
    fun `Qualifier로 등록한 구현체가 하나여도 해당 Qualifier를 명시해야 한다`() {
        injector.registerSingleton(Service::class, Remote::class) { RemoteService() }

        assertThatThrownBy { injector.create(Service::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier를 지정해야 합니다")
    }

    @Test
    fun `같은 타입과 Qualifier를 중복 등록할 수 없다`() {
        registerServices()

        assertThatThrownBy {
            injector.registerSingleton(Service::class, Remote::class) { LocalService() }
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("이미 등록되거나 생성된 의존성입니다: Service[@Remote]")
    }

    @Test
    fun `Qualifier로 표시하지 않은 애노테이션은 등록할 수 없다`() {
        assertThatThrownBy {
            injector.registerSingleton(Service::class, NotQualifier::class) { LocalService() }
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("@Qualifier가 붙은 애노테이션")
    }

    @Test
    fun `런타임에 보이지 않는 Qualifier는 등록할 수 없다`() {
        assertThatThrownBy {
            injector.registerSingleton(Service::class, BinaryQualifier::class) { LocalService() }
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("RUNTIME 보존 정책")
    }

    @Test
    fun `애노테이션 값으로 구분하려는 Qualifier는 명확히 거부한다`() {
        assertThatThrownBy {
            injector.registerSingleton(Service::class, Named::class) { LocalService() }
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("값 없이 애노테이션 타입으로 구분")
    }

    @Test
    fun `필드에 두 Qualifier가 붙으면 주입 대상을 포함한 오류를 낸다`() {
        registerServices()

        assertThatThrownBy { injector.create(MultipleFieldTarget::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier를 하나만 지정")
            .hasMessageContaining("MultipleFieldTarget.service")
    }

    @Test
    fun `생성자 파라미터에 두 Qualifier가 붙으면 오류를 낸다`() {
        registerServices()

        assertThatThrownBy { injector.create(MultipleConstructorTarget::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier를 하나만 지정")
            .hasMessageContaining("MultipleConstructorTarget.service")
    }

    @Test
    fun `Qualifier만 붙고 Inject가 없는 필드는 주입하지 않는다`() {
        registerServices()

        assertThat(injector.create(QualifierOnlyTarget::class).isInitialized()).isFalse()
    }

    @Test
    fun `같은 타입이라도 Qualifier가 다른 의존성은 재귀적으로 해결한다`() {
        injector.registerSingleton(Service::class, Local::class) { LocalService() }
        injector.registerSingleton(Service::class, Remote::class) { create(DelegatingService::class) }

        val remote = injector.create(Service::class, Remote::class) as DelegatingService

        assertThat(remote.delegate).isSameAs(injector.create(Service::class, Local::class))
    }

    @Test
    fun `순환 의존성 경로에 Qualifier도 표시한다`() {
        injector.registerSingleton(Service::class, Remote::class) { create(Service::class, Local::class) }
        injector.registerSingleton(Service::class, Local::class) { create(Service::class, Remote::class) }

        assertThatThrownBy { injector.create(Service::class, Remote::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Service[@Remote] -> Service[@Local] -> Service[@Remote]")
    }

    private fun registerServices() {
        injector.registerSingleton(Service::class, Remote::class) { RemoteService() }
        injector.registerSingleton(Service::class, Local::class) { LocalService() }
    }

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Remote

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Local

    annotation class NotQualifier

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BinaryQualifier

    @Qualifier
    annotation class Named(
        val value: String,
    )

    interface Service

    class RemoteService : Service

    class LocalService : Service

    class FieldTarget {
        @Inject
        @Remote
        lateinit var remote: Service

        @Inject
        @Local
        lateinit var local: Service
    }

    class ConstructorTarget(
        @Remote val remote: Service,
        @Local val local: Service,
    )

    class UnqualifiedFieldTarget {
        @Inject
        lateinit var service: Service
    }

    class UnqualifiedConstructorTarget(
        val service: Service,
    )

    class MultipleFieldTarget {
        @Inject
        @Remote
        @Local
        lateinit var service: Service
    }

    class MultipleConstructorTarget(
        @Remote @Local val service: Service,
    )

    class QualifierOnlyTarget {
        @Remote
        lateinit var service: Service

        fun isInitialized(): Boolean = this::service.isInitialized
    }

    class DelegatingService(
        @Local val delegate: Service,
    ) : Service
}
