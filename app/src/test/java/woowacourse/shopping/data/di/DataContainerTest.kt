package woowacourse.shopping.data.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.ShoppingDatabase

class DataContainerTest {
    @Test
    fun `등록되지 않은 타입은 null을 반환한다`() {
        val container = DataContainer.testInstanceCreate()
        assertThat(container.getInstanceOrNull(ShoppingDatabase::class)).isNull()
    }

    @Test
    fun `등록된 타입은 provider를 처음 요청할 때 실행하고 인스턴스를 재사용한다`() {
        // given: DataContainer 객체 인스턴스가 생성된다.
        val dao = FakeCartProductDao()
        var providerCallCount = 0

        val container =
            DataContainer.testInstanceCreate(
                CartProductDao::class to {
                    providerCallCount++
                    dao
                },
            )

        // when: 등록된 Dao 객체 생성을 요청하면
        val firstDao = container.getInstanceOrNull(CartProductDao::class)

        // then: provider를 호출하고 새로운 인스턴스를 생성한다.
        assertThat(providerCallCount).isEqualTo(1)
        assertThat(firstDao).isSameInstanceAs(dao)

        // when: 이미 생성된 Dao 객체를 요청하면
        val secondDao = container.getInstanceOrNull(CartProductDao::class)

        // then: provider를 호출하지 않고 기존 인스턴스를 재사용한다.
        assertThat(providerCallCount).isEqualTo(1)
        assertThat(secondDao).isSameInstanceAs(firstDao)
    }

    private class FakeCartProductDao : CartProductDao {
        override suspend fun getAll(): List<CartProductEntity> = emptyList()

        override suspend fun insert(cartProduct: CartProductEntity) = Unit

        override suspend fun delete(id: Long) = Unit
    }
}
