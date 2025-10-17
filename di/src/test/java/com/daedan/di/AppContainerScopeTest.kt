package com.daedan.di

import androidx.test.core.app.ApplicationProvider
import com.daedan.di.fixture.FakeActivity
import com.daedan.di.fixture.FakeApplication
import com.daedan.di.fixture.testModule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class) // 👈 클래스 레벨에서 Application 지정
class AppContainerScopeTest {
    @Test
    fun `인스턴스를 ViewModel Scope에 등록하면 액티비티가 파괴되어도 살아남는다`() {
        // given
        val app = ApplicationProvider.getApplicationContext<FakeApplication>()
        app.register(app.testModule())

        // when
        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()

        val before = activity.viewModel.arg1

        activity.recreate()

        val after =
            activity.viewModel.arg1

        // then
        assert(before === after)
    }

    @Test
    fun `인스턴스를 ActivityScope에 등록하면 액티비티가 파괴될 때 해제된다`() {
        // given
        val app = ApplicationProvider.getApplicationContext<FakeApplication>()
        app.register(app.testModule())
        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()

        val before = activity.activityArgument

        // when
        activity.recreate()

        val after = activity.activityArgument

        // then
        assert(before !== after)
    }
}
