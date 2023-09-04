package woowacourse.shopping.util.autoDI

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import kotlin.reflect.full.starProjectedType

class LifeCycleTypeTest {

    @Test
    fun `LifeCycle 하위 객체 생성시 type 변수값을 생성한다`() {
        // Singleton
        val singleton = LifeCycleType.Singleton<CartRepository> {
            CartRepositoryImpl()
        }
        assertThat(singleton.type).isEqualTo(CartRepository::class.starProjectedType)
        // Disposable
        val disposable = LifeCycleType.Disposable<CartRepository> {
            CartRepositoryImpl()
        }
        assertThat(singleton.type).isEqualTo(CartRepository::class.starProjectedType)
    }
}
