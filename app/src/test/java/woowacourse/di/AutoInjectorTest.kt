package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.di.module.DefaultActivityModule
import woowacourse.shopping.di.module.DefaultApplicationModule
import woowacourse.shopping.ui.MainViewModel
import kotlin.reflect.full.isSubclassOf

internal class AutoInjectorTest {
    @Test
    fun `DefaultActivityModule로 MainViewModel을 만들 수 있다`() {
        val activityModule = DefaultActivityModule(DefaultApplicationModule())
        val instance = activityModule.provideInstance(MainViewModel::class.java)
        assertEquals(true, instance::class.isSubclassOf(MainViewModel::class))
    }
}
