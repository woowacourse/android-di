package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.Olive
import olive.di.fixture.Sandwich
import org.junit.Test
import kotlin.reflect.KClass

class RecursiveDITest {
    private lateinit var diContainer: DIContainer
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Test
    fun `Sandwich 객체가 Olive를 필요로 하는 경우 Olive를 생성 후 Sandwich를 생성한다`() {
        // given
        diContainer = DIContainer()

        // when
        val actual = diContainer.instance(Sandwich::class)

        // then
        assertThat(actual).isInstanceOf(Sandwich::class.java)
        assertThat(diContainer.instance(Olive::class)).isInstanceOf(Olive::class.java)
    }

    private fun DIContainer(modules: List<KClass<out DIModule>> = emptyList()): DIContainer {
        return DIContainer(
            applicationInstance = applicationInstance,
            applicationType = applicationType,
            diModules = modules,
        )
    }
}
