package woowacourse.shopping.di.annotationdi

import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

object FakeObj

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeAnnotationDIApplication::class)
class AnnotationTest {

    @Test
    fun `어노테이션을 사용한 프로퍼티만 주입된다`() {
        val activity = Robolectric
            .buildActivity(FakeAnnotationDIActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel!!

        Assertions.assertThat(viewModel.fakeObj).isSameAs(FakeObj)
    }
}
