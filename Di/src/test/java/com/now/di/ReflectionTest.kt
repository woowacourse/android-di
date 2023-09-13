package com.now.di

import com.now.annotation.Qualifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

class ReflectionTest {
    @Test
    fun `모듈에 있는 함수를 실행하여 인스턴스를 생성할 수 있다`() {
        // given
        val module = KeyboardModule()
        val kFunctions = module::class.declaredFunctions

        // when
        val instances = kFunctions.map { it.call(module) }

        // then
        assertThat(instances.size).isEqualTo(2)
        assertThat(kFunctions.size).isEqualTo(2)
    }

    @Test
    fun `모듈에 있는 함수의 애노테이션을 가져올 수 있다`() {
        // given
        val annotations = mutableListOf<Annotation>()
        val module = KeyboardModule()
        val kFunctions = module::class.declaredFunctions

        // when
        kFunctions.forEach { kFunction ->
            kFunction.annotations.forEach { annotation ->
                annotations.add(annotation)
            }
        }

        // then
        assertThat(annotations.size).isEqualTo(1)
    }

    @Test
    fun `모듈에 있는 함수에 Qualifier 애노테이션이 붙어있는 애노테이션이 있는지 확인할 수 있다`() {
        // given
        val annotations = mutableListOf<Boolean>()
        val module = KeyboardModule()
        val kFunctions = module::class.declaredFunctions

        // when
        kFunctions.forEach { kFunction ->
            kFunction.annotations.forEach { annotation ->
                annotations.add(annotation.annotationClass.hasAnnotation<Qualifier>())
            }
        }

        // then
        assertThat(annotations.size).isEqualTo(1)
        assertThat(annotations[0]).isTrue
    }
}
