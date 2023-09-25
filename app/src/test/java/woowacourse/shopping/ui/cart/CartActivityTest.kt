package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import com.google.common.truth.Truth
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.di.annotation.InjectField
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()

        // then
        Truth.assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(CartActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        Truth.assertThat(viewModel).isNotNull()
    }

    @Test
    fun `Activity를 재생성했을 때, 액티비티 lifecycle을 따르는 필드 객체에 새로운 인스턴스가 주입된다`() {
        // given
        // activity를 만들었을 때의 dateformatter를 저장한다
        lateinit var firstDateFormatter: DateFormatter
        lateinit var secondDateFormatter: DateFormatter

        val scenario = ActivityScenario.launch(CartActivity::class.java)
        scenario.onActivity {
            val formatter = it::class.declaredMemberProperties.firstOrNull { property ->
                property.hasAnnotation<InjectField>()
            } ?: return@onActivity
            formatter.isAccessible = true
            firstDateFormatter = formatter.getter.call(it) as DateFormatter
        }

        // when
        // activity를 재생성하여 dateformatter를 저장한다
        scenario.recreate()
        scenario.onActivity {
            val formatter = it::class.declaredMemberProperties.firstOrNull { property ->
                property.hasAnnotation<InjectField>()
            } ?: return@onActivity
            formatter.isAccessible = true
            secondDateFormatter = formatter.getter.call(it) as DateFormatter
        }

        // then
        // 위 두 개를 비교하여 notEqual한지 확인한다
        assertNotEquals(firstDateFormatter, secondDateFormatter)
    }
}
