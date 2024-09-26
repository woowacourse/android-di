package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.Olive
import olive.di.fixture.Sandwich
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class RecursiveDITest {
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Before
    fun setup() {
        DIContainer.injectApplication(applicationInstance, applicationType)
    }

    @Test
    fun `Sandwich 객체가 Olive를 필요로 하는 경우 Olive를 생성 후 Sandwich를 생성한다`() {
        // when
        val actual = DIContainer.instance(Sandwich::class)

        // then
        assertThat(actual).isInstanceOf(Sandwich::class.java)
        assertThat(DIContainer.instance(Olive::class)).isInstanceOf(Olive::class.java)
    }
}
