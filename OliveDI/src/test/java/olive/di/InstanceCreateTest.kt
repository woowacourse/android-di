package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.NotSingletonFoo
import olive.di.fixture.SingletonFoo
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class InstanceCreateTest {
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Before
    fun setup() {
        DIContainer.injectApplication(applicationInstance, applicationType)
    }

    @Test
    fun `instance()로 새로운 인스턴스를 생성해 반환한다`() {
        // when
        val actual = DIContainer.instance(NotSingletonFoo::class)

        // then
        assertThat(actual).isInstanceOf(NotSingletonFoo::class.java)
    }

    @Test
    fun `singletonInstance()의 인스턴스가 없는 경우 새로운 인스턴스를 생성해 반환한다`() {
        // when
        val actual = DIContainer.instance(SingletonFoo::class)

        // then
        assertThat(actual).isInstanceOf(SingletonFoo::class.java)
    }

    @Test
    fun `singletonInstance()로 기존에 있는 인스턴스를 반환한다`() {
        // given
        val expected = DIContainer.instance(SingletonFoo::class)

        // when
        val actual = DIContainer.instance(SingletonFoo::class)

        // then
        assertThat(actual).isSameInstanceAs(expected)
    }
}
