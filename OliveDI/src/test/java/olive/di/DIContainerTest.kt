package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.KClass
import io.mockk.mockk
import olive.di.annotation.Inject
import olive.di.annotation.Qualifier

// create test
class Foo

// module test
interface Parent

class Child : Parent

abstract class AbstractTestModule : DIModule {
    abstract fun bindChild(child: Child): Parent
}

class TestModule : DIModule {
    fun bindChild(): Child = Child()
}

// recursive DI test
class Sandwich(olive: Olive)

class Olive

// qualifier test
@Qualifier
annotation class Child1

@Qualifier
annotation class Child2

@Child1
class ChildImpl1 : Parent

@Child2
class ChildImpl2 : Parent

abstract class QualifierTestModule : DIModule {
    @Child1
    abstract fun bindChild1(child1: ChildImpl1): Parent

    @Child2
    abstract fun bindChild2(child2: ChildImpl2): Parent
}

class QualifierTest1 {
    @Child1
    @Inject
    lateinit var parent: Parent
}

class QualifierTest2 {
    @Child2
    @Inject
    lateinit var parent: Parent
}

// field
class FieldInjectTest {
    @Inject
    lateinit var injectFoo: Foo
    lateinit var notInjectFoo: Foo

    fun isInitializedInjectFoo(): Boolean {
        return this::injectFoo.isInitialized
    }

    fun isInitializedNotInjectFoo(): Boolean {
        return this::notInjectFoo.isInitialized
    }
}

class DIContainerTest {
    private lateinit var diContainer: DIContainer
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Test
    fun `instance()로 새로운 인스턴스를 생성해 반환한다`() {
        // given
        diContainer = DIContainer()

        // when
        val actual = diContainer.instance(Foo::class)

        // then
        assertThat(actual).isInstanceOf(Foo::class.java)
    }

    @Test
    fun `singletonInstance()로 기존에 있는 인스턴스를 반환한다`() {
        // given
        diContainer = DIContainer()
        val expected = diContainer.singletonInstance(Foo::class)

        // when
        val actual = diContainer.singletonInstance(Foo::class)

        // then
        assertThat(actual).isSameInstanceAs(expected)
    }

    @Test
    fun `singletonInstance()의 인스턴스가 없는 경우 새로운 인스턴스를 생성해 반환한다`() {
        // given
        diContainer = DIContainer()

        // when
        val actual = diContainer.singletonInstance(Foo::class)

        // then
        assertThat(actual).isInstanceOf(Foo::class.java)
    }

    @Test
    fun `모듈이 abstract class인 경우 함수의 리턴 타입의 인스턴스를 저장한다`() {
        // given
        diContainer = DIContainer(modules = listOf(AbstractTestModule::class))

        // when
        val actual = diContainer.singletonInstance(Parent::class)

        // then
        assertThat(actual).isInstanceOf(Child::class.java)
    }

    @Test
    fun `모듈이 일반 class인 경우 함수의 실행 결과 인스턴스를 저장한다`() {
        // given
        diContainer = DIContainer(modules = listOf(TestModule::class))

        // when
        val actual = diContainer.singletonInstance(Child::class)

        // then
        assertThat(actual).isInstanceOf(Child::class.java)
    }

    @Test
    fun `@Qualifier 어노테이션으로 인터페이스 구현체를 구분한다1`() {
        // given
        diContainer = DIContainer(modules = listOf(QualifierTestModule::class))

        // when
        val actual = diContainer.singletonInstance(QualifierTest1::class)

        // then
        assertThat(actual.parent).isInstanceOf(ChildImpl1::class.java)
    }

    @Test
    fun `@Qualifier 어노테이션으로 인터페이스 구현체를 구분한다2`() {
        // given
        diContainer = DIContainer(modules = listOf(QualifierTestModule::class))

        // when
        val actual = diContainer.singletonInstance(QualifierTest2::class)

        // then
        assertThat(actual.parent).isInstanceOf(ChildImpl2::class.java)
    }

    @Test
    fun `lateinit var이고 @Inject 어노테이션이 붙은 프로퍼티에 필드 주입을 한다`() {
        // given
        diContainer = DIContainer()
        diContainer.singletonInstance(Foo::class)

        // when
        val actual = diContainer.instance(FieldInjectTest::class)

        // then
        assertThat(actual.isInitializedInjectFoo()).isTrue()
    }

    @Test
    fun `@Inject 어노테이션이 붙지 않은 lateinit var 프로퍼티에 필드 주입을 하지 않는다`() {
        // given
        diContainer = DIContainer()
        diContainer.singletonInstance(Foo::class)

        // when
        val actual = diContainer.instance(FieldInjectTest::class)

        // then
        assertThat(actual.isInitializedNotInjectFoo()).isFalse()
    }

    @Test
    fun `Sandwich 객체가 Olive를 필요로 하는 경우 Olive를 생성 후 Sandwich를 생성한다`() {
        // given
        diContainer = DIContainer()

        // when
        val actual = diContainer.singletonInstance(Sandwich::class)

        // then
        assertThat(actual).isInstanceOf(Sandwich::class.java)
        assertThat(diContainer.singletonInstance(Olive::class)).isInstanceOf(Olive::class.java)
    }

    private fun DIContainer(modules: List<KClass<out DIModule>> = emptyList()): DIContainer {
        return DIContainer(
            applicationInstance = applicationInstance,
            applicationType = applicationType,
            diModules = modules,
        )
    }
}
