package com.example.sh1mj1.extension

import com.example.sh1mj1.annotation.Qualifier
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.reflect.full.memberProperties

class KPropertyExtensionKtTest {
    class StubAnnotatedClass {
        @Qualifier("stubAnnotated")
        private val property: String = "stub"
    }

    @Test
    fun `withQualifier test`() {
        // given
        val kClass = StubAnnotatedClass::class
        val kProperty = kClass.memberProperties.first()

        // when
        val qualifier = kProperty.withQualifier()

        // then
        qualifier shouldBe Qualifier(value = "stubAnnotated")
    }
}
