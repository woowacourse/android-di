package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.fake.FakeCartProductDao
import woowacourse.shopping.repository.CartRepository
import kotlin.reflect.full.createInstance

class TargetClass(val cartRepository: CartRepository)

class AppContainerTest {
    private lateinit var appContainer: AppContainer

    @Before
    fun setup() {
        appContainer = AppContainer()
        appContainer.addProvider(CartProductDao::class, FakeCartProductDao::class::createInstance)
        appContainer.addImplementationClass(CartRepository::class, DefaultCartRepository::class)
    }

    @Test
    fun `클래스를 넘기면 해당 클래스의 생성자 프로퍼티를 주입한 인스턴스를 반환해준다`() {
        // given

        // when
        val actual = appContainer.inject(TargetClass::class.java)

        // then
        assertThat(actual).isInstanceOf(TargetClass::class.java)
    }

    @Test
    fun `서로 다른 TargetClass가 주입받은 cartRepository는 같은 객체이다`() {
        // given
        val targetClass1 = appContainer.inject(TargetClass::class.java)
        val targetClass2 = appContainer.inject(TargetClass::class.java)

        // when
        assertThat(targetClass1).isNotEqualTo(targetClass2)

        // then
        assertThat(targetClass1.cartRepository).isEqualTo(targetClass2.cartRepository)
    }
}
