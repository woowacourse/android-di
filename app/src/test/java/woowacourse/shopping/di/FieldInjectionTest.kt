@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FieldInjectionTest {
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

    @Test
    fun `생성자에 필요한 객체가 기존 방식대로 생성된다`() {
        val testViewModel = DependencyContainer.create(TestViewModel::class)

        assertThat(testViewModel).isInstanceOf(TestViewModel::class.java)
    }

    @Test
    fun `Annotation이 붙은 필드만 주입된다`() {
        val testViewModel = DependencyContainer.create(TestViewModel::class) as TestViewModel

        assertThat(testViewModel.testRepo1).isInstanceOf(TestRepo1::class.java)
        assertThat(testViewModel.testRepo2).isInstanceOf(TestRepo2::class.java)
        assertThat(testViewModel.isTestRepo3Initialized()).isFalse()
    }
}
