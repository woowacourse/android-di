package woowacourse.shopping.util.autoDI

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.util.autoDI.autoDIModule.AutoDIModule

class AutoDITest {

    @Test
    fun `singleton 객체를 Container에 등록한다`() {
        // when
        val testQualifier = "test"
        AutoDI {
            singleton<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val containerQualifier = AutoDIModule.singletons.value.first().qualifier
        val containerInstanceType =
            AutoDIModule.singletons.value.first().getInstance().javaClass.typeName
        assertThat(containerQualifier).isEqualTo(containerQualifier)
        assertThat(containerInstanceType).isEqualTo(CartRepositoryImpl().javaClass.typeName)
    }

    @Test
    fun `disposable 객체를 Container에 등록한다`() {
        // when
        val testQualifier = "test"
        AutoDI {
            disposable<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val containerQualifier = AutoDIModule.disposables.value.first().qualifier
        val containerInstanceType =
            AutoDIModule.disposables.value.first().getInstance().javaClass.typeName
        assertThat(containerQualifier).isEqualTo(containerQualifier)
        assertThat(containerInstanceType).isEqualTo(CartRepositoryImpl().javaClass.typeName)
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
        val injectedInstance = AutoDI.inject<Pig>()

        // then
        val pigName = "멧돼지"
        assertThat(injectedInstance.name).isEqualTo(pigName)
    }
}
