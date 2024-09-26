package olive.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import olive.di.fixture.Baz
import olive.di.fixture.TestApplication
import olive.di.fixture.ViewModelScopeTestViewModel1
import olive.di.fixture.ViewModelScopeTestViewModel2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewModelScopeTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `뷰모델이 생성되면 인스턴스가 생성된다`() {
        // when
        val viewModel = DIContainer.instance(ViewModelScopeTestViewModel1::class)

        // then
        val actual = viewModel.baz
        assertThat(instances[Baz::class]).isEqualTo(actual)
        assertThat(actual).isInstanceOf(Baz::class.java)
    }

    @Test
    fun `뷰모델이 소멸되면 인스턴스가 소멸된다`() {
        // given
        val viewModel = DIContainer.instance(ViewModelScopeTestViewModel1::class)

        // when
        viewModel.onClearedTest()

        // then
        assertThat(instances[Baz::class]).isNull()
    }

    @Test
    fun `두 뷰모델이 서로 다른 인스턴스를 가진다`() {
        // when
        val viewModel1 = DIContainer.instance(ViewModelScopeTestViewModel1::class)
        val viewModel2 = DIContainer.instance(ViewModelScopeTestViewModel2::class)

        // then
        val actual1 = viewModel1.baz
        val actual2 = viewModel2.baz
        assertThat(actual1).isNotSameInstanceAs(actual2)
    }
}
