package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.repository.CartRepository

class TargetClass(private val cartRepository: CartRepository)

class AppContainerTest {
    private lateinit var appContainer: AppContainer

    @Before
    fun setup() {
        appContainer = AppContainer()
    }

    @Test
    fun `클래스를 넘기면 해당 클래스의 생성자 프로퍼티를 주입한 인스턴스를 반환해준다`() {
        // given

        // when
        val actual = appContainer.inject(TargetClass::class.java)

        // then
        assertThat(actual).isInstanceOf(TargetClass::class.java)
    }
}
