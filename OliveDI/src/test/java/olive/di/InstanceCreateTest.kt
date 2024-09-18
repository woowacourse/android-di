package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.NotSingletonFoo
import olive.di.fixture.SingletonFoo
import org.junit.Test
import kotlin.reflect.KClass

class InstanceCreateTest {
    private lateinit var diContainer: DIContainer
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Test
    fun `instance()로 새로운 인스턴스를 생성해 반환한다`() {
        // given
        diContainer = DIContainer()

        // when
        val actual = diContainer.instance(NotSingletonFoo::class)

        // then
        assertThat(actual).isInstanceOf(NotSingletonFoo::class.java)
    }

    @Test
    fun `singletonInstance()의 인스턴스가 없는 경우 새로운 인스턴스를 생성해 반환한다`() {
        // given
        diContainer = DIContainer()

        // when
        val actual = diContainer.instance(SingletonFoo::class)

        // then
        assertThat(actual).isInstanceOf(SingletonFoo::class.java)
    }

    @Test
    fun `singletonInstance()로 기존에 있는 인스턴스를 반환한다`() {
        // given
        diContainer = DIContainer()
        val expected = diContainer.instance(SingletonFoo::class)

        // when
        val actual = diContainer.instance(SingletonFoo::class)

        // then
        assertThat(actual).isSameInstanceAs(expected)
    }

    private fun DIContainer(modules: List<KClass<out DIModule>> = emptyList()): DIContainer {
        return DIContainer(
            applicationInstance = applicationInstance,
            applicationType = applicationType,
            diModules = modules,
        )
    }
}
