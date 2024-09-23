package com.android.diandroid

import com.android.di.component.DiContainer
import com.android.di.component.DiInjector
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ApplicationInjectorTest {
    private lateinit var testActivity: TestActivity
    private lateinit var application: TestApplication

    @Before
    fun setup() {
        val activityController = Robolectric.buildActivity(TestActivity::class.java)
        testActivity = activityController.get()
        application = testActivity.application as TestApplication
    }

    @Test
    fun `애플리케이션이 생성되면 diInjector가 초기화되어야 한다`() {
        assert(application.getApplicationContainer() != null)
    }

    @Test
    fun `diContainer에 주입하면 해당 class로 childInjectors에 추가되어야 한다`() {
        // given
        val realActivityName = testActivity::class.java.name
        val realInjector = DiInjector(DiContainer())

        // when
        application.saveInjector(realActivityName, realInjector)

        // then
        assert(application.getInjector(realActivityName) == realInjector)
    }

    @Test
    fun `childInjectors에서 지정한 키로 injector를 제거할 수 있어야 한다`() {
        // given
        val injector = DiInjector(DiContainer())
        application.saveInjector(application.javaClass.name, injector)

        // when
        application.removeInjector(application.javaClass.name)

        // then
        assert(application.getInjector(application.javaClass.name) == null)
    }
}
