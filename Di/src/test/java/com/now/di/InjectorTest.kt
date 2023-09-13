package com.now.di

import com.now.annotation.Qualifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class InjectorTest {
    @Test
    fun `addModule 함수를 실행하면 모듈에 있는 함수가 실행되어 인스턴스들이 Container에 저장된다`() {
        val module = FakeModule()
        Injector.addModule(module)

        FakeModule::class.declaredFunctions.map {
            val klass = it.returnType.jvmErasure
            val annotation = it.annotations.firstOrNull {
                it.annotationClass.hasAnnotation<Qualifier>()
            }
            val dependencyType = DependencyType(klass, annotation)

            val instance = Container.getInstance(dependencyType) ?: throw NullPointerException()
            assertThat(instance).isNotNull
        }
    }
}
