package woowacourse.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AnnotationTest {
    @Target(AnnotationTarget.PROPERTY)
    annotation class PropertyOnly

    @Target(AnnotationTarget.FIELD)
    annotation class FieldOnly

    class Pizza(@PropertyOnly val topping: String, @FieldOnly val size: String)

    @Target(AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.SOURCE)
    annotation class SourceOnly

    @Target(AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.BINARY)
    annotation class BinaryOnly

    @Target(AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RuntimeOnly

    class Ham(@SourceOnly val source: String, @BinaryOnly val binary: String, @RuntimeOnly val runtime: String)

    @Test
    fun `property 어노테이션은 코틀린 프로퍼티에서 찾을 수 있다`() {
        assertThat(Pizza::topping.annotations).isNotEmpty()
    }

    @Test
    fun `property 어노테이션은 자바 필드에서 찾을 수 없다`() {
        assertThat(Pizza::class.java.getDeclaredField("topping").annotations).isEmpty()
    }

    @Test
    fun `field 어노테이션은 코틀린 프로퍼티에서 찾을 수 없다`() {
        assertThat(Pizza::size.annotations).isEmpty()
    }

    @Test
    fun `field 어노테이션은 자바 필드에서 찾을 수 있다`() {
        assertThat(Pizza::class.java.getDeclaredField("size").annotations).isNotEmpty()
    }

    @Test
    fun `SOURCE로 유지되는 어노테이션은 실행 중에 조회할 수 없다`() {
        assertThat(Ham::source.annotations).isEmpty()
    }

    @Test
    fun `BINARY로 유지되는 어노테이션은 실행 중에 조회할 수 없다`() {
        assertThat(Ham::binary.annotations).isEmpty()
    }

    @Test
    fun `RUNTIME으로 유지되는 어노테이션은 실행 중에 조회할 수 있다`() {
        assertThat(Ham::runtime.annotations).isNotEmpty()
    }
}
