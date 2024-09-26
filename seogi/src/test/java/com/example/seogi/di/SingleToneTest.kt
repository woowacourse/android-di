package com.example.seogi.di

import com.example.seogi.fixture.Child2
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
class SingleToneTest {
    private val diContainer = DiApplication.diContainer

    @Test
    fun `SingleTone 어노테이션이 붙은 경우 앱이 실행 될 때 인스턴스가 저장된다`() {
        assertThat(diContainer.hasDependency(ParentFoo::class, Child2())).isTrue()
    }

    @Test
    fun `SingleTone 어노테이션이 붙은 경우 뷰모델이 종료되어도 인스턴스는 제거되지 않는다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .destroy()
                .get()

        // when
        activity.viewModel.onClearedViewModel()

        // then
        assertThat(diContainer.hasDependency(ParentFoo::class, Child2())).isTrue()
    }
}
