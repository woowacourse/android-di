package olive.di

import android.app.Application
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import olive.di.fixture.ChildImpl1
import olive.di.fixture.ChildImpl2
import olive.di.fixture.QualifierTest1
import olive.di.fixture.QualifierTest2
import olive.di.fixture.QualifierTestModule
import org.junit.Test
import kotlin.reflect.KClass

class QualifierTest {
    private val applicationInstance = mockk<Any>()
    private val applicationType = mockk<KClass<out Application>>()

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
