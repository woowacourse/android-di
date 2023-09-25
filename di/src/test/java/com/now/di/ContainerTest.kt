package com.now.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

class ContainerTest {
    private lateinit var container: Container

    @BeforeEach
    fun setup() {
        container = Container(null)
    }

    @Test
    fun `provide 함수가 있는 모듈을 컨테이너에 추가할 수 있다`() {
        // given
        val module = 하나의_인터페이스에_대해_하나의_구현체가_있는_모듈()

        module::class.declaredFunctions.forEach {
            container.addInstance(module, it)
        }

        // when & then
        val klass = module::class.declaredFunctions.first().returnType.jvmErasure
        val dependencyType = DependencyType(klass, null)
        assertThat(container.getInstanceRecursive(dependencyType)).isNotNull
    }

    @Test
    fun `provide 함수가 재귀적 호출이 필요한 경우에도 컨테이너에 추가할 수 있다`() {
        // given
        val module = 재귀적_호출이_필요한_모듈()

        module::class.declaredFunctions.forEach {
            container.addInstance(module, it)
        }

        // when & then
        module::class.declaredFunctions.forEach { kFunction ->
            val klass = kFunction.returnType.jvmErasure
            val dependencyType = DependencyType(klass, null)
            assertThat(container.getInstanceRecursive(dependencyType)).isNotNull
        }
    }

    @Test
    fun `자식 컨테이너에 없는 경우 부모 컨테이너에서 값을 가져온다`() {
        // given
        val module = 재귀적_호출이_필요한_모듈()
        val childContainer = Container(parentContainer = container)

        module::class.declaredFunctions.forEach {
            container.addInstance(module, it)
        }

        // when & then
        module::class.declaredFunctions.forEach { kFunction ->
            val klass = kFunction.returnType.jvmErasure
            val dependencyType = DependencyType(klass, null)
            assertThat(childContainer.getInstanceRecursive(dependencyType)).isNotNull
        }
    }
}
