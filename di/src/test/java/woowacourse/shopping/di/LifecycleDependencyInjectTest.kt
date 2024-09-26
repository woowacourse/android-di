package woowacourse.shopping.di

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LifecycleDependencyInjectTest {

    @Test
    fun `Activity 생명주기를 따르는 의존성은 Activity가 Create된 후 인스턴스화된다`() {

    }

    @Test
    fun `Activity 생명주기를 따르는 의존성은 Activity가 Destroy된 후 인스턴스가 삭제된다`() {

    }

    @Test
    fun `ViewModel의 생명주기를 따르는 의존성은 ViewModel이 생성된 후 인스턴스화된다`() {

    }

    @Test
    fun `ViewModel의 생명주기를 따르는 의존성은 ViewModel이 Clear된 후 인스턴스가 삭제된다`() {

    }

    @Test
    fun `Application의 생명주기를 따르는 의존성은 앱이 종료되기 전까지 인스턴스로 남아있다`() {

    }
}
