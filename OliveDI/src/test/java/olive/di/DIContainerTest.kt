package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.AbstractTestModule
import olive.di.fixture.Child
import olive.di.fixture.ChildImpl1
import olive.di.fixture.ChildImpl2
import olive.di.fixture.FieldInjectTest
import olive.di.fixture.Foo
import olive.di.fixture.Olive
import olive.di.fixture.Parent
import olive.di.fixture.QualifierTest1
import olive.di.fixture.QualifierTest2
import olive.di.fixture.QualifierTestModule
import olive.di.fixture.Sandwich
import olive.di.fixture.TestModule
import org.junit.Test
import kotlin.reflect.KClass

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
