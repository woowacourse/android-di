package com.woowa.di.injection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import com.woowa.di.fixture.TestActivity
import com.woowa.di.fixture.TestApplication
import com.woowa.di.fixture.qualifier.FailQualifierTestViewModel
import com.woowa.di.fixture.qualifier.QualifierTestViewModel
import com.woowa.di.test.DIActivityTestRule
import com.woowa.di.viewmodel.getDIViewModelFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class QualifierTest {
    private lateinit var activity: TestActivity

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var diRule = DIActivityTestRule(TestActivity::class.java)

    @Before
    fun setUp() {
        activity = diRule.getActivity()
    }

    @Test
    fun `interface에 대한 서로 다른 구현체를 구분하여 DI 주입을 할 수 있다`() {
        // given
        val viewModel =
            ViewModelProvider(
                activity,
                getDIViewModelFactory<QualifierTestViewModel>(),
            )[QualifierTestViewModel::class.java]

        // then
        assertThat(viewModel.fake1).isNotEqualTo(viewModel.fake2)
    }

    @Test
    fun `Qualifier 어노테이션으로 같은 타입의 구현 객체를 구분하지 않으면, 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ViewModelProvider(
                activity,
                getDIViewModelFactory<FailQualifierTestViewModel>(),
            )[FailQualifierTestViewModel::class.java]
        }
    }
}
