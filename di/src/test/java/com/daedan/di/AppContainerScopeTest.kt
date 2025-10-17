package com.daedan.di

import androidx.test.core.app.ApplicationProvider
import com.daedan.di.fixture.Child1
import com.daedan.di.fixture.FakeActivity
import com.daedan.di.fixture.FakeApplication
import com.daedan.di.fixture.TestViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class) // 👈 클래스 레벨에서 Application 지정
class AppContainerScopeTest {
    @Test
    fun `인스턴스를 ViewModel 생명주기에 등록하면 뷰모델이 소멸될 때 해제된다`() {
        // given
        val app = ApplicationProvider.getApplicationContext<FakeApplication>()
        val module =
            app.module {
                scope<TestViewModel> {
                    scoped { Child1() }
                }
                viewModel {
                    TestViewModel(get(scope = it))
                }
            }

        app.register(module)

        // when
        val controller =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()

        val before =
            controller
                .get()
                .viewModel.arg1

        controller.pause().stop().destroy()

        val controller2 = Robolectric.buildActivity(FakeActivity::class.java).create()
        val after =
            controller2
                .get()
                .viewModel.arg1

        // then
        assert(before != after)
    }
}
