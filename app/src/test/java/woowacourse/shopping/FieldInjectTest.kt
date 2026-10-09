package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import smile.di.Provides
import smile.di.SmileDi
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.DeliveryFee
import woowacourse.shopping.ui.cart.CartViewModel

class FieldInjectTest {
    @Test
    fun `CartViewModel에 모듈이 제공한 배송비가 필드 주입된다`() {
        val smileDi = SmileDi(FakeModule(), AppContainer())
        val viewModel = smileDi.createDependency(CartViewModel::class)
        assertThat(viewModel.uiState.value.deliveryFee).isEqualTo(5_000)
    }
}

class FakeModule {
    @Provides
    @RoomDB
    fun provideCartRepository(): CartRepository = InMemoryCartRepository()

    @Provides
    fun provideDeliveryFee(): DeliveryFee = DeliveryFee(5_000)
}
