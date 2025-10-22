package woowacourse.di

import androidx.activity.ComponentActivity
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.android.di.AndroidContainer
import woowacourse.shopping.android.di.Scope
import woowacourse.shopping.data.PersistentCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.ui.ShoppingApplication

@RunWith(RobolectricTestRunner::class)
class ApplicationScopeTest {
    @Test
    fun `CartRepository는 Application 생명주기 동안 유지된다`() {
        // given
        val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
        application.onCreate()

        val firstActivity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()
        val secondActivity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        // then
        assertThat(firstActivity.cartRepository).isSameInstanceAs(secondActivity.cartRepository)
    }

    private class TestActivity : ComponentActivity() {
        val cartRepository: CartRepository =
            AndroidContainer.instance(
                CartRepository::class,
                Scope.ApplicationScope,
                PersistentCartRepository.QUALIFIER,
            )
    }
}
