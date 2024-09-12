package woowacourse.shopping.di

import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.ViewModelFactory
import woowacourse.shopping.util.injectedViewModels

class FakeDataSource1

class FakeDataSource2

interface FakeRepository

class FakeRepositoryImpl(
    @com.example.di.Inject val fakeDataSource: FakeDataSource1,
    val fakeDataSourceImplWithNoAutoInject: FakeDataSource2? = null,
) : FakeRepository

class FakeViewModel(
    @com.example.di.Inject val fakeRepository: FakeRepository,
) : ViewModel() {
    @com.example.di.Inject
    lateinit var fieldFakeRepository: FakeRepository
}

private val testModule =
    com.example.di.Module().apply {
        addDeferredTypes(
            FakeRepository::class to FakeRepositoryImpl::class,
            FakeDataSource1::class to FakeDataSource1::class,
            FakeDataSource2::class to FakeDataSource2::class,
        )
    }

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by injectedViewModels {
        ViewModelFactory(testModule)
    }
}

@RunWith(RobolectricTestRunner::class)
class ViewModelInjectorTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: FakeActivity

    @Before
    fun setup() {
        activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
    }

    @Test
    fun `뷰모델 생성자 파라미터에 올바른 인스턴스를 주입한다`() {
        val actual = activity.viewModel.fakeRepository
        val expected = FakeRepositoryImpl::class.java

        assertThat(actual).isInstanceOf(expected)
    }

    @Test
    fun `뷰모델 필드에 올바른 인스턴스를 주입한다`() {
        val actual = activity.viewModel.fieldFakeRepository
        val expected = FakeRepositoryImpl::class.java

        assertThat(actual).isInstanceOf(expected)
    }

    @Test
    fun `Inject Annotation이 붙여지지 않은 요소에는 주입하지 않는다`() {
        val repository = (activity.viewModel.fakeRepository as FakeRepositoryImpl)
        val actual = repository.fakeDataSourceImplWithNoAutoInject
        assertThat(actual).isNull()
    }
}
