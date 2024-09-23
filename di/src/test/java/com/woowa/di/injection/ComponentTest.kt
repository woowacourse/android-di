package com.woowa.di.injection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import com.woowa.di.activity.ActivityComponentManager
import com.woowa.di.fixture.TestApplication
import com.woowa.di.fixture.component.ComponentTestViewModel
import com.woowa.di.fixture.component.ComponentTestViewModel2
import com.woowa.di.fixture.component.FailComponentTestViewModel
import com.woowa.di.fixture.component.TestActivity
import com.woowa.di.fixture.component.TestActivityComponent
import com.woowa.di.test.DIActivityTestRule
import com.woowa.di.viewmodel.getDIViewModelFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ComponentTest {
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
    fun `자동 DI가 주입된 viewModel 객체가 생성된다`() {
        // given
        val viewModel =
            ViewModelProvider(
                activity,
                getDIViewModelFactory<ComponentTestViewModel>(),
            )[ComponentTestViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
        assertDoesNotThrow {
            viewModel.singletonFake
            viewModel.viewModelFake
        }
    }

    @Test
    fun `Inject 어노테이션이 없을 경우, DI 주입이 안 된다`() {
        // given
        val viewModel =
            ViewModelProvider(
                activity,
                getDIViewModelFactory<FailComponentTestViewModel>(),
            )[FailComponentTestViewModel::class.java]

        // when
        assertThat(viewModel::class.declaredMemberProperties.none { it.hasAnnotation<Inject>() }).isTrue()

        // then
        assertThrows<UninitializedPropertyAccessException> {
            viewModel.fake
        }
    }

    @Test
    fun `viewModelComponent에서 생성된 Di 객체는 2개의 viewModel에서 사용할 수 없다`() {
        // given
        val viewModel =
            ViewModelProvider(
                activity,
                getDIViewModelFactory<ComponentTestViewModel>(),
            )[ComponentTestViewModel::class.java]
        assertThat(viewModel.viewModelFake).isNotNull()

        // then
        assertThrows<IllegalArgumentException> {
            ViewModelProvider(
                activity,
                getDIViewModelFactory<ComponentTestViewModel2>(),
            )[ComponentTestViewModel2::class.java]
        }
    }

    @Test
    fun `activity가 소멸되면, 주입된 객체는 제거되기 때문에, 객체를 호출하면 에러가 발생한다`() {
        // given
        val diInstance = ActivityComponentManager.getDIInstance(TestActivityComponent::class)
        assertThat(diInstance).isNotNull()

        // when
        activity.finish()
        diRule.getController().destroy()

        // then
        assertThrows<IllegalStateException> {
            ActivityComponentManager.getDIInstance(TestActivityComponent::class)
        }
    }
}
