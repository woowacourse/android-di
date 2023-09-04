package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.module.DefaultActivityModule
import woowacourse.shopping.di.module.DefaultApplicationModule
import woowacourse.shopping.ui.MainViewModel
import kotlin.reflect.full.isSubclassOf

internal class AutoInjectorTest {
    @Test
    fun `AutoInjector로 MainViewModel을 만들 수 있다`() {
        val autoInjector = AutoInjector(DefaultActivityModule(DefaultApplicationModule()))
        val instance = autoInjector.inject(MainViewModel::class.java)
        assertEquals(true, instance::class.isSubclassOf(MainViewModel::class))
    }
}
