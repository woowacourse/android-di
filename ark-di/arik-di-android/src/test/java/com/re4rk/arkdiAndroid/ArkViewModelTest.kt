package com.re4rk.arkdiAndroid

import androidx.lifecycle.ViewModel
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
class ArkViewModelTest {
    @Test
    fun `ArkViewModel 클래스는 @ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`()
        }
    }

    @Test
    fun `ArkViewModel 클래스는 @ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`()
        }
    }

    @Test
    fun `ArkViewModel 클래스는 어플리케이션 모듈은 주입이 된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `어플리케이션 모듈은 주입이 된다`()
        }
    }

    @Test
    fun `ArkViewModel 클래스는 리테인드 액티비티 모듈은 주입이 안된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `리테인드 액티비티 모듈은 주입이 된다`()
        }
    }

    @Test
    fun `ArkViewModel 클래스는 액티비티 모듈은 주입이 안된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `액티비티 모듈은 주입이 안된다`()
        }
    }

    @Test
    fun `ArkViewModel 클래스는 뷰모델 모듈은 주입이 된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `뷰모델 모듈은 주입이 된다`()
        }
    }

    @Test
    fun `ArkViewModel 클래스는 서비스 모듈은 주입이 안된다`() {
        // when
        `뷰모델을 생성했을 때`().apply {
            // then
            `서비스 모듈은 주입이 안된다`()
        }
    }

    private fun `뷰모델을 생성했을 때`(): FakeViewModel {
        return Robolectric.buildActivity(ArkAppCompatActivity::class.java).create().get()
            .injectViewModel(FakeViewModel::class)
    }

    class FakeViewModel : ViewModel() {
        lateinit var fakeViewModelDependencyNoInject: FakeActivityDependency

        @ArkInject
        lateinit var fakeViewModelDependency: FakeViewModelDependency

        @ArkInject
        lateinit var fakeApplicationDependency: FakeApplicationDependency

        @ArkInject
        lateinit var fakeRetainedActivityDependency: FakeRetainedActivityDependency

        @ArkInject
        lateinit var fakeActivityDependency: FakeActivityDependency

        @ArkInject
        lateinit var fakeServiceDependency: FakeServiceDependency

        fun `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
            assertThat(::fakeViewModelDependency.isInitialized).isTrue
        }

        fun `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
            assertThat(::fakeViewModelDependencyNoInject.isInitialized).isFalse
        }

        fun `어플리케이션 모듈은 주입이 된다`() {
            assertThat(::fakeApplicationDependency.isInitialized).isTrue
        }

        fun `리테인드 액티비티 모듈은 주입이 된다`() {
            assertThat(::fakeRetainedActivityDependency.isInitialized).isTrue
        }

        fun `액티비티 모듈은 주입이 안된다`() {
            assertThat(::fakeActivityDependency.isInitialized).isFalse
        }

        fun `뷰모델 모듈은 주입이 된다`() {
            assertThat(::fakeViewModelDependency.isInitialized).isTrue
        }

        fun `서비스 모듈은 주입이 안된다`() {
            assertThat(::fakeServiceDependency.isInitialized).isFalse
        }
    }
}
