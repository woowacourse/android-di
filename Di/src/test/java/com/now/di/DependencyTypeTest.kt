package com.now.di

import com.now.annotation.Qualifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class DependencyTypeTest {
    @Test
    fun `Keyboard의 Qualifier가 붙어있는 인자로부터 DependencyType을 만들면 null이 아니다`() {
        KeyBoard::class.primaryConstructor?.parameters?.map {
            if (it.hasAnnotation<Qualifier>()) {
                val dependencyType = DependencyType(it.type.jvmErasure, it.findAnnotation<Qualifier>()!!)
                assertThat(dependencyType).isNotNull
            }
        }
    }
}
