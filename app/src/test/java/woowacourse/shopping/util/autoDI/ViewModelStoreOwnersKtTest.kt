package woowacourse.shopping.util.autoDI

import android.os.Bundle
import android.os.PersistableBundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ViewModelStoreOwnersKtTestNoOwnerProducerNoExtrasProducer {

    internal class FakeRepository

    internal class FakeViewModel(fakeRepository: FakeRepository) : ViewModel() {
        var testNumber = 0
    }

    internal class FakeActivity : AppCompatActivity() {
        val viewModel: FakeViewModel by injectViewModel()

        override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
            super.onCreate(savedInstanceState, persistentState)
        }

        fun makeFragmentContainer() {
            // 동적으로 FrameLayout을 생성하여 프래그먼트 컨테이너로 사용
            val fragmentContainer = FrameLayout(this).apply {
                id = FRAGMENT_CONTAINER_ID
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            }

            setContentView(fragmentContainer)
        }
    }

    internal class FakeFragment : Fragment() {
        val viewModel: FakeViewModel by injectViewModel()
    }

    internal class FakeFragmentForActivityInject : Fragment() {
        val viewModel: FakeViewModel by injectActivityViewModel()
    }

    init {
        AutoDI {
            singleton { FakeRepository() }
            viewModel { FakeViewModel(inject()) }
        }
    }

    @Test
    fun `Activity 환경에서 injectViewModel을 통해서 뷰모델이 주입된다 extrasProducer 제외`() {
        // when
        val activity: FakeActivity? = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()
        val viewModel = activity?.viewModel

        // then
        Truth.assertThat(viewModel).isNotNull()
        Truth.assertThat(viewModel).isInstanceOf(FakeViewModel::class.java)
    }

    // 프래그먼트 인스턴스를 얻을수 없음 현재  -> 추후 수정
    @Test
    @Ignore
    fun `Fragment 환경에서 injectViewModel을 통해서 뷰모델이 주입된다 ownerProducer,extrasProducer 제외`() {
        // when
        val activity: FakeActivity? = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()
        activity!!.supportFragmentManager.beginTransaction()
            .add<FakeFragment>(FRAGMENT_CONTAINER_ID, FRAGMENT_TAG)
            .commit()
        val fragment =
            activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as FakeFragment
        val viewModel = fragment.viewModel

        // then
        Truth.assertThat(viewModel).isNotNull()
        Truth.assertThat(viewModel).isInstanceOf(FakeViewModel::class.java)
    }

    companion object {
        private const val FRAGMENT_CONTAINER_ID = 139390023
        private const val FRAGMENT_TAG = "테스트 태그"
    }
}
