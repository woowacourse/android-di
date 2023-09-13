package woowacourse.shopping.di.constructordi

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.InMemoryFakeRepository
import woowacourse.shopping.di.OnDiskFakeRepository

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeConstructorDIApplication::class)
class ConstructorDITest {

    @Test
    fun `맞는 타입의 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        val activity = Robolectric
            .buildActivity(FakeConstructorDIActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel

        assertThat(viewModel).isNotNull
    }

    @Test
    fun `같은 타입이라도 어노테이션에 따라 다른 인스턴스가 주입된다`() {
        val activity = Robolectric
            .buildActivity(FakeConstructorDIActivity::class.java)
            .create()
            .get()

        val viewModel = fakeConstructorDIViewModel(activity)

        assertThat(viewModel?.inMemoryFakeRepository).isInstanceOf(InMemoryFakeRepository::class.java)
        assertThat(viewModel?.onDiskFakeRepository).isInstanceOf(OnDiskFakeRepository::class.java)
    }

    private fun fakeConstructorDIViewModel(activity: FakeConstructorDIActivity?) =
        activity?.viewModel
}
