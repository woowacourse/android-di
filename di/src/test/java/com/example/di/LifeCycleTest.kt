package com.example.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.annotation.QualifierType
import com.example.fake.FakeActivity
import com.example.fake.FakeApplication
import com.example.fake.FakeCartRepository
import com.example.fake.FakeFieldRepository
import com.example.fake.FakeProductRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class LifeCycleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activityController: ActivityController<FakeActivity>
    private lateinit var activity: FakeActivity
    private lateinit var application: FakeApplication

    @Before
    fun setup() {
        activityController = Robolectric.buildActivity(FakeActivity::class.java)
        activity = activityController.get()
        application = activity.application as FakeApplication
    }

    @Test
    fun `Application이 생성되면 LifeCycleScope이 APPLICATION인 인스턴스가 주입된다`() {
        val containsCartRepository =
            DIContainer.containsInstance(FakeCartRepository::class, QualifierType.Database)

        assertThat(containsCartRepository).isTrue()
    }

    @Test
    fun `Application이 종료되면 LifeCycleScope이 APPLICATION인 인스턴스가 삭제된다`() {
        application.onTerminate()

        val containsCartRepository =
            DIContainer.containsInstance(FakeCartRepository::class, QualifierType.Database)

        assertThat(containsCartRepository).isFalse()
    }

    @Test
    fun `Activity가 생성되면 LifeCycleScope이 ACTIVITY인 인스턴스가 주입된다`() {
        activity = activityController.create().get()

        activityController.start()

        val containsFieldRepository =
            DIContainer.containsInstance(FakeFieldRepository::class)

        assertThat(containsFieldRepository).isTrue()
    }

    @Test
    fun `Activity가 종료되면 LifeCycleScope이 ACTIVITY인 인스턴스가 삭제된다`() {
        activity = activityController.create().get()

        activityController.destroy()

        val containsFieldRepository =
            DIContainer.containsInstance(FakeFieldRepository::class)

        assertThat(containsFieldRepository).isFalse()
    }

    @Test
    fun `ViewModel이 생성되면 LifeCycleScope이 VIEWMODEL인 인스턴스가 주입된다`() {
        activity = activityController.create().get()

        val containsProductRepository =
            DIContainer.containsInstance(FakeProductRepository::class)

        assertThat(containsProductRepository).isTrue()
    }

    @Test
    fun `ViewModel이 종료되면 LifeCycleScope이 VIEWMODEL인 인스턴스가 삭제된다`() {
        activity = activityController.create().get()

        activity.viewModel.onCleared()

        val containsProductRepository =
            DIContainer.containsInstance(FakeProductRepository::class)

        assertThat(containsProductRepository).isFalse()
    }
}
