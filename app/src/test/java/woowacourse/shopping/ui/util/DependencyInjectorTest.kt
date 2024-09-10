package woowacourse.shopping.ui.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.FakeActivity
import woowacourse.shopping.FakeRepositoryImpl

@RunWith(RobolectricTestRunner::class)
class DependencyInjectorTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var activity: FakeActivity

    @Before
    fun setUp() {
        activity =
            Robolectric.buildActivity(FakeActivity::class.java)
                .create()
                .get()
    }

    @Test
    fun `알맞은 인스턴스를 주입한다`() {
        val actual = activity.viewModel.fakeRepository
        val expected = FakeRepositoryImpl::class.java

        assertThat(actual).isInstanceOf(expected)
    }
}
