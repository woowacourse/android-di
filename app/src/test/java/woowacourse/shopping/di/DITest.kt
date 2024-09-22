package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.fake.DefaultInMemoryFakeRepository
import woowacourse.shopping.fake.DefaultRoomFakeRepository
import woowacourse.shopping.fake.FakeActivity
import woowacourse.shopping.fake.FakeApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class DITest {

    @Test
    fun `DefaultRoomFakeRepository 주입 할 수 있다`() {
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        assertThat(activity.viewModel.fakeRepository).isEqualTo(DefaultRoomFakeRepository)
    }

    @Test
    fun `DefaultInMemoryFakeRepository 주입 할 수 있다`() {
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        assertThat(activity.viewModel2.fakeRepository).isEqualTo(DefaultInMemoryFakeRepository)
    }

    @Test
    fun `반환 타입이 같은 필드가 있어도 구별하여 주입 할 수 있다`() {
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        assertThat(activity.viewModel3.fakeRepository1).isEqualTo(DefaultRoomFakeRepository)
        assertThat(activity.viewModel3.fakeRepository2).isEqualTo(DefaultInMemoryFakeRepository)
    }
}
