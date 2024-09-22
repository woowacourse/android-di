package com.example.sh1mj1

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.sh1mj1.component.ActivityComponent
import com.example.sh1mj1.component.injectedSh1mj1ActivityComponent
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
        shouldNotThrow<UninitializedPropertyAccessException> { controller.get().dateFormatter }
    }

//
//    @Test
//    fun `액티비티가 onCreate 되었을 때 dateFormatter 의 인스턴스는 존재한다`() {
//        // given
//        val controller = Robolectric
//            .buildActivity(StubActivity::class.java)
//            .setup()
//
//        val context = controller.get()
//        val dateFormatter = StubDateFormatter(context)
//
//
//        // when
//
//        // TODO: 어디선가에서 dateFormatter를 주입해준다.
//        controller.get().dateFormatter = dateFormatter
//
//
//        // then
//
//    }
//
//
//    @Test
//    fun `DateFormatter 는 액티비티 라이프 사이클에 따른다`() {
//        // given
//        val controller = Robolectric
//            .buildActivity(StubActivity::class.java)
//            .setup()
//
//        // baseContext?
//        val context = controller.get()
//        val dateFormatter = StubDateFormatter(context)
//        controller.get().dateFormatter = StubDateFormatter(context)
//
//        // when
//        controller.get().dateFormatter.isCleanedUp = false
//
//        // // Activity가 Created, Started, Resumed된 상태일 때 의존성 주입 필요 마킹된 필드에 인스턴스가 존재한다.
//
//
//        //// Activity가 Destroyed된 상태일 때 의존성 주입된 필드 인스턴스가 제거된다.
//
//
//        // then
//
//    }
//
//
}

class Stub1Application : DiApplication() {
    override fun onCreate() {
        super.onCreate()

        activityContainer.add(
            ActivityComponent(
                injectedClass = RetainActivityLifecycleTest.IDateFormatter::class,
                instanceProvider = { context: Context -> RetainActivityLifecycleTest.StubDateFormatter(context) },
                qualifier = null,
            ),
        )
    }
}
