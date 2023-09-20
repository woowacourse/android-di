package com.re4rk.arkdiAndroid

import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdiAndroid.fakeClasses.FakeApplication
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeActivityDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeApplicationDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeRetainedActivityDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeServiceDependency
import com.re4rk.arkdiAndroid.fakeClasses.fakeDependency.FakeViewModelDependency
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class ArkActivityTest {
    @Test
    fun `ArkActivity 클래스는 @ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`()
        }
    }

    @Test
    fun `ArkActivity 클래스는 @ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`()
        }
    }

    @Test
    fun `ArkActivity 클래스는 어플리케이션 모듈은 주입이 된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `어플리케이션 모듈은 주입이 된다`()
        }
    }

    @Test
    fun `ArkActivity 클래스는 리테인드 액티비티 모듈은 주입이 된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `리테인드 액티비티 모듈은 주입이 된다`()
        }
    }

    @Test
    fun `ArkActivity 클래스는 액티비티 모듈은 주입이 된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `액티비티 모듈은 주입이 된다`()
        }
    }

    @Test
    fun `ArkActivity 클래스는 뷰모델 모듈은 주입이 안된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `뷰모델 모듈은 주입이 안된다`()
        }
    }

    @Test
    fun `ArkActivity 클래스는 서비스 모듈은 주입이 안된다`() {
        // when
        `액티비티를 생성했을 때`().apply {
            // then
            `서비스 모듈은 주입이 안된다`()
        }
    }

    private fun `액티비티를 생성했을 때`(): FakeActivity {
        return Robolectric.buildActivity(FakeActivity::class.java).create().get()
    }

    class FakeActivity : ArkAppCompatActivity() {
        lateinit var fakeActivityDependencyNoInject: FakeActivityDependency

        @ArkInject
        lateinit var fakeActivityDependency: FakeActivityDependency

        @ArkInject
        lateinit var fakeApplicationDependency: FakeApplicationDependency

        @ArkInject
        lateinit var fakeRetainedActivityDependency: FakeRetainedActivityDependency

        @ArkInject
        lateinit var fakeViewModelDependency: FakeViewModelDependency

        @ArkInject
        lateinit var fakeServiceDependency: FakeServiceDependency

        fun `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
            assertThat(::fakeActivityDependency.isInitialized).isTrue
        }

        fun `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
            assertThat(::fakeActivityDependencyNoInject.isInitialized).isFalse
        }

        fun `어플리케이션 모듈은 주입이 된다`() {
            assertThat(::fakeApplicationDependency.isInitialized).isTrue
        }

        fun `리테인드 액티비티 모듈은 주입이 된다`() {
            assertThat(::fakeRetainedActivityDependency.isInitialized).isTrue
        }

        fun `액티비티 모듈은 주입이 된다`() {
            assertThat(::fakeActivityDependency.isInitialized).isTrue
        }

        fun `뷰모델 모듈은 주입이 안된다`() {
            assertThat(::fakeViewModelDependency.isInitialized).isFalse
        }

        fun `서비스 모듈은 주입이 안된다`() {
            assertThat(::fakeServiceDependency.isInitialized).isFalse
        }
    }
}
