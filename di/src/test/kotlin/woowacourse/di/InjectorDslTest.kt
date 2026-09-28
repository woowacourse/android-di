package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class InjectorDslTest {
    @Test
    fun `DSL로 등록한 객체는 처음 조회할 때 생성하고 이후에는 공유한다`() {
        var created = 0
        val container =
            injector {
                singleton<Dependency> {
                    created++
                    Dependency()
                }
                singleton<Consumer> { Consumer(get()) }
            }
        assertThat(created).isZero()

        val consumer = container.get<Consumer>()

        assertThat(consumer.dependency).isSameAs(container.get<Dependency>())
        assertThat(container.get<Consumer>()).isSameAs(consumer)
        assertThat(created).isEqualTo(1)
    }

    @Test
    fun `DSL에서도 Qualifier를 지정해 구현체를 선택한다`() {
        val container =
            injector {
                singleton<Service>(Remote::class) { RemoteService() }
                singleton<Service>(Local::class) { LocalService() }
            }

        assertThat(container.get<Service>(Remote::class)).isInstanceOf(RemoteService::class.java)
        assertThat(container.get<Service>(Local::class)).isInstanceOf(LocalService::class.java)
    }

    @Test
    fun `DSL에서도 모호한 의존성은 Qualifier 없이는 조회할 수 없다`() {
        val container =
            injector {
                singleton<Service>(Remote::class) { RemoteService() }
                singleton<Service>(Local::class) { LocalService() }
            }

        assertThatThrownBy { container.get<Service>() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier가 필요합니다: Service")
    }

    @Qualifier
    annotation class Remote

    @Qualifier
    annotation class Local

    class Dependency

    class Consumer(
        val dependency: Dependency,
    )

    interface Service

    class RemoteService : Service

    class LocalService : Service
}
