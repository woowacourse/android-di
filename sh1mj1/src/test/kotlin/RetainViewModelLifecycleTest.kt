package com.example.sh1mj1

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.container.viewmodelscope.ViewModelScopeComponentContainer
import com.example.sh1mj1.stub.StubActivity
import com.example.sh1mj1.stub.StubApplication
import com.example.sh1mj1.stub.StubRepo
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApplication::class)
class RetainViewModelLifecycleTest {
    @Test
    fun `액티비티에서 뷰모델이 정상적으로 주입도어 뷰모델의 함수를 사용할 수 있다`() {
        // given & when
        val activityController =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()

        val activity = activityController.get()

        // then
        activity.useViewModelFunction()
        shouldNotThrow<Exception> { activity.useViewModelFunction() }
    }

    @Test
    fun `액티비티의 구성변경이 일어나도 뷰모델은 유지되어 뷰모델은 같다`() {
        // given
        val activityController =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()

        val activity = activityController.get()

        // when
        val originalViewModel = activity.viewModel

        // 구성 변경이 일어나도 뷰모델은 유지된다 그 시나리오임.
        activityController.configurationChange()

        val viewModelAfterConfigurationChanged = activity.viewModel

        // then
        originalViewModel shouldBeSameInstanceAs viewModelAfterConfigurationChanged
    }

    @Test
    fun `액티비티의 구성변경이 일어나도 뷰모델은 유지되어 뷰모델의 싱글톤 레포지토리는 같다`() {
        // given
        val activityController =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()

        val activity = activityController.get()

        // when
        val originalRepo = activity.viewModel.repo1

        // 구성 변경이 일어나도 뷰모델은 유지된다 그 시나리오임.
        activityController.configurationChange()

        val repoAfterConfigurationChanged = activity.viewModel.repo1

        // then
        originalRepo shouldBeSameInstanceAs repoAfterConfigurationChanged
    }

    @Test
    fun `뷰모델이 clear 되었을 때 뷰모델이 가진 뷰모델 스코프 레포지토리는 삭제된다`() {
        // given
        val activityController =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()

        val activity = activityController.get()
        val viewModelScopeRepo = activity.viewModel.repo2

        // when
        activityController.destroy()

        // then
        ViewModelScopeComponentContainer.instance().find(
            clazz = StubRepo::class,
            qualifier = Qualifier("viewModelScope"),
        ) shouldBe null
    }
}
