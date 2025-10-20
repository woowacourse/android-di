package test

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import test.fixture.FakeCartViewModel
import test.fixture.TestActivity

@RunWith(RobolectricTestRunner::class)
class AutoViewModelFactoryTest {
    private lateinit var controller: ActivityController<TestActivity>

    @Before
    fun setUp() {
        controller = Robolectric.buildActivity(TestActivity::class.java).setup()
    }

    @Test
    fun `Activity가 finish 되면 ViewModel도 파괴된다`() {
        // given
        val activity = controller.get()

        // when
        activity.finish()

        // then
        val viewModelStore =
            activity.viewModelStore.keys().contains("${FakeCartViewModel::class.qualifiedName}")
        Assert.assertFalse(viewModelStore)
    }

    @Test
    fun `Activity가 configurationChange 되도 ViewModel의 인스턴스는 같다`() {
        // given
        val viewModelBeforeChange = controller.get().viewModel

        // when
        controller.configurationChange()
        val viewModelAfterChange = controller.get().viewModel

        // then

        Assert.assertEquals(viewModelBeforeChange, viewModelAfterChange)
    }
}
