package woowacourse.shopping.util.autoDI

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import kotlin.reflect.full.isSubclassOf

class InjectorTest {
    @Test
    fun `singleton으로 등록된 객체를 프로퍼티 위임을 통해 주입받는다(qualifier 받지않는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            singleton<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val cartRepository: CartRepository by Injector()
        assertThat(cartRepository::class.isSubclassOf(CartRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `singleton으로 등록된 객체를 프로퍼티 위임을 통해 주입받는다(qualifier 받는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            singleton<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val cartRepository: CartRepository by Injector(testQualifier)
        assertThat(cartRepository::class.isSubclassOf(CartRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `disposable 객체를 Container에 등록한다(qualifier 받지않는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            disposable<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val cartRepository: CartRepository by Injector()
        assertThat(cartRepository::class.isSubclassOf(CartRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `disposable 객체를 Container에 등록한다(qualifier 받는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            disposable<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val cartRepository: CartRepository by Injector(testQualifier)
        assertThat(cartRepository::class.isSubclassOf(CartRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `String값 "멧돼지"를 주입받는 Pig 객체를 inject함수를 통해 주입받는다`() {
        // given
        class Pig(val name: String)
        AutoDI {
            disposable { Pig(inject("pigName")) }
            disposable("pigName") { "멧돼지" }
        }

        // when
        val injectedInstance: Pig by Injector()

        // then
        val pigName = "멧돼지"
        Truth.assertThat(injectedInstance.name).isEqualTo(pigName)
    }
}
