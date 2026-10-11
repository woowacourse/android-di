package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.Scope
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ScopeLifecycleTest {
    @Test
    fun `장바구니 엔트리 스코프는 재사용되고 엔트리가 정리되면 닫힌다`() {
        assertThat(RuntimeEnvironment.getApplication()).isInstanceOf(ShoppingApplication::class.java)
        val viewModelStore = ViewModelStore()
        val viewModelProvider = ViewModelProvider(viewModelStore, HunnitFactory.screenScopeFactory())
        val owner = viewModelProvider.get(ScreenScopeOwner::class.java)

        val first = owner.scope.resolve(DateFormatter::class)

        assertSame(first, owner.scope.resolve(DateFormatter::class))
        assertSame(owner, viewModelProvider.get(ScreenScopeOwner::class.java))

        viewModelStore.clear()

        assertTrue(owner.scope.isClosed)
        assertThrows(IllegalStateException::class.java) { owner.scope.resolve(DateFormatter::class) }
    }

    @Test
    fun `다른 화면 엔트리는 새 DateFormatter를 받고 종료된 ViewModel 스코프를 정리한다`() {
        RuntimeEnvironment.getApplication()
        val firstScreenStore = ViewModelStore()
        val firstScreenOwner =
            ViewModelProvider(firstScreenStore, HunnitFactory.screenScopeFactory())
                .get(ScreenScopeOwner::class.java)
        val firstDateFormatter = firstScreenOwner.scope.resolve(DateFormatter::class)
        val firstViewModelStore = ViewModelStore()
        val firstViewModelProvider =
            ViewModelProvider(firstViewModelStore, HunnitFactory.viewModelFactory(firstScreenOwner.scope))
        val firstViewModel = firstViewModelProvider.get(ProductsViewModel::class.java)
        val firstViewModelScope = firstViewModel.getCloseable<Scope>(VIEW_MODEL_SCOPE_CLOSEABLE_KEY)!!
        val firstProductRepository = firstViewModelScope.resolve(ProductRepository::class)
        val firstCartRepository = firstViewModelScope.resolve(CartRepository::class, RoomCart::class)

        assertSame(firstViewModel, firstViewModelProvider.get(ProductsViewModel::class.java))
        assertSame(firstProductRepository, firstViewModelScope.resolve(ProductRepository::class))

        firstViewModelStore.clear()

        assertTrue(firstViewModelScope.isClosed)
        firstScreenStore.clear()
        assertTrue(firstScreenOwner.scope.isClosed)

        val secondScreenStore = ViewModelStore()
        val secondScreenOwner =
            ViewModelProvider(secondScreenStore, HunnitFactory.screenScopeFactory())
                .get(ScreenScopeOwner::class.java)
        val secondDateFormatter = secondScreenOwner.scope.resolve(DateFormatter::class)
        val secondViewModelStore = ViewModelStore()
        val secondViewModel =
            ViewModelProvider(secondViewModelStore, HunnitFactory.viewModelFactory(secondScreenOwner.scope))
                .get(ProductsViewModel::class.java)
        val secondViewModelScope = secondViewModel.getCloseable<Scope>(VIEW_MODEL_SCOPE_CLOSEABLE_KEY)!!

        assertNotSame(firstDateFormatter, secondDateFormatter)
        assertNotSame(firstProductRepository, secondViewModelScope.resolve(ProductRepository::class))
        assertSame(firstCartRepository, secondViewModelScope.resolve(CartRepository::class, RoomCart::class))

        secondViewModelStore.clear()
        secondScreenStore.clear()
    }
}
