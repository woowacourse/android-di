package com.zzang.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.zzang.di.testfixture.FakeActivity
import com.zzang.di.testfixture.FakeApplication
import com.zzang.di.testfixture.FakeViewModel
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

    @Test
    fun `ViewModel이 생성될 때 인스턴스가 저장된다`() {
        // GIVEN
        Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // WHEN

        // THEN
        assertThat(DIContainer.resolve(FakeViewModel::class)).isNotNull()
    }
}
