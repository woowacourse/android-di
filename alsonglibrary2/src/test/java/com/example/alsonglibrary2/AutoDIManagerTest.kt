package com.example.alsonglibrary2

import androidx.lifecycle.ViewModelProvider
import com.example.alsonglibrary2.di.AutoDIManager
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class AutoDIManagerTest {
    private lateinit var activity: FakeActivity
    private lateinit var viewModel: FakeViewModel

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()

        viewModel = ViewModelProvider(
            activity,
//            AutoDIManager.createViewModelFactory<FakeViewModel>(),
        )[FakeViewModel::class.java]
    }

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeRepository = DefaultFakeRepository()
        AutoDIManager.registerDependency<FakeRepository>(fakeRepository)
        // when

        // then
        val actual = activity.viewModel
        assertThat(actual).isNotNull()
        assertThat(actual.fakeRepository).isEqualTo(fakeRepository);
    }
}