package woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class FieldInjectorTest {
    class TestService

    private class Target {
        @InjectField
        lateinit var testService: TestService

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
        val injectedService =
            target::class
                .declaredMemberProperties
                .first { it.name == "testService" }
                .apply { isAccessible = true }
                .getter
                .call(target)

        assertThat(injectedService).isInstanceOf(TestService::class.java)
        assertThat(target.untouched).isEqualTo("keep")
    }

    @Test(expected = IllegalStateException::class)
    fun `컨테이너에_바인딩이_없으면_주입은_실패한다`() {
        // given
        val container = Container()
        val target = Target()

        // when
        FieldInjector.inject(target, container)

        // then (예외 발생)
        assertThatThrownBy { FieldInjector.inject(target, container) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("provider")
    }
}
