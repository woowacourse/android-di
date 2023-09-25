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
        injector = Injector(Container(null))
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

            val instance = injector.getCurrentContainer().getInstanceRecursive(dependencyType) ?: throw NullPointerException()
            assertThat(instance).isNotNull
        }
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 하나일 경우 인스턴스 생성에 성공한다`() {
        // given
        val module = 하나의_인터페이스에_대해_하나의_구현체가_있는_모듈()
        injector.addModule(module)

        // when
        val 주입받을인자가한개있는키보드클래스 = injector.inject<주입받을_인자가_한_개_있는_키보드_클래스>(주입받을_인자가_한_개_있는_키보드_클래스::class)

        // then
        assertThat(주입받을인자가한개있는키보드클래스).isNotNull
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 두개일 경우 Qualifier를 사용하지 않으면 잘못된 주입이 발생한다`() {
        // given
        val module = 하나의_인터페이스에_대해_두_개의_구현체가_Qualifier로_구분되지_않는_모듈()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<주입받을_인자가_한_개_있는_키보드_클래스>(주입받을_인자가_한_개_있는_키보드_클래스::class)

        // then
        assertThat(keyboard.housing).isNotExactlyInstanceOf(PlasticHousing::class.java)
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 두개일 경우 Qualifier를 사용하면 알맞게 주입할 수 있다`() {
        // given
        val module = 하나의_인터페이스에_대해_두_개의_구현체가_Qualifier로_구분되는_모듈()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<Qualifier가_한_개_있는_키보드_클래스>(Qualifier가_한_개_있는_키보드_클래스::class)

        // then
        assertThat(keyboard.housing).isInstanceOf(AluminumHousing::class.java)
        assertThat(keyboard.housing).isNotExactlyInstanceOf(PlasticHousing::class.java)
    }

    @Test
    fun `하나의 인터페이스를 구현하는 구현체가 두개일 경우 Qualifier를 사용하면 알맞게 주입할 수 있다2`() {
        // given
        val module = 두_개의_인터페이스에_대해_두_개의_구현체가_Qualifier로_구분되는_모듈()
        injector.addModule(module)

        // when
        val keyboard = injector.inject<Qualifier가_두_개_있는_키보드_클래스>(Qualifier가_두_개_있는_키보드_클래스::class)

        // then
        assertThat(keyboard.housing).isInstanceOf(AluminumHousing::class.java)
        assertThat(keyboard.housing).isNotExactlyInstanceOf(PlasticHousing::class.java)

        assertThat(keyboard.switch).isInstanceOf(BlueSwitch::class.java)
        assertThat(keyboard.switch).isNotExactlyInstanceOf(RedSwitch::class.java)
    }
}
