package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.ChildImpl1
import olive.di.fixture.ChildImpl2
import olive.di.fixture.FieldInjectTest
import olive.di.fixture.Foo
import olive.di.fixture.QualifierTest1
import olive.di.fixture.QualifierTest2
import olive.di.fixture.QualifierTestModule
import org.junit.Test
import kotlin.reflect.KClass

class FieldInjectTest {
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

    @Test
    fun `lateinit var이고 @Inject 어노테이션이 붙은 프로퍼티에 필드 주입을 한다`() {
        // given
        setupDIContainer()
        DIContainer.instance(Foo::class)

        // when
        val actual = DIContainer.instance(FieldInjectTest::class)

        // then
        assertThat(actual.isInitializedInjectFoo()).isTrue()
    }

    @Test
    fun `@Inject 어노테이션이 붙지 않은 lateinit var 프로퍼티에 필드 주입을 하지 않는다`() {
        // given
        setupDIContainer()
        DIContainer.instance(Foo::class)

        // when
        val actual = DIContainer.instance(FieldInjectTest::class)

        // then
        assertThat(actual.isInitializedNotInjectFoo()).isFalse()
    }

    @Test
    fun `@Qualifier 어노테이션으로 인터페이스 구현체를 구분한다1`() {
        // given
        setupDIContainer(modules = listOf(QualifierTestModule::class))

        // when
        val actual = DIContainer.instance(QualifierTest1::class)

        // then
        assertThat(actual.parent).isInstanceOf(ChildImpl1::class.java)
    }

    @Test
    fun `@Qualifier 어노테이션으로 인터페이스 구현체를 구분한다2`() {
        // given
        setupDIContainer(modules = listOf(QualifierTestModule::class))

        // when
        val actual = DIContainer.instance(QualifierTest2::class)

        // then
        assertThat(actual.parent).isInstanceOf(ChildImpl2::class.java)
    }

    private fun setupDIContainer(modules: List<KClass<out DIModule>> = emptyList()) {
        DIContainer.injectApplication(applicationInstance, applicationType)
        DIContainer.injectModules(modules)
    }
}
