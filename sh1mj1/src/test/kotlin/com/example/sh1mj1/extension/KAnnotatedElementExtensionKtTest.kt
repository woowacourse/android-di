package com.example.sh1mj1.extension

import com.example.sh1mj1.annotation.Qualifier
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class KAnnotatedElementExtensionKtTest {
    class StubAnnotatedClass(
        @Qualifier("stubAnnotated")
        private val propertyInt: Int = 1,
    )

    @Test
    fun `생성자 파라미터에서 Qualifier 애노테이션을 가진 파라미터의 Qualifier 테스트`() {
        // given
        val kClass = StubAnnotatedClass::class
        val kParameter = kClass.primaryConstructor?.parameters?.first()

        // when
        val qualifier = kParameter?.withQualifier()

        // then
        qualifier shouldBe Qualifier(value = "stubAnnotated")
    }

    class StubAnnotatedClass2 {
        @Qualifier("stubAnnotated")
        private val propertyString: String = "stub"
    }

    @Test
    fun `멤버 프로퍼티에서 Qualifier 애노테이션을 가진 프로퍼티의 Qualifier 테스트`() {
        // given
        val kClass = StubAnnotatedClass2::class
        val kProperty = kClass.memberProperties.first()

        // when
        val qualifier = kProperty.withQualifier()

        // then
        qualifier shouldBe Qualifier(value = "stubAnnotated")
    }
}
