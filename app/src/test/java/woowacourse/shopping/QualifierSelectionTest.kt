package woowacourse.shopping

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import woowacourse.di.DependencyContainer
import woowacourse.shopping.di.AutoViewModelFactory
import woowacourse.shopping.di.InMemoryCart
import woowacourse.shopping.di.RoomCart
import woowacourse.shopping.domain.repository.CartRepository

class QualifierSelectionTest {
    @Test
    fun `RoomCart와 InMemoryCart Annotation으로 구현체를 선택한다`() {
        val roomRepository = FakeCartRepository()
        val inMemoryRepository = FakeCartRepository()
        val container =
            DependencyContainer().apply {
                registerInstance(CartRepository::class, roomRepository, RoomCart::class)
                registerInstance(CartRepository::class, inMemoryRepository, InMemoryCart::class)
            }
        val factory = AutoViewModelFactory(container)

        val roomViewModel = factory.create(RoomQualifiedCartViewModel::class.java)
        val inMemoryViewModel = factory.create(InMemoryQualifiedCartViewModel::class.java)

        assertThat(roomViewModel.repository).isSameInstanceAs(roomRepository)
        assertThat(inMemoryViewModel.repository).isSameInstanceAs(inMemoryRepository)
    }

    @Test
    fun `구현체가 여러 개인 CartRepository를 무표시로 요청하면 오류가 난다`() {
        val container =
            DependencyContainer().apply {
                registerInstance(CartRepository::class, FakeCartRepository(), RoomCart::class)
                registerInstance(CartRepository::class, FakeCartRepository(), InMemoryCart::class)
            }

        assertThatThrownBy {
            AutoViewModelFactory(container).create(UnqualifiedCartViewModel::class.java)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Qualifier를 지정하세요")
    }
}

class RoomQualifiedCartViewModel(
    @param:RoomCart val repository: CartRepository,
) : ViewModel()

class InMemoryQualifiedCartViewModel(
    @param:InMemoryCart val repository: CartRepository,
) : ViewModel()

class UnqualifiedCartViewModel(
    val repository: CartRepository,
) : ViewModel()
