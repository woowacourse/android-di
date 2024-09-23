package com.example.seogi.di

import com.example.seogi.fixture.FakeActivity
import com.example.seogi.fixture.FakeApplication
import com.example.seogi.fixture.FakeDateFormatter
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class DiActivityTest {
    private val diContainer = DiApplication.diContainer

    @Test
    fun `Activity가 실행될 때 dateFormatter인스턴스가 저장된다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()

        // then
        assertThat(diContainer.hasDependency(FakeDateFormatter::class, null)).isTrue()
    }

    @Test
    fun `Activity가 destroy될 때 dateFormatter인스턴스가 삭제된다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .destroy()
                .get()

        // then
        assertThat(diContainer.hasDependency(FakeDateFormatter::class, null)).isFalse()
    }
}
