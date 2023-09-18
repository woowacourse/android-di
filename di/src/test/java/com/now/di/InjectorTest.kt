package com.now.di

import com.now.annotation.Qualifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class InjectorTest {
    lateinit var injector: Injector

    @BeforeEach
    fun setup() {
        injector = Injector(null, Container())
    }

    @Test
    fun `addModule 함수를 실행하면 모듈에 있는 함수가 실행되어 인스턴스들이 Container에 저장된다`() {
        // given
        val module = KeyboardModule()

        // when
        injector.addModule(module)

        // then
        KeyboardModule::class.declaredFunctions.map {
            val klass = it.returnType.jvmErasure
            val annotation = it.annotations.firstOrNull {
                it.annotationClass.hasAnnotation<Qualifier>()
            }
            val dependencyType = DependencyType(klass, annotation)

            val instance = injector.getCurrentContainer().getInstance(dependencyType) ?: throw NullPointerException()
            assertThat(instance).isNotNull
        }
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 하나일 경우 인스턴스 생성에 성공한다`() {
        // given
        val module = KeyboardModuleOneInterfaceOneImplement()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<Keyboard>(Keyboard::class)

        // then
        assertThat(keyboard).isNotNull
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 두개일 경우 Qualifier를 사용하지 않으면 잘못된 주입이 발생한다`() {
        // given
        val module = KeyboardModuleOneInterfaceTwoImplementWithOutQualifier()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<Keyboard>(Keyboard::class)

        // then
        assertThat(keyboard.housing).isNotExactlyInstanceOf(PlasticHousing::class.java)
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 두개일 경우 Qualifier를 사용하면 알맞게 주입할 수 있다`() {
        // given
        val module = KeyboardModuleOneInterfaceTwoImplementWithQualifier()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<KeyboardWithQualifier1>(KeyboardWithQualifier1::class)

        // then
        assertThat(keyboard.housing).isInstanceOf(AluminumHousing::class.java)
        assertThat(keyboard.housing).isNotExactlyInstanceOf(PlasticHousing::class.java)
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 두개일 경우 Qualifier를 사용하면 알맞게 주입할 수 있다2`() {
        // given
        val module = KeyboardModuleTwoInterfaceTwoImplementWithQualifier()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<KeyboardWithQualifier2>(KeyboardWithQualifier2::class)

        // then
        assertThat(keyboard.housing).isInstanceOf(AluminumHousing::class.java)
        assertThat(keyboard.housing).isNotExactlyInstanceOf(PlasticHousing::class.java)

        assertThat(keyboard.switch).isInstanceOf(BlueSwitch::class.java)
        assertThat(keyboard.switch).isNotExactlyInstanceOf(RedSwitch::class.java)
    }
}
