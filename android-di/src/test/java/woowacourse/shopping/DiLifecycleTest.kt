package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowActivity
import woowacourse.shopping.fake.activity.FakeActivity
import woowacourse.shopping.fake.application.FakeApplication

@RunWith(RobolectricTestRunner::class)
@Config(
    application = FakeApplication::class,
)
class DiLifecycleTest {
    private lateinit var controller: ActivityController<FakeActivity>

    @Before
    fun setup() {
        controller = buildActivity(FakeActivity::class.java).setup()
    }

    @Test
    fun `CartRepository는_액티비티가_재생성되었을_때_이전과_동일한_인스턴스를_가진다`() {
        // given & when
        val previousCartRepository = controller.get().cartViewModel.cartRepository
        controller.recreate()

        // then
        assertThat(controller.get().cartViewModel.cartRepository).isEqualTo(previousCartRepository)
    }

    @Test
    fun `MainViewModel과_CartViewModel의_CartRepository는_동일한_인스턴스를_가진다`() {
        // given & when

        // then
        assertThat(controller.get().mainViewModel.cartRepository).isEqualTo(controller.get().cartViewModel.cartRepository)
    }

    @Test
    fun `ProductRepository는_액티비티가_재생성되었을_때_이전과_동일한_인스턴스를_가진다`() {
        // given & when
        val previousProductRepository = controller.get().mainViewModel.productRepository
        controller.recreate()

        // then
        assertThat(controller.get().mainViewModel.productRepository).isEqualTo(previousProductRepository)
    }

    @Test
    fun `DateFormatter1은_액티비티가_재생성되었을_때_이전과_동일한_인스턴스를_가진다`() {
        // given & when
        val previousDateFormatter = controller.get().dateFormatter1
        controller.recreate()

        ShadowActivity()
        // then
        assertThat(controller.get().dateFormatter1).isEqualTo(previousDateFormatter)
    }

    @Test
    fun `DateFormatter2는_액티비티가_재생성되었을_때_이전과_동일한_인스턴스를_가지지_않는다`() {
        // given & when
        val previousDateFormatter = controller.get().dateFormatter2
        controller.recreate()

        // then
        assertThat(controller.get().dateFormatter2).isNotEqualTo(previousDateFormatter)
    }
}
