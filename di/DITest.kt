package woowacourse

import androidx.lifecycle.ViewModel
import com.example.di.FieldInjector
import com.example.di.Inject
import com.example.di.ShoppingContainer
import com.example.domain.repository.CartRepository
import com.example.domain.repository.ProductRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.DefaultRoomCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import kotlin.jvm.java

class FakeCartProductDao : CartProductDao {
    private val items = mutableListOf<CartProductEntity>()

    override suspend fun insert(entity: CartProductEntity) {
        items += entity
    }

    override suspend fun getAll(): List<CartProductEntity> = items.toList()

    override suspend fun delete(id: Long) {
        items.removeIf { it.id == id }
    }
}

class DiBasicsTests {
    private fun container(): ShoppingContainer =
        ShoppingContainer().apply {
            register(CartProductDao::class) { FakeCartProductDao() }
            register(ProductRepository::class) { DefaultProductRepository() }
            register(CartRepository::class) { DefaultRoomCartRepository(get(CartProductDao::class)) }
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

        val ex =
            assertThrows(IllegalStateException::class.java) {
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
        assertTrue(vm.cartRepository is DefaultRoomCartRepository)
    }
}
