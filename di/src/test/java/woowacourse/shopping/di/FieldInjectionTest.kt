@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import woowacourse.di.DependencyContainer
import woowacourse.di.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class FieldInjectionTest {
    private val testScopeId = 1L

    class TestViewModel(
        val testRepo1: TestRepo1,
    ) {
        @Inject
        lateinit var testRepo2: TestRepo2
        lateinit var testRepo3: TestRepo3

        fun isTestRepo3Initialized() = ::testRepo3.isInitialized
    }

    class TestRepo1

    class TestRepo2

    class TestRepo3

    @BeforeTest
    fun openScope() {
        DependencyContainer.openScope(testScopeId)
    }

    @AfterTest
    fun closeScope() {
        DependencyContainer.closeScope(testScopeId)
    }

    @Test
    fun `생성자에 필요한 객체가 기존 방식대로 생성된다`() {
        val testViewModel = DependencyContainer.create(TestViewModel::class, testScopeId)

        assertThat(testViewModel).isInstanceOf(TestViewModel::class.java)
    }

    @Test
    fun `Annotation이 붙은 필드만 주입된다`() {
        val testViewModel =
            DependencyContainer.create(TestViewModel::class, testScopeId) as TestViewModel

        assertThat(testViewModel.testRepo1).isInstanceOf(TestRepo1::class.java)
        assertThat(testViewModel.testRepo2).isInstanceOf(TestRepo2::class.java)
        assertThat(testViewModel.isTestRepo3Initialized()).isFalse()
    }
}
