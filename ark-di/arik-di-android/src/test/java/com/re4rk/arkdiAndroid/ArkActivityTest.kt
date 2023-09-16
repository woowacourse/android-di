package com.re4rk.arkdiAndroid

import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdiAndroid.fakeDependency.FakeActivityDependency
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

    private fun `액티비티를 생성했을 때`(): FakeActivity {
        return Robolectric.buildActivity(FakeActivity::class.java).create().get()
    }

    class FakeActivity : ArkAppCompatActivity() {
        lateinit var fakeActivityDependencyNoInject: FakeActivityDependency

        @ArkInject
        lateinit var fakeActivityDependency: FakeActivityDependency

        @ArkInject
        lateinit var fakeServiceDependency2: FakeActivityDependency

        fun `@ArkInject 어노테이션이 있는 필드에는 주입이 된다`() {
            assertThat(::fakeActivityDependency.isInitialized).isTrue
        }

        fun `@ArkInject 어노테이션이 없는 필드에는 주입이 안된다`() {
            assertThat(::fakeActivityDependencyNoInject.isInitialized).isFalse
        }
    }
}
