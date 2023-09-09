package woowacourse.di

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.annotation.FieldInject
import woowacourse.util.getFakeActivityModule
import woowacourse.util.getFakeApplicationModule
import kotlin.reflect.full.isSubclassOf

class FakeViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {
    @FieldInject
    lateinit var productRepository: ProductRepository
}

class FakePrivateFieldInjectViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {
    @FieldInject
    private lateinit var productRepository: ProductRepository
}

internal class AutoInjectorTest {
    @Test
    fun `DefaultActivityModule로 MainViewModel을 만들 수 있다`() {
        // given
        val activityModule = getFakeActivityModule(getFakeApplicationModule())

        // when
        val instance = activityModule.provideInstance(FakeViewModel::class.java)

        // then
        assertEquals(true, instance::class.isSubclassOf(FakeViewModel::class))
        assertNotNull(instance.productRepository)
    }

    @Test
    fun `가시성이 공개되지 않은 필드는 주입받을 수 없다`() {
        // given
        val activityModule = getFakeActivityModule(getFakeApplicationModule())

        // then
        assertThrows(IllegalStateException::class.java) {
            activityModule.provideInstance(FakePrivateFieldInjectViewModel::class.java)
        }
    }
}
