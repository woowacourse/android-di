package woowacourse

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import woowacourse.shopping.FieldInjector
import woowacourse.shopping.Inject
import woowacourse.shopping.ShoppingContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.jvm.java

private class TestMainVm : ViewModel() {
    @field:Inject
    lateinit var productRepository: ProductRepository
    var title: String = "Main"
}


private class TestBothVm : ViewModel() {
    @field:Inject
    lateinit var productRepository: ProductRepository
    @field:Inject
    lateinit var cartRepository: CartRepository
}

private class FactoryTargetVm : ViewModel() {
    @field:Inject
    lateinit var productRepository: ProductRepository
}

class DiBasicsTests {

    private fun container(): ShoppingContainer =
        ShoppingContainer().apply {
            register(ProductRepository::class) { DefaultProductRepository() }
            register(CartRepository::class) { DefaultCartRepository() }
        }

    @Test
    fun `inject 어노테이션이 붙은 곳만 주입된다`() {
        val vm = TestMainVm()
        FieldInjector.inject(vm, container())

        assertNotNull(vm.productRepository)
        assertTrue(vm.productRepository is DefaultProductRepository)
    }

    @Test
    fun `주입 대상이 없는 필드는 그대로 유지`() {
        val vm = TestMainVm()
        FieldInjector.inject(vm, container())

        assertEquals("Main", vm.title)
    }

    @Test
    fun `컨테이너에 등록 안 된 타입이면 에러가 난다`() {
        val empty = ShoppingContainer()
        val vm = TestMainVm()

        val ex = assertThrows(IllegalStateException::class.java) {
            FieldInjector.inject(vm, empty)
        }

        assertTrue(ex.message!!.contains("ProductRepository"))
    }

    @Test
    fun `여러 필드가 동시에 잘 주입`() {
        val vm = TestBothVm()
        FieldInjector.inject(vm, container())

        assertNotNull(vm.productRepository)
        assertNotNull(vm.cartRepository)
        assertTrue(vm.productRepository is DefaultProductRepository)
        assertTrue(vm.cartRepository is DefaultCartRepository)
    }
}