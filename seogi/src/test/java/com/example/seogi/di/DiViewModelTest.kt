package com.example.seogi.di

import com.example.seogi.fixture.Child1
import com.example.seogi.fixture.FakeActivity
import com.example.seogi.fixture.FakeApplication
import com.example.seogi.fixture.ParentFoo
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class DiViewModelTest {
    private val diContainer = DiApplication.diContainer

    @Test
    fun `ViewModel이 생성될 때 인스턴스가 저장된다`() {
        // given
        Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        assertThat(diContainer.hasDependency(ParentFoo::class, Child1())).isTrue()
    }

    @Test
    fun `ViewModel이 onCleared될 때 인스턴스가 저장된다`() {
        // given
        val viewModel =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()
                .viewModel

        // when
        viewModel.onClearedViewModel()

        // then
        assertThat(diContainer.hasDependency(ParentFoo::class, Child1())).isFalse()
    }
}
