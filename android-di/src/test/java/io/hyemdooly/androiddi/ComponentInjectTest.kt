package io.hyemdooly.androiddi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class ComponentInjectTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Application에서 @Inject가 붙은 파라미터에 인스턴스를 주입할 수 있다`() {
        // given
        val application = RuntimeEnvironment.getApplication() as FakeApplication

        // when
        val database = application.database
        val repository = application.repository

        // then
        assertThat(database).isNotNull()
        assertThat(repository).isNotNull()
    }

    @Test
    fun `Activity에서 생성자를 주입할 수 있고, @Inject가 붙은 파라미터에 인스턴스를 주입할 수 있다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val formatter = activity.formatter

        // then
        assertThat(formatter).isNotNull()
    }

    @Test
    fun `ViewModel에서 생성자를 주입할 수 있고, @Inject가 붙은 파라미터에 인스턴스를 주입할 수 있다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()
        val viewModel = (activity as FakeActivity).createViewModel(FakeViewModel::class)

        // then
        assertThat(viewModel).isNotNull()
    }
}
