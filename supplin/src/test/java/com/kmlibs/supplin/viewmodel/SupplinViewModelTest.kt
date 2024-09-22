package com.kmlibs.supplin.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity1
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity2
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity3
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity4
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity5
import com.kmlibs.supplin.fixtures.android.fragment.FakeFragment1
import com.kmlibs.supplin.fixtures.android.fragment.FakeFragment2
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SupplinViewModelTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Injector.init {
            context(context)
            module(FakeConcreteModule::class, FakeRepositoryModule::class, FakeDataSourceModule::class)
        }
    }

    /**
     * Activity tests
     */
    @Test
    fun `supplinViewModel should provide the correct ViewModel instance to Activities`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity1::class.java)
                .create()
                .get()

        // then
        val viewModel = activity.viewModel
        assertThat(viewModel).isNotNull()
        assertThat(viewModel.isRepositoryInitialized).isTrue()
    }

    @Test(expected = IllegalStateException::class)
    fun `supplinViewModel cannot provide Activities with ViewModel instance annotated with @Supply on properties in constructors`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity2::class.java)
                .create()
                .get()

        activity.viewModel
    }

    @Test
    fun `supplinViewModel should provide ViewModel instance to Activities without modules recursively - constructor injection`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity3::class.java)
                .create()
                .get()

        // then
        val viewModel = activity.viewModel
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `supplinViewModel should provide ViewModel instance to Activities without modules recursively - constructor and field injection`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity4::class.java)
                .create()
                .get()

        // then
        val viewModel = activity.viewModel
        assertThat(viewModel).isNotNull()
        assertThat(viewModel.isRepositoryInitialized).isTrue()
    }

    @Test
    fun `supplinViewModel should provide ViewModel instance to Activities without modules recursively`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity5::class.java)
                .create()
                .get()

        // then
        val viewModel = activity.viewModel
        assertThat(viewModel).isNotNull()
    }

    /**
     * Fragment Tests
     */
    @Test
    fun `supplinViewModel should provide the correct ViewModel instance to Fragments`() {
        // given & when
        val scenario = launchFragmentInContainer<FakeFragment1>()

        // then
        scenario.onFragment { fragment ->
            val viewModel = fragment.viewModel
            assertThat(viewModel).isNotNull()
            assertThat(viewModel.isRepositoryInitialized).isTrue()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `supplinViewModel cannot provide Fragments with ViewModel instance annotated with @Supply on properties in constructors`() {
        // given & when
        val scenario = launchFragmentInContainer<FakeFragment2>()

        // then
        scenario.onFragment { fragment ->
            val viewModel = fragment.viewModel
            assertThat(viewModel).isNotNull()
        }
    }
}
