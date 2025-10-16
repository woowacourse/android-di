package com.daedan.di

import com.daedan.di.fixture.Child1
import com.daedan.di.fixture.Child2
import com.daedan.di.fixture.CircularDependency1
import com.daedan.di.fixture.CircularDependency2
import com.daedan.di.fixture.ComponentObject1
import com.daedan.di.fixture.ComponentObject2
import com.daedan.di.fixture.ConstructorInjectionWithAnnotation
import com.daedan.di.fixture.ConstructorInjectionWithName
import com.daedan.di.fixture.FieldAndConstructorInjection
import com.daedan.di.fixture.FieldInjection
import com.daedan.di.fixture.FieldInjectionWithAnnotation
import com.daedan.di.fixture.FieldInjectionWithName
import com.daedan.di.fixture.GeneralAnnotation
import com.daedan.di.fixture.NestedDependency
import com.daedan.di.fixture.Parent
import com.daedan.di.fixture.TestComponent1
import com.daedan.di.fixture.TestComponent2
import com.daedan.di.fixture.UnableReflectObject
import com.daedan.di.qualifier.TypeQualifier
import com.daedan.di.util.annotated
import com.daedan.di.util.named
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class AppContainerStoreTest {
    @Test
    fun `instantiate는 등록된 팩토리를 통해 객체를 생성해야 한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val qualifier = TypeQualifier(Parent::class)
        val module =
            module(appContainerStore) {
                single { Parent(Child1(), Child2()) }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual = appContainerStore.instantiate(qualifier)

        // then
        assertThat(actual).isInstanceOf(Parent::class.java)
    }

    @Test
    fun `중첩 의존성 체인을 성공적으로 해결하고 주입해야 한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { Child1() }
                single { Child2() }
                single { Parent(child1 = get(), child2 = get()) }
                single { NestedDependency(get()) }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual =
            appContainerStore.instantiate(
                TypeQualifier(NestedDependency::class),
            )

        // then
        assertThat(actual).isInstanceOf(NestedDependency::class.java)
    }

    @Test
    fun `factory로 등록한 의존성은 매번 다른 인스턴스를 생성한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                factory { Child1() }
            }
        appContainerStore.registerFactory(module)

        // when
        val expected = appContainerStore.instantiate(TypeQualifier(Child1::class))
        val actual = appContainerStore.instantiate(TypeQualifier(Child1::class))

        // then
        assertThat(actual).isNotSameAs(expected)
    }

    @Test
    fun `single로 등록한 의존성은 동일 인스턴스를 반환한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { Child1() }
                single { Child2() }
                single { Parent(child1 = get(), child2 = get()) }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual =
            appContainerStore.instantiate(
                TypeQualifier(Parent::class),
            )
        val expected =
            appContainerStore.instantiate(
                TypeQualifier(Parent::class),
            )

        // then
        assertThat(actual).isSameAs(expected)
    }

    @Test
    fun `순환 참조가 발생하면 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { CircularDependency1(get()) }
                single { CircularDependency2(get()) }
                single { Parent(get(), get()) }
            }

        // when
        appContainerStore.registerFactory(module)

        // then
        assertThatThrownBy {
            appContainerStore.instantiate(
                TypeQualifier(CircularDependency1::class),
            )
        }.message().contains("순환 참조가 발견되었습니다")
    }

    @Test
    fun `필수 의존성을 해결할 수 없으면 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { UnableReflectObject(get()) }
            }

        // when
        appContainerStore.registerFactory(module)

        // then
        assertThatThrownBy {
            appContainerStore.instantiate(
                TypeQualifier(UnableReflectObject::class),
            )
        }.message().contains("주 생성자를 찾을 수 없습니다")
    }

    @Test
    fun `@Component가 없는 어노테이션은 인스턴스를 등록하지 않는다`() {
        // given
        val appContainerStore = AppContainerStore()
        val obj1 = ComponentObject1()

        // when - then
        assertThatThrownBy {
            module(appContainerStore) {
                single(annotated<GeneralAnnotation>()) { obj1 }
            }
        }.message().contains("@Component 어노테이션으로 등록되지 않았습니다")
    }

    @Test
    fun `필드 주입을 수행할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { Child1() }
                single { Child2() }
                single { FieldInjection() }
            }

        // when
        appContainerStore.registerFactory(module)

        // then
        val obj =
            appContainerStore.instantiate(TypeQualifier(FieldInjection::class)) as FieldInjection
        obj.assertPropertyInitialized()
    }

    @Test
    fun `같은 타입을 네이밍으로 구분하여 필드 주입을 수행할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { Child1() }
                single { Child2() }
                single(named("parent1")) { Parent(get(), get()) }
                single(named("parent2")) { Parent(get(), get()) }
                single { FieldInjectionWithName() }
            }

        // when
        appContainerStore.registerFactory(module)

        // then
        val obj =
            appContainerStore.instantiate(
                TypeQualifier(FieldInjectionWithName::class),
            ) as FieldInjectionWithName

        obj.assertPropertyInitialized()
    }

    @Test
    fun `같은 타입을 어노테이션으로 구분하여 필드 주입을 수행할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single(annotated<TestComponent1>()) { ComponentObject1() }
                single(annotated<TestComponent2>()) { ComponentObject2() }
                single { FieldInjectionWithAnnotation() }
            }

        // when
        appContainerStore.registerFactory(module)

        // then
        val obj =
            appContainerStore.instantiate(
                TypeQualifier(FieldInjectionWithAnnotation::class),
            ) as FieldInjectionWithAnnotation

        obj.assertPropertyInitialized()
    }

    @Test
    fun `같은 타입을 네이밍으로 구분하여 생성자 주입을 수행할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { Child1() }
                single { Child2() }
                single { Parent(get(), get()) }
                single { ConstructorInjectionWithName(get(), get()) }
            }

        // when
        appContainerStore.registerFactory(module)

        // then
        assertThatCode {
            appContainerStore.instantiate(
                TypeQualifier(ConstructorInjectionWithName::class),
            ) as ConstructorInjectionWithName
        }.doesNotThrowAnyException()
    }

    @Test
    fun `같은 타입을 어노테이션으로 구분하여 생성자 주입을 수행할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single(annotated<TestComponent1>()) { ComponentObject1() }
                single(annotated<TestComponent2>()) { ComponentObject2() }
                single {
                    ConstructorInjectionWithAnnotation(
                        get(annotated<TestComponent1>()),
                        get(annotated<TestComponent2>()),
                    )
                }
            }
        appContainerStore.registerFactory(module)

        // when - then
        assertThatCode {
            appContainerStore.instantiate(
                TypeQualifier(ConstructorInjectionWithAnnotation::class),
            ) as ConstructorInjectionWithAnnotation
        }.doesNotThrowAnyException()
    }

    @Test
    fun `필드 주입과 생성자 주입을 동시에 수행할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                single { Child1() }
                single { Child2() }
                single(annotated<TestComponent1>()) { ComponentObject1() }
                single(annotated<TestComponent2>()) { ComponentObject2() }
                single(named("parent1")) { Parent(get(), get()) }
                single(named("parent2")) { Parent(get(), get()) }

                single {
                    FieldAndConstructorInjection(
                        get(named("parent1")),
                        get(annotated<TestComponent2>()),
                    )
                }
            }
        // when
        appContainerStore.registerFactory(module)

        // then
        assertThatCode {
            appContainerStore.instantiate(
                TypeQualifier(FieldAndConstructorInjection::class),
            ) as FieldAndConstructorInjection
        }.doesNotThrowAnyException()
    }
}
