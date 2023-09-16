package com.re4rk.arkdiAndroid

import androidx.test.rule.ServiceTestRule
import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdiAndroid.fakeDependency.FakeActivityDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeApplicationDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeRetainedActivityDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeServiceDependency
import com.re4rk.arkdiAndroid.fakeDependency.FakeViewModelDependency
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ServiceController
import org.robolectric.annotation.Config

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class ArkServiceTest {
    @get:Rule
    val serviceRule = ServiceTestRule()

    private lateinit var serviceController: ServiceController<FakeService>

    @Before
    fun setUp() {
        // given
        serviceController = Robolectric.buildService(FakeService::class.java)
    }

    @Test
    fun `ArkService 클래스의 필드 주입은 @ArkInject 어노테이션을 사용한다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `어플리케이션 의존성이 주입이 됐다`()
            `서비스 의존성이 주입이 됐다`()
            `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`()
        }
    }

    @Test
    fun `ArkService 클래스는 ApplicationModule을 사용할 수 있다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `어플리케이션 의존성이 주입이 됐다`()
        }
    }

    @Test
    fun `ArkServie 클래스는 ServiceModule을 사용할 수 있다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `서비스 의존성이 주입이 됐다`()
        }
    }

    @Test
    fun `ArkService 클래스는 RetainedActivityModule을 모듈을 사용할 수 없다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `리테인드 액티비티 의존성이 주입이 안됐다`()
        }
    }

    @Test
    fun `ArkService 클래스는 ActivityModule을 사용할 수 없다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `액티비티 의존성이 주입이 안됐다`()
        }
    }

    @Test
    fun `ArkService 클래스는 ViewModelModule을 사용할 수 없다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `뷰모델 의존성이 주입이 안됐다`()
        }
    }

    private fun `서비스를 생성했을 때`(): FakeService {
        return serviceController.create().startCommand(0, 0).get()
    }

    class FakeService : ArkService() {
        @ArkInject
        lateinit var fakeApplicationDependency: FakeApplicationDependency

        @ArkInject
        lateinit var fakeServiceDependency: FakeServiceDependency
        lateinit var fakeServiceDependencyNoInject: FakeServiceDependency

        @ArkInject
        lateinit var fakeRetainedActivityDependency: FakeRetainedActivityDependency

        @ArkInject
        lateinit var fakeActivityDependency: FakeActivityDependency

        @ArkInject
        lateinit var fakeViewModelDependency: FakeViewModelDependency

        fun `어플리케이션 의존성이 주입이 됐다`() {
            assertThat(::fakeApplicationDependency.isInitialized).isTrue
        }

        fun `서비스 의존성이 주입이 됐다`() {
            assertThat(::fakeServiceDependency.isInitialized).isTrue
        }

        fun `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
            assertThat(::fakeServiceDependencyNoInject.isInitialized).isFalse
        }

        fun `리테인드 액티비티 의존성이 주입이 안됐다`() {
            assertThat(::fakeRetainedActivityDependency.isInitialized).isFalse
        }

        fun `액티비티 의존성이 주입이 안됐다`() {
            assertThat(::fakeActivityDependency.isInitialized).isFalse
        }

        fun `뷰모델 의존성이 주입이 안됐다`() {
            assertThat(::fakeViewModelDependency.isInitialized).isFalse
        }
    }
}
