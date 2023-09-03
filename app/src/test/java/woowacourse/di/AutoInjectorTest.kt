package woowacourse.di

import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.module.NormalModule
import woowacourse.shopping.di.module.SingletonModule
import woowacourse.shopping.ui.MainViewModel

internal class AutoInjectorTest {
    @Test
    fun `AutoInjector로 MainViewModel을 만들 수 있다`() {
        val autoInjector = AutoInjector(SingletonModule(), NormalModule())
        val instance = autoInjector.inject(MainViewModel::class.java)
        assertEquals(true, instance is MainViewModel)
    }
}
