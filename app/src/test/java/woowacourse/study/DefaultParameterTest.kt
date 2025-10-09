package woowacourse.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

class DefaultParameterTest {
    @Test
    fun `KClass#primaryConstructor는 파라미터 기본값을 사용하지 않는 생성자를 반환한다`() {
        val primaryConstructor: KFunction<DefaultParameterClass>? =
            DefaultParameterClass::class.primaryConstructor

        assertThat(primaryConstructor?.parameters?.size).isEqualTo(2)
    }
}

private class DefaultParameterClass(
    val param: Int,
    val paramWithDefaultParameter: Int = 20,
)
