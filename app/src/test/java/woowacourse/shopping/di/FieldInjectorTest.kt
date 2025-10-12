package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.di.auto.Container
import woowacourse.shopping.di.auto.FieldInjector
import woowacourse.shopping.di.auto.InjectField
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class FieldInjectorTest {
    class TestService

    private class Target {
        @InjectField
        lateinit var service: TestService

        val untouched: String = "keep"
    }

    @Test
    fun `InjectField가_붙은_필드에_의존성이_주입된다`() {
        // given
        val container =
            Container().apply {
                bindSingleton(TestService::class) { TestService() }
            }
        val target = Target()

        // when
        FieldInjector.inject(target, container)

        // then
        assertThat(this::class).isNotNull()
        assertThat(::Target).isNotNull()
        assertThat(
            target::class
                .declaredMemberProperties
                .first { it.name == "service" }
                .apply { isAccessible = true }
                .getter
                .call(target),
        ).isInstanceOf(TestService::class.java)
        assertThat(target.untouched).isEqualTo("keep")
    }

    @Test(expected = IllegalStateException::class)
    fun `컨테이너에_바인딩이_없으면_주입은_실패한다`() {
        // given
        val container = Container()
        val target = Target()

        // when
        FieldInjector.inject(target, container)

        // then
        assertThat(this::class)
    }
}
