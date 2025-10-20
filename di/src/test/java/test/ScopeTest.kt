package test

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import test.fixture.TestActivity

@RunWith(RobolectricTestRunner::class)
class ScopeTest {
    @Test
    fun `AppScope를 갖는 프로퍼티의 인스턴스는 싱글톤이다`() {
        // given
        val firstController =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .setup()

        // when
        firstController.destroy()
        val secondController =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .setup()

        // then
        val firstViewModel = firstController.get().viewModel
        val secondViewModel = secondController.get().viewModel

        Assert.assertEquals(
            firstViewModel.fakeCartRepository,
            secondViewModel.fakeCartRepository,
        )
    }

    @Test
    fun `ViewModel이 파괴되면 ViewModelScope를 갖는 프로퍼티의 캐시가 정리되어 인스턴스가 달라진다`() {
        // given
        val firstController =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .setup()

        // when
        firstController.destroy()
        val secondController =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .setup()

        // then
        val firstViewModel = firstController.get().viewModel
        val secondViewModel = secondController.get().viewModel

        Assert.assertNotEquals(
            firstViewModel.fakeProductRepository,
            secondViewModel.fakeProductRepository,
        )
    }

    @Test
    fun `Activity가 파괴되면 ActivityScope를 갖는 프로퍼티의 캐시가 정리되어 인스턴스가 달라진다`() {
        // given
        val firstController =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .setup()

        // when
        firstController.destroy()
        val secondController =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .setup()

        // then
        val firstDateFormatter = firstController.get().dateFormatter
        val secondDateFormatter = secondController.get().dateFormatter

        Assert.assertNotEquals(firstDateFormatter, secondDateFormatter)
    }
}
