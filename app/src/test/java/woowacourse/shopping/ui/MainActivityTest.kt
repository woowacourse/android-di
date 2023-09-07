package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.SheathApplication
import com.ki960213.sheath.scanner.ClassScanner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

@RunWith(RobolectricTestRunner::class)
@Config(application = TestShoppingApplication::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}

class TestShoppingApplication : SheathApplication(FakeScanner())

class FakeScanner : ClassScanner {
    override fun findAll(targetClass: Class<*>): List<Class<*>> {
        return listOf(DefaultCartRepository::class.java, DefaultProductRepository::class.java)
    }
}
