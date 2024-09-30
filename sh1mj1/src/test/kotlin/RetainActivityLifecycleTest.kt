package com.example.sh1mj1

import com.example.sh1mj1.container.activityscope.DefaultActivityComponentContainer
import com.example.sh1mj1.stub.StubActivity
import com.example.sh1mj1.stub.StubApplication
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApplication::class)
class RetainActivityLifecycleTest {
    @Test
    fun `액티비티가 setup 될 때 dateFormatter 를 외부에서 자동으로 주입해준다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()

        // then
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }

    @Test
    fun `액티비티가 onCreate 되었을 때 dateFormatter 의 인스턴스는 존재한다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(StubActivity::class.java)

        // when
        controller.create()

        // then
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }

    @Test
    fun `액티비티가 pause 되었을 때 dateFormatter 의 인스턴스를 존재한다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(StubActivity::class.java)
        controller.setup()

        // when
        controller.pause()

        // then
        shouldNotThrow<Exception> { controller.get().dateFormatter }
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }

    @Test
    fun `액티비티가 stop 되었을 때 dateFormatter 의 인스턴스는 존재한다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(StubActivity::class.java)
        controller.setup()

        // when
        controller.stop()

        // then
        shouldNotThrow<Exception> { controller.get().dateFormatter }
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }

    @Test
    fun `액티비티가 destory 되었을 때 dateFormatter 의 인스턴스는 존재 삭제된다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(StubActivity::class.java)
        controller.setup()
        val activityContext = controller.get().baseContext
        controller.get().useDateFormatter()

        // when
        controller.destroy()

        // then
        val find =
            DefaultActivityComponentContainer.instance().findComponentInstance(DateFormatter::class, activityContext)
        find shouldBe null
    }
}
