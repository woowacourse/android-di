package com.example.di

import FakeDatabaseCartRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fake.FakeActivity
import com.example.fake.FakeApplication
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
class InjectorTest {
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
    fun `Inject Annotation이 붙은 생성자는 자동으로 의존성이 주입된다`() {
        val cartRepository = activity.viewModel.fakeCartRepository

        assertThat(cartRepository).isNotNull()
    }

    @Test
    fun `Inject Annotation이 붙지 않은 생성자는 자동으로 의존성이 주입되지 않는다`() {
        val cartRepository =
            (activity.viewModel.fakeCartRepository) as FakeDatabaseCartRepository
        val productRepository = cartRepository.fakeProductRepository

        assertThat(productRepository).isNull()
    }

    @Test
    fun `Inject Annotation이 붙은 필드는 자동으로 의존성이 주입된다`() {
        val fieldRepository = activity.viewModel.fakeFieldRepository

        assertThat(fieldRepository).isNotNull()
    }
}
