package woowacourse.shopping

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ViewModelFactoryTest {

    @Test
    fun `Injector를 통해 ViewModel 의존성 주입을 할 수 있다`() {
        // given

        // when
        val activity: FakeActivityWithConstructor? =
            Robolectric.buildActivity(FakeActivityWithConstructor::class.java)
                .create()
                .get()
        val viewModel: FakeViewModelWithConstructor? = activity?.viewModel

        // then
        assertNotNull(viewModel)
        assertTrue(viewModel?.repository is DefaultFakeRepository)
    }
}
