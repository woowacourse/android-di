package woowacourse.shopping.di

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.harodi.DependencyKey
import com.harodi.DiManager
import com.harodi.ScopeKey
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.repositoryImpl.DefaultCart
import woowacourse.shopping.data.repositoryImpl.InMemoryCartRepository
import woowacourse.shopping.data.repositoryImpl.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DiManagerTest {
    @Test
    fun `ViewModel을 받았을 때 해당 ViewModel이 어떤 클래스인지 알 수 있다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val productsViewModelScope = createViewModelScope(applicationScope)
        val cartViewModelScope = createViewModelScope(applicationScope)

        // when
        val productsViewModel = diManager.resolve(ProductsViewModel::class.java, productsViewModelScope)
        val cartViewModel = diManager.resolve(CartViewModel::class.java, cartViewModelScope)

        // then
        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
        assertThat(cartViewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test
    fun `ViewModel의 의존성을 알맞은 스코프에 저장한다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val viewModelScope = createViewModelScope(applicationScope)
        val productDependencyKey =
            DependencyKey(
                classType = ProductRepository::class.java,
                qualifier = null,
            )
        val cartDependencyKey =
            DependencyKey(
                classType = CartRepository::class.java,
                qualifier = DefaultCart::class,
            )

        // when
        diManager.resolve(ProductsViewModel::class.java, viewModelScope)

        // then
        assertThat(diManager.searchScope(viewModelScope, productDependencyKey)).isInstanceOf(ProductRepository::class.java)
        assertThat(diManager.searchScope(applicationScope, cartDependencyKey)).isInstanceOf(InMemoryCartRepository::class.java)
    }

    @Test
    fun `Inject 애노테이션이 붙은 필드만 주입한다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val viewModelScope = createViewModelScope(applicationScope)

        // when
        val viewModel = diManager.resolve(FieldInjectionTestViewModel::class.java, viewModelScope)

        // then
        assertThat(viewModel.isInjectedRepositoryInitialized()).isTrue()
        assertThat(viewModel.isIgnoredRepositoryInitialized()).isFalse()
    }

    @Test
    fun `다른 ViewModel을 만들어도 ViewModel 객체를 생성할 수 있다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val viewModelScope = createViewModelScope(applicationScope)

        // when
        assertThat(diManager.resolve(TestViewModel::class.java, viewModelScope)).isInstanceOf(TestViewModel::class.java)
    }

    @Test
    fun `DateFormatter는 화면별로 재사용되고 화면 스코프 제거 시 함께 제거된다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val firstScreenScope = createScreenScope(applicationScope)
        val secondScreenScope = createScreenScope(applicationScope)
        val formatterKey = DependencyKey(DateFormatter::class.java, null)
        val contextKey = DependencyKey(Context::class.java, null)
        val applicationContext = RuntimeEnvironment.getApplication()
        diManager.addScopePolicy(DateFormatter::class.java, null, Scope.SCREEN)
        diManager.addScopePolicy(Context::class.java, null, Scope.APPLICATION)
        diManager.addScope(applicationScope, Context::class.java, null, applicationContext)

        // when
        val firstFormatter = diManager.resolve(DateFormatter::class.java, firstScreenScope)
        val repeatedFormatter = diManager.resolve(DateFormatter::class.java, firstScreenScope)
        val secondFormatter = diManager.resolve(DateFormatter::class.java, secondScreenScope)
        diManager.removeScope(firstScreenScope)

        // then
        assertThat(repeatedFormatter).isSameInstanceAs(firstFormatter)
        assertThat(secondFormatter).isNotSameInstanceAs(firstFormatter)
        assertThat(diManager.searchScope(firstScreenScope, formatterKey)).isNull()
        assertThat(diManager.searchScope(secondScreenScope, formatterKey)).isSameInstanceAs(secondFormatter)
        assertThat(diManager.searchScope(applicationScope, contextKey)).isSameInstanceAs(applicationContext)
        assertThat(diManager.resolve(DateFormatter::class.java, firstScreenScope)).isNotSameInstanceAs(firstFormatter)
    }

    private fun createDiManager(): DiManager =
        DiManager().apply {
            addScopePolicy(
                classType = ProductRepository::class.java,
                qualifier = null,
                scopeKind = Scope.VIEW_MODEL,
            )
            addScopePolicy(
                classType = CartRepository::class.java,
                qualifier = DefaultCart::class,
                scopeKind = Scope.APPLICATION,
            )
            addProvider(
                classType = CartRepository::class.java,
                qualifier = DefaultCart::class,
                value = InMemoryCartRepository::class.java,
            )
        }

    private fun createApplicationScope(): ScopeKey =
        ScopeKey(
            parentKey = null,
            scopeKind = Scope.APPLICATION,
        )

    private fun createViewModelScope(applicationScope: ScopeKey): ScopeKey =
        ScopeKey(
            parentKey = applicationScope,
            scopeKind = Scope.VIEW_MODEL,
        )

    private fun createScreenScope(applicationScope: ScopeKey): ScopeKey =
        ScopeKey(
            parentKey = applicationScope,
            scopeKind = Scope.SCREEN,
        )
}
