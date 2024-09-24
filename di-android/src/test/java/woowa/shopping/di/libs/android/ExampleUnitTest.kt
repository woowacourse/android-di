package woowa.shopping.di.libs.android

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.startDI

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        startDI {
            androidContext(buildActivity(MyActivity::class.java).create().get())
        }
    }

    @After
    fun tearDown() {
        Containers.clearContainersForTest()
    }

    class MyActivity : ScopeActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }
    }

    @Test
    fun addition_isCorrect() {
        // given
        val activity =
            buildActivity(MyActivity::class.java)
                .create()
                .get()

        // then
        activity.shouldNotBeNull()
    }
}