package com.example.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.example.di.activity.DiEntryPointActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
internal class ApplicationModuleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    class FakeFirstViewModel(
        @FakeInMemoryCartRepositoryQualiefier val cartRepository: FakeCartRepository,
    ) : ViewModel()

    class FakeSecondViewModel(
        @FakeInMemoryCartRepositoryQualiefier val cartRepository: FakeCartRepository,
    ) : ViewModel()

    @Test
    fun `애플리케이션 모듈에 정의된 함수들로 만들어진 객체는 서로 다른 액티비티에서 같은 값을 반환받는다`() {
        // given
        class FakeFirstActivity : DiEntryPointActivity() {
            val viewModel: FakeFirstViewModel by viewModel()
        }

        class FakeSecondActivity : DiEntryPointActivity() {
            val viewModel: FakeSecondViewModel by viewModel()
        }

        // when
        val firstActivity = Robolectric
            .buildActivity(FakeFirstActivity::class.java)
            .create()
            .get()

        val secondActivity = Robolectric
            .buildActivity(FakeSecondActivity::class.java)
            .create()
            .get()

        // then
        val firstViewModel = firstActivity.viewModel
        val secondViewModel = secondActivity.viewModel
        assertThat(firstViewModel.cartRepository === secondViewModel.cartRepository).isTrue
    }
}
