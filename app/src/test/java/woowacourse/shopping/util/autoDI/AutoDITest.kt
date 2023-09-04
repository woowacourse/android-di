package woowacourse.shopping.util.autoDI

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.util.autoDI.dependencyContainer.DependencyContainer

class AutoDITest {

    @Test
    fun `singleton 객체를 Container에 등록한다`() {
        // when
        val testQualifier = "test"
        AutoDI {
            singleton<CartRepository>("test") { CartRepositoryImpl() }
        }

        // then
        val containerQualifier = DependencyContainer.singletons.values.first().qualifier
        val containerInstanceType =
            DependencyContainer.singletons.values.first().getInstance().javaClass.typeName
        assertThat(containerQualifier).isEqualTo(containerQualifier)
        assertThat(containerInstanceType).isEqualTo(CartRepositoryImpl().javaClass.typeName)
    }

    @Test
    fun `disposable 객체를 Container에 등록한다`() {
        // when
        val testQualifier = "test"
        AutoDI {
            disposable<CartRepository>("test") { CartRepositoryImpl() }
        }

        // then
        val containerQualifier = DependencyContainer.disposables.values.first().qualifier
        val containerInstanceType =
            DependencyContainer.disposables.values.first().getInstance().javaClass.typeName
        assertThat(containerQualifier).isEqualTo(containerQualifier)
        assertThat(containerInstanceType).isEqualTo(CartRepositoryImpl().javaClass.typeName)
    }
}
