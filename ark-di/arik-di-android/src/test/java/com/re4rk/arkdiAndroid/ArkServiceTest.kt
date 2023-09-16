package com.re4rk.arkdiAndroid

import androidx.test.rule.ServiceTestRule
import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdiAndroid.fakeClasses.FakeApplication
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeServiceDependency
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
    fun `ArkService 클래스는 @ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`()
        }
    }

    @Test
    fun `ArkService 클래스는 @ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
        // when
        `서비스를 생성했을 때`().apply {
            // then
            `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`()
        }
    }

    private fun `서비스를 생성했을 때`(): FakeService {
        return serviceController.create().startCommand(0, 0).get()
    }

    class FakeService : ArkService() {
        lateinit var fakeServiceDependencyNoInject: FakeServiceDependency

        @ArkInject
        lateinit var fakeServiceDependency: FakeServiceDependency

        fun `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
            assertThat(::fakeServiceDependency.isInitialized).isTrue
        }

        fun `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
            assertThat(::fakeServiceDependencyNoInject.isInitialized).isFalse
        }
    }
}
