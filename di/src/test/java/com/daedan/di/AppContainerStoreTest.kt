package com.daedan.di

import com.daedan.di.fixture.Child1
import com.daedan.di.fixture.Child2
import com.daedan.di.fixture.CircularDependency1
import com.daedan.di.fixture.CircularDependency2
import com.daedan.di.fixture.NestedDependency
import com.daedan.di.fixture.Parent
import com.daedan.di.fixture.TestViewModel
import com.daedan.di.fixture.UnableReflectObject
import com.daedan.di.qualifier.NamedQualifier
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class AppContainerStoreTest {
    @Test
    fun `instantiate는 등록된 팩토리를 통해 객체를 생성해야 한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val namedQualifier = NamedQualifier(Parent::class)
        val module =
            module(appContainerStore) {
                factory { Parent(Child1(), Child2()) }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual = appContainerStore.instantiate(namedQualifier)
        assertThat(actual).isInstanceOf(Parent::class.java)
    }

    @Test
    fun `중첩 의존성 체인을 성공적으로 해결하고 주입해야 한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                factory { Child1() }
                factory { Child2() }
                factory { Parent(child1 = get(), child2 = get()) }
                factory { NestedDependency(get()) }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual =
            appContainerStore.instantiate(
                NamedQualifier(NestedDependency::class),
            )

        // when
        assertThat(actual).isInstanceOf(NestedDependency::class.java)
    }

    @Test
    fun `한 번 등록한 의존성은 동일 인스턴스를 반환한다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                factory { Child1() }
                factory { Child2() }
                factory { Parent(child1 = get(), child2 = get()) }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual =
            appContainerStore.instantiate(
                NamedQualifier(Parent::class),
            )
        val expected = appContainerStore[Parent::class]

        // then
        assertThat(actual).isSameAs(expected)
    }

    @Test
    fun `AutoViewModel 어노테이션이 붙은 객체는 매번 새로운 인스턴스를 반환한다`() {
        // given
        val appContainerStore = AppContainerStore()

        // when
        val actual =
            appContainerStore.instantiate(
                NamedQualifier(TestViewModel::class),
            )
        val expected = appContainerStore[TestViewModel::class]

        // then
        assertThat(actual).isNotSameAs(expected)
    }

    @Test
    fun `순환 참조가 발생하면 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                factory { CircularDependency1(get()) }
                factory { CircularDependency2(get()) }
                factory { Parent(get(), get()) }
            }
        appContainerStore.registerFactory(module)

        // when - then
        assertThatThrownBy {
            appContainerStore.instantiate(
                NamedQualifier(CircularDependency1::class),
            )
        }.message().contains("순환 참조가 발견되었습니다")
    }

    @Test
    fun `필수 의존성을 해결할 수 없으면 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore()
        val module =
            module(appContainerStore) {
                factory { UnableReflectObject(get()) }
            }
        appContainerStore.registerFactory(module)

        // when = then
        assertThatThrownBy {
            appContainerStore.instantiate(
                NamedQualifier(UnableReflectObject::class),
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
                factory(name = "child1") { child1 }
                factory(name = "child2") { child2 }
            }
        appContainerStore.registerFactory(module)

        // when
        val actual1 = appContainerStore.instantiate(NamedQualifier(Child1::class, "child1"))
        val actual2 = appContainerStore.instantiate(NamedQualifier(Child1::class, "child2"))

        // then
        assertThat(actual1).isSameAs(child1)
        assertThat(actual2).isSameAs(child2)
    }
}
