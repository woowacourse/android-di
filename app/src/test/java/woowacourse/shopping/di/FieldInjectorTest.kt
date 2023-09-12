package woowacourse.shopping.di

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.Fake

@RunWith(RobolectricTestRunner::class)
@Config(application = Fake.TestApplication::class)
class FieldInjectorTest {

    @Test
    fun `InjectField 어노테이션이 붙은 필드에 필드 주입한다`() {
        // given
        val activity = Robolectric
            .buildActivity(Fake.FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        Assert.assertNotNull(viewModel.pingu)
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `InjectField 어노테이션이 붙지 않은 필드에는 필드 주입하지 않는다`() {
        // given
        val activity = Robolectric
            .buildActivity(Fake.FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        Assert.assertNotNull(viewModel.otter)
    }
}
