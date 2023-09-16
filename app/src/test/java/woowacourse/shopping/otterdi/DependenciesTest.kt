package woowacourse.shopping.otterdi

import junit.framework.TestCase.assertNotNull
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
}
