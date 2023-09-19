package woowacourse.shopping.otterdi

import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertNotEquals
import org.junit.Test
import woowacourse.shopping.otterdi.annotation.Inject

internal class DependenciesTest {

    interface Dependency
    class DefaultDependency : Dependency

    class RecursiveDependency(@Inject val dependency: Dependency)
    class RecursiveTestViewModel(@Inject val recursiveDependency: RecursiveDependency)
    object RecursiveTestModule : Module {
        fun provideDefaultDependency(): Dependency = DefaultDependency()
        fun provideRecursiveDependency(@Inject dependency: Dependency): RecursiveDependency =
            RecursiveDependency(dependency)
    }

    @Test
    fun `필요한 의존성은 재귀로 초기화된다`() {
        val injector = Injector(RecursiveTestModule)
        val recursiveTestViewModel = injector.inject<RecursiveTestViewModel>()

        assertNotNull(recursiveTestViewModel.recursiveDependency)
    }

    class InstanceTestViewModel1(@Inject val dependency: Dependency)
    class InstanceTestViewModel2(@Inject val dependency: Dependency)
    object InstanceTestModule : Module {
        fun provideDefaultDependency(): Dependency = DefaultDependency()
    }

    @Test
    fun `의존성은 기본적으로 매번 새로 생성되어 주입한다`() {
        // given
        val injector = Injector(InstanceTestModule)

        // when
        val viewModel1 = injector.inject<InstanceTestViewModel1>()
        val viewModel2 = injector.inject<InstanceTestViewModel2>()

        // then
        assertNotEquals(viewModel1.hashCode(), viewModel2.hashCode())
    }
}
