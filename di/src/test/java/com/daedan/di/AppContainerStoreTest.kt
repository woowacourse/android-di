package com.daedan.di

import com.daedan.di.fixture.Child1
import com.daedan.di.fixture.Child2
import com.daedan.di.fixture.CircularDependency1
import com.daedan.di.fixture.CircularDependency2
import com.daedan.di.fixture.ComponentObject1
import com.daedan.di.fixture.ComponentObject2
import com.daedan.di.fixture.GeneralAnnotation
import com.daedan.di.fixture.NestedDependency
import com.daedan.di.fixture.Parent
import com.daedan.di.fixture.TestComponent1
import com.daedan.di.fixture.TestComponent2
import com.daedan.di.fixture.UnableReflectObject
import com.daedan.di.qualifier.AnnotationQualifier
import com.daedan.di.qualifier.NamedQualifier
import com.daedan.di.qualifier.TypeQualifier
import org.assertj.core.api.Assertions.assertThat
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
    fun `viewModel로 등록한 의존성은 매번 다른 인스턴스를 생성한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                viewModel { Child1() }
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
    fun `의존성에 네이밍을 지정하면 같은 타입의 인스턴스를 여러개 등록할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val child1 = Child1()
        val child2 = Child1()
        val module =
            module(appContainerStore) {
                single(named("child1")) { child1 }
                single(named("child2")) { child2 }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual1 = appContainerStore.instantiate(NamedQualifier("child1"))
        val actual2 = appContainerStore.instantiate(NamedQualifier("child2"))

        // then
        assertThat(actual1).isSameAs(child1)
        assertThat(actual2).isSameAs(child2)
    }

    @Test
    fun `의존성에 어노테이션을 지정하면 같은 타입의 인스턴스를 여러개 등록할 수 있다`() {
        // given
        val appContainerStore = AppContainerStore()
        val obj1 = ComponentObject1()
        val obj2 = ComponentObject2()
        val module =
            module(appContainerStore) {
                single(annotated<TestComponent1>()) { obj1 }
                single(annotated<TestComponent2>()) { obj2 }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual1 = appContainerStore.instantiate(AnnotationQualifier(TestComponent1::class))
        val actual2 = appContainerStore.instantiate(AnnotationQualifier(TestComponent2::class))

        // then
        assertThat(actual1).isSameAs(obj1)
        assertThat(actual2).isSameAs(obj2)
    }

    @Test
    fun `@Component가 없는 어노테이션은 인스턴스를 등록하지 않는다`() {
        // given
        val appContainerStore = AppContainerStore()
        val obj1 = ComponentObject1()
        val module =
            module(appContainerStore) {
                single(annotated<GeneralAnnotation>()) { obj1 }
            }
        // when
        appContainerStore.registerFactory(module)

        // then
        val actual = appContainerStore[AnnotationQualifier(GeneralAnnotation::class)]
        assertThat(actual).isNull()
    }
}
