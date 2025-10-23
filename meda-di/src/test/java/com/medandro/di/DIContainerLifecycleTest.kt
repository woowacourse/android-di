package com.medandro.di

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.test.core.app.ApplicationProvider
import com.medandro.di.annotation.InjectField
import com.medandro.di.annotation.LifecycleScope
import com.medandro.di.annotation.Qualifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DIContainerLifecycleTest {
    private lateinit var applicationContext: Context
    private lateinit var sharedDIContainer: DIContainer

    @Before
    fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()
        // 모든 테스트에서 공유할 DIContainer
        sharedDIContainer =
            DIContainer(
                applicationContext,
                ActivityService::class,
                AppService::class,
                ViewModelService::class,
                ServiceWithContext::class,
                QualifiedService::class,
            )
        // TestActivity에서 사용할 수 있도록 설정
        TestActivity.diContainer = sharedDIContainer
    }

    // 테스트용 클래스들
    class AppService

    class ActivityService

    class ViewModelService

    class ServiceWithContext(val context: Context)

    @Qualifier("TestQualifier")
    class QualifiedService

    class TestActivity : ComponentActivity() {
        companion object {
            lateinit var diContainer: DIContainer
        }

        @InjectField(scope = LifecycleScope.ACTIVITY)
        lateinit var activityService: ActivityService

        @InjectField(scope = LifecycleScope.APPLICATION)
        lateinit var appService: AppService

        @InjectField(scope = LifecycleScope.AUTO)
        lateinit var autoService: ActivityService

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            diContainer.injectFields(this)
        }
    }

    class TestViewModel : BaseViewModel() {
        @InjectField(scope = LifecycleScope.VIEWMODEL)
        lateinit var viewModelService: ViewModelService

        @InjectField(scope = LifecycleScope.APPLICATION)
        lateinit var appService: AppService

        @InjectField(scope = LifecycleScope.AUTO)
        lateinit var autoService: ViewModelService
    }

    @Test
    fun `Application 스코프 인스턴스는 앱 전체에서 동일하다`() {
        // given
        class TestTarget1 {
            @InjectField(scope = LifecycleScope.APPLICATION)
            lateinit var service: AppService
        }

        class TestTarget2 {
            @InjectField(scope = LifecycleScope.APPLICATION)
            lateinit var service: AppService
        }

        val target1 = TestTarget1()
        val target2 = TestTarget2()

        // when
        sharedDIContainer.injectFields(target1)
        sharedDIContainer.injectFields(target2)

        // then
        assertThat(target1.service).isSameAs(target2.service)
    }

    @Test
    fun `Activity 스코프 인스턴스는 같은 Activity 내에서 동일하다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity = controller.get()

        // when - 같은 Activity에 대해 여러 번 getInstance 호출
        val service1 =
            sharedDIContainer.getInstance(
                DependencyKey(ActivityService::class),
                LifecycleScope.ACTIVITY,
                activity,
            )
        val service2 =
            sharedDIContainer.getInstance(
                DependencyKey(ActivityService::class),
                LifecycleScope.ACTIVITY,
                activity,
            )

        // then - 모두 같은 인스턴스여야 함
        assertThat(service1).isSameAs(service2)
        assertThat(activity.activityService).isSameAs(service1)

        controller.destroy()
    }

    @Test
    fun `다른 Activity 간에는 Activity 스코프 인스턴스가 다르다`() {
        // given
        val controller1 = Robolectric.buildActivity(TestActivity::class.java).setup()
        val controller2 = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity1 = controller1.get()
        val activity2 = controller2.get()

        // when - 다른 Activity에서 각각 서비스 요청
        val service1 =
            sharedDIContainer.getInstance(
                DependencyKey(ActivityService::class),
                LifecycleScope.ACTIVITY,
                activity1,
            )
        val service2 =
            sharedDIContainer.getInstance(
                DependencyKey(ActivityService::class),
                LifecycleScope.ACTIVITY,
                activity2,
            )

        // then
        assertThat(service1).isNotSameAs(service2)
        assertThat(activity1.activityService).isSameAs(service1)
        assertThat(activity2.activityService).isSameAs(service2)

        controller1.destroy()
        controller2.destroy()
    }

    @Test
    fun `ViewModel 스코프 인스턴스는 ViewModel마다 다르다`() {
        // given
        val viewModel1 = TestViewModel()
        val viewModel2 = TestViewModel()

        // when
        sharedDIContainer.injectFields(viewModel1)
        sharedDIContainer.injectFields(viewModel2)

        // then
        assertThat(viewModel1.viewModelService).isNotSameAs(viewModel2.viewModelService)
        // Application 스코프는 같아야 함
        assertThat(viewModel1.appService).isSameAs(viewModel2.appService)
    }

    @Test
    fun `AUTO 스코프는 Activity에서 ACTIVITY 스코프로 자동 변환된다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity = controller.get()

        // when & then - AUTO 스코프와 ACTIVITY 스코프가 같은 인스턴스 반환
        assertThat(activity.autoService).isSameAs(activity.activityService)

        controller.destroy()
    }

    @Test
    fun `AUTO 스코프는 ViewModel에서 VIEWMODEL 스코프로 자동 변환된다`() {
        // given
        val viewModel = TestViewModel()

        // when
        sharedDIContainer.injectFields(viewModel)

        // then - AUTO 스코프와 VIEWMODEL 스코프가 같은 인스턴스 반환
        assertThat(viewModel.autoService).isSameAs(viewModel.viewModelService)
    }

    @Test
    fun `Configuration Change시 새로운 Activity 인스턴스는 새로운 서비스 인스턴스를 받는다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val originalActivity = controller.get()
        val originalService = originalActivity.activityService

        // when - Configuration change 시뮬레이션
        controller.recreate()
        val recreatedActivity = controller.get()
        val recreatedService = recreatedActivity.activityService

        // then - 새로운 Activity 인스턴스이므로 다른 서비스 인스턴스를 받음
        assertThat(recreatedActivity).isNotSameAs(originalActivity)
        assertThat(recreatedService).isNotSameAs(originalService)
        // 하지만 Application 스코프는 동일
        assertThat(recreatedActivity.appService).isSameAs(originalActivity.appService)

        controller.destroy()
    }

    @Test
    fun `Activity destroy 후 같은 Activity context로 재요청 시 새 인스턴스 생성된다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity = controller.get()
        val original = activity.activityService

        // when - 완전 파괴 (Configuration Change 아님)
        controller.pause().stop().destroy()

        // then - 같은 activity 참조로 재요청해도 새 인스턴스가 생성됨 (스코프 정리됨)
        val newInstance =
            sharedDIContainer.getInstance(
                DependencyKey(ActivityService::class),
                LifecycleScope.ACTIVITY,
                activity,
            )
        assertThat(newInstance).isNotSameAs(original)
    }

    @Test
    fun `Activity pause-resume 시에는 Activity 스코프 인스턴스가 유지된다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity = controller.get()
        val originalService = activity.activityService

        // when - pause/resume (destroy되지 않음)
        controller.pause()

        // 다른 방법으로 같은 서비스 요청
        val pausedService =
            sharedDIContainer.getInstance(
                DependencyKey(ActivityService::class),
                LifecycleScope.ACTIVITY,
                activity,
            )

        controller.resume()
        val resumedService = activity.activityService

        // then - 모두 같은 인스턴스 유지
        assertThat(pausedService).isSameAs(originalService)
        assertThat(resumedService).isSameAs(originalService)

        controller.destroy()
    }

    @Test
    fun `Context 주입이 스코프에 따라 올바르게 동작한다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity = controller.get()

        // when - Application 스코프에서 Context 주입
        val appService =
            sharedDIContainer.getInstance(
                DependencyKey(ServiceWithContext::class),
                LifecycleScope.APPLICATION,
            ) as ServiceWithContext

        // Activity 스코프에서 Context 주입
        val activityService =
            sharedDIContainer.getInstance(
                DependencyKey(ServiceWithContext::class),
                LifecycleScope.ACTIVITY,
                activity,
            ) as ServiceWithContext

        // then
        assertThat(appService.context).isSameAs(applicationContext)
        assertThat(activityService.context).isSameAs(activity)
        assertThat(appService.context).isNotSameAs(activityService.context)

        controller.destroy()
    }

    @Test
    fun `ViewModel onCleared 시 ViewModel 스코프가 정리된다`() {
        // given - ViewModelStore 생성
        val store = ViewModelStore()
        val provider = ViewModelProvider(store, ViewModelProvider.NewInstanceFactory())

        val viewModel = provider[TestViewModel::class.java]
        sharedDIContainer.injectFields(viewModel)
        val originalService = viewModel.viewModelService

        // when - ViewModelStore 정리
        store.clear()

        // ViewModel 재생성
        val newProvider = ViewModelProvider(store, ViewModelProvider.NewInstanceFactory())
        val newViewModel = newProvider[TestViewModel::class.java]
        sharedDIContainer.injectFields(newViewModel)
        val newService = newViewModel.viewModelService

        // then
        assertThat(newService).isNotSameAs(originalService)
    }

    @Test(expected = IllegalStateException::class)
    fun `ACTIVITY 스코프를 Activity가 아닌 곳에서 사용하면 예외 발생`() {
        // given
        class RegularClass {
            @InjectField(scope = LifecycleScope.ACTIVITY)
            lateinit var service: ActivityService
        }

        val regularObject = RegularClass()

        // when - Activity가 아닌 곳에서 ACTIVITY 스코프 사용
        sharedDIContainer.injectFields(regularObject)
    }

    @Test(expected = IllegalStateException::class)
    fun `VIEWMODEL 스코프를 ViewModel이 아닌 곳에서 사용하면 예외 발생`() {
        // given
        class RegularClass {
            @InjectField(scope = LifecycleScope.VIEWMODEL)
            lateinit var service: ViewModelService
        }

        val regularObject = RegularClass()

        // when - ViewModel이 아닌 곳에서 VIEWMODEL 스코프 사용
        sharedDIContainer.injectFields(regularObject)
    }

    @Test
    fun `Qualifier와 함께 생명주기 스코프가 올바르게 작동한다`() {
        // given
        class TestTarget {
            @InjectField(scope = LifecycleScope.APPLICATION)
            @Qualifier("TestQualifier")
            lateinit var service1: QualifiedService

            @InjectField(scope = LifecycleScope.APPLICATION)
            @Qualifier("TestQualifier")
            lateinit var service2: QualifiedService
        }

        val target = TestTarget()

        // when
        sharedDIContainer.injectFields(target)

        // then - Qualifier와 스코프가 모두 적용되어 같은 인스턴스 반환
        assertThat(target.service1).isSameAs(target.service2)
    }

    @Test
    fun `같은 DIContainer에서 Application 스코프는 Activity destroy에 영향받지 않는다`() {
        // given
        val controller = Robolectric.buildActivity(TestActivity::class.java).setup()
        val activity = controller.get()
        val appService = activity.appService

        // when - Activity 완전 파괴
        controller.pause().stop().destroy()

        // 새로운 Activity 생성
        val newController = Robolectric.buildActivity(TestActivity::class.java).setup()
        val newActivity = newController.get()

        // then - Application 스코프 서비스는 동일하게 유지
        assertThat(newActivity.appService).isSameAs(appService)
        // Activity 스코프 서비스는 다름
        assertThat(newActivity.activityService).isNotSameAs(activity.activityService)

        newController.destroy()
    }
}
