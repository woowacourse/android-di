package woowacourse.shopping

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertSame
import shopping.di.DIContainer
import shopping.di.Scope
import woowacourse.shopping.data.fake.FakeCartRepository
import woowacourse.shopping.data.fake.FakeProductRepository
import woowacourse.shopping.data.fake.ICartRepository
import woowacourse.shopping.data.fake.IProductRepository
import woowacourse.shopping.ui.MainViewModel

class ViewModelScopeTest {
    @Test
    fun `ViewModel 스코프에서 의존성이 유지되고 제거되는지 테스트`() {
        // Given
        DIContainer.register(
            clazz = ICartRepository::class.java,
            instance = FakeCartRepository(),
            scope = Scope.APP,
        )

        val viewModel = MainViewModel()

        // Fake 객체 등록
        DIContainer.register(
            clazz = IProductRepository::class.java,
            instance = FakeProductRepository(),
            scope = Scope.VIEWMODEL,
            owner = viewModel,
        )

        // 의존성 주입
        DIContainer.injectFields(viewModel)

        // When
        val productRepository1 =
            DIContainer.resolve(
                IProductRepository::class.java,
                scope = Scope.VIEWMODEL,
                owner = viewModel,
            )

        // Then
        val productRepository2 =
            DIContainer.resolve(
                IProductRepository::class.java,
                scope = Scope.VIEWMODEL,
                owner = viewModel,
            )

        // 동일한 인스턴스인지 확인
        assertSame(productRepository1, productRepository2)

        // ViewModel의 onCleared 호출하여 스코프 제거
        viewModel.onCleared()

        // 스코프 제거 후 의존성 요청 시 예외 발생 확인
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            DIContainer.resolve(
                IProductRepository::class.java,
                scope = Scope.VIEWMODEL,
                owner = viewModel,
            )
        }
    }
}
