package smile.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QualifierTest {
    @Test
    fun `올바른 qualifier를 가진 객체를 찾을 수 있다`() {
        val smileDi =
            SmileDi(
                module =
                    object {
                        @Provides
                        @Real
                        fun provideRealDependency(): Provider = RealProvider()
                    },
                repository = FakeDependencyRepository(),
            )
        val consumer = smileDi.resolveDependencies(RealConsumer::class)
        assertThat(consumer.provider).isInstanceOf(RealProvider::class.java)
    }
}

interface Provider

@Qualifier
annotation class Real

class RealProvider : Provider

class RealConsumer(
    @Real val provider: Provider,
)
