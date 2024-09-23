package com.example.sh1mj1.component.activityscope

import com.example.sh1mj1.stub.StubActivity
import com.example.sh1mj1.stub.StubApplication
import com.example.sh1mj1.stub.StubDateFormatter
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApplication::class)
class ActivityScopeComponentFactoryTest {
    @Test
    fun `StubActivity 의 dateFormatter 의 구현체 타입을 확인한다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(StubActivity::class.java)
                .setup()
                .get()

        // when
        val factory = injectActivityScopeComponent<DateFormatter>()

        // then
        val dateFormatter =
            factory.getValue(
                thisRef = activity,
                property = StubActivity::dateFormatter,
            )
        dateFormatter.shouldBeInstanceOf<StubDateFormatter>()
    }
}
