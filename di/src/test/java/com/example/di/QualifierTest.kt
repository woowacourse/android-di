package com.example.di

import FakeDatabaseCartRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fake.FakeActivity
import com.example.fake.FakeApplication
import com.example.fake.FakeInMemoryCartRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class QualifierTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: FakeActivity

    @Before
    fun setup() {
        activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()
    }

    @Test
    fun `Qualifier Annotation이 붙은 생성자는 정확한 타입의 의존성이 주입된다`() {
        val cartRepository = activity.viewModel.fakeCartRepository
        assertThat(cartRepository).isInstanceOf(FakeDatabaseCartRepository::class.java)
    }

    @Test
    fun `Qualifier Annotation이 없는 경우 기본 구현체는 주입되지 않는다`() {
        val databaseCartRepository = activity.viewModel.fakeCartRepository
        assertThat(databaseCartRepository).isNotInstanceOf(FakeInMemoryCartRepository::class.java)
    }
}
