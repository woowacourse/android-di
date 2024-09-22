package com.example.sh1mj1

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.sh1mj1.component.activityscope.activityScopeComponent
import com.example.sh1mj1.component.activityscope.injectedSh1mj1ActivityComponent
import io.kotest.assertions.throwables.shouldNotThrow
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Stub1Application::class)
class RetainActivityLifecycleTest {
    class StubActivity : Activity() {
        val dateFormatter by injectedSh1mj1ActivityComponent<IDateFormatter>()
    }

    interface IDateFormatter

    class StubDateFormatter(context: Context) : LifecycleEventObserver, IDateFormatter {
        override fun onStateChanged(
            source: LifecycleOwner,
            event: Lifecycle.Event,
        ) {
        }
    }

    @Test
    fun `액티비티가 setup 될 때 dateFormatter 를 외부에서 자동으로 주입해준다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()

        // then
        // TODO: 프로퍼티에 접근했을 때 인스턴스가 할당되지 않았다는 예외를 던지는 UninitializedPropertyAccessException 를 던지지 않는다는 assertion 만으로 충분할까?
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }


    @Test
    fun `액티비티가 onCreate 되었을 때 dateFormatter 의 인스턴스는 존재한다`() {
        // given
        val controller = Robolectric
            .buildActivity(StubActivity::class.java)

        // when
        controller.create()

        // then
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }


    @Test
    fun `액티비티가 pause 되었을 때 dateFormatter 의 인스턴스를 존재한다`() {
        // given
        val controller = Robolectric
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
        val controller = Robolectric
            .buildActivity(StubActivity::class.java)
        controller.setup()

        // when
        controller.stop()

        // then
        shouldNotThrow<Exception> { controller.get().dateFormatter }
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }

    // TODO: 액티비티의 구성 변경이 일어나도 dateFormatter 의 인스턴스는 존재한다
//    @Test
//    fun `액티비티의 구성 변경이 일어나도 dateFormatter 의 인스턴스는 존재한다`() {
//        // given
//        val controller = Robolectric
//            .buildActivity(StubActivity::class.java)
//        controller.setup()
//
//        // when
//        controller.configurationChange()
//
//        // then
//        shouldNotThrow<Exception> { controller.get().dateFormatter }
//        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
//    }
//
}

class Stub1Application : DiApplication() {
    override fun onCreate() {
        super.onCreate()

        activityContainer.add(
            activityScopeComponent<RetainActivityLifecycleTest.IDateFormatter>(
                instanceProvider = RetainActivityLifecycleTest::StubDateFormatter,
                qualifier = null,
            ),
        )
    }
}
