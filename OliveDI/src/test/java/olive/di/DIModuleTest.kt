package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.AbstractTestModule
import olive.di.fixture.Child
import olive.di.fixture.Parent
import olive.di.fixture.TestModule
import org.junit.Test
import kotlin.reflect.KClass

class DIModuleTest {
    private lateinit var diContainer: DIContainer
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Test
    fun `모듈이 abstract class인 경우 함수의 리턴 타입의 인스턴스를 저장한다`() {
        // given
        diContainer = DIContainer(modules = listOf(AbstractTestModule::class))

        // when
        val actual = diContainer.instance(Parent::class)

        // then
        assertThat(actual).isInstanceOf(Child::class.java)
    }

    @Test
    fun `모듈이 일반 class인 경우 함수의 실행 결과 인스턴스를 저장한다`() {
        // given
        diContainer = DIContainer(modules = listOf(TestModule::class))

        // when
        val actual = diContainer.instance(Child::class)

        // then
        assertThat(actual).isInstanceOf(Child::class.java)
    }

    private fun DIContainer(modules: List<KClass<out DIModule>> = emptyList()): DIContainer {
        return DIContainer(
            applicationInstance = applicationInstance,
            applicationType = applicationType,
            diModules = modules,
        )
    }
}