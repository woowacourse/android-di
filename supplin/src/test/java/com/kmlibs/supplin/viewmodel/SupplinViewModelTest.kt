package com.kmlibs.supplin.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity1
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity2
import com.kmlibs.supplin.fixtures.android.fragment.FakeFragment1
import com.kmlibs.supplin.fixtures.android.fragment.FakeFragment2
import com.kmlibs.supplin.fixtures.android.module.FakeModule
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
            module(FakeModule::class)
        }
    }

    /**
     * Activity tests
     */
    @Test
    fun `supplinViewModel should provide the correct ViewModel instance to Activities`() {
        // given & when
        val activity = Robolectric
            .buildActivity(FakeActivity1::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertThat(viewModel).isNotNull()
    }

    @Test(expected = IllegalStateException::class)
    fun `supplinViewModel cannot provide Activities with ViewModel instance annotated with @Supply on properties in constructors`() {
        // given & when
        val activity = Robolectric
            .buildActivity(FakeActivity2::class.java)
            .create()
            .get()

        activity.viewModel
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
