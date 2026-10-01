package woowacourse.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test

@Target(AnnotationTarget.FIELD)
annotation class FieldOnly
annotation class All

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOnly
@Target(AnnotationTarget.CLASS)
annotation class ClassOnly
// 위 Pizza 선언에 size 프로퍼티를 더한다
@All
@ClassOnly
class Pizza(@PropertyOnly val topping: String, @FieldOnly val size: String)
class AnnotationTest {
    @Test
    fun `찾는 경로가 다르다`() {
        // PROPERTY: 코틀린 리플렉션으로만 보인다
        assertThat(Pizza::topping.annotations).isNotEmpty()
        assertThat(Pizza::class.java.getDeclaredField("topping").annotations).isEmpty()
        // FIELD: 자바 필드에 붙는다
        assertThat(Pizza::class.java.getDeclaredField("size").annotations).isNotEmpty()
    }
}

