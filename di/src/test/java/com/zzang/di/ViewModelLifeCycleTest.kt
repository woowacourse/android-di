package com.zzang.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.zzang.di.testfixture.FakeActivity
import com.zzang.di.testfixture.FakeApplication
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class ViewModelLifeCycleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        DIContainer.clearAll()
    }

    @Test
    fun `ViewModel을 자동으로 주입해준다`() {
        // GIVEN
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // WHEN

        // THEN
        val viewModel = activity.viewModel
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `ViewModel이 생성될 때 필요한 인스턴스가 자동으로 추가된다`() {
        // GIVEN
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // WHEN
        val viewModel = activity.viewModel

        // THEN
        assertThat(viewModel.inMemoryRepository).isNotNull()
    }

    @Test
    fun `ViewModel이 생성될 때 필요한 인스턴스가 qualifier에 따라 다르게 주입된다`() {
        // GIVEN
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // WHEN
        val viewModel = activity.viewModel

        // THEN
        assertThat(viewModel.inMemoryRepository).isNotSameInstanceAs(viewModel.databaseRepository)
    }

    @Test
    fun `ViewModel이 onDestroy될 때 ViewModelScope에 있는 인스턴스들이 제거된다`() {
        // GIVEN
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val viewModel = activity.viewModel

        // WHEN
        viewModel.onCleared()

        // THEN
        assertThat(DIContainer.viewModelScopedInstanceSize()).isEqualTo(0)
    }
}
