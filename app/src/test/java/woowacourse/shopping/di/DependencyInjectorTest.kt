package woowacourse.shopping.di

import org.junit.Assert.assertNotNull
import org.junit.Test
import woowacourse.shopping.di.DependencyInjector.inject

class DependencyInjectorTest {
    internal interface TestRepository
    internal interface TestRepository2

    internal class TestName
    internal class TestID
    internal class TestProduct(
        private val testName: TestName,
        private val testID: TestID
    )

    internal class DefaultTestRepository : TestRepository
    internal class DefaultTestRepository2 : TestRepository2

    internal class TestViewModel(
        private val testRepository: TestRepository,
        private val testRepository2: TestRepository2,
        private val testProduct: TestProduct
    )

    @Test(expected = IllegalStateException::class)
    fun `Singleton에 TestRepository만 존재하면 TestViewModel 생성에 실패한다`() {
        // given
        DependencyInjector.dependencies = object : Dependencies {
            val testRepository: TestRepository by lazy { DefaultTestRepository() }
        }

        // when
        inject<TestViewModel>()

        // then
    }

    @Test
    fun `Singleton에 의존이 모두 존재하면 TestViewModel 생성에 성공한다`() {
        // given
        DependencyInjector.dependencies = object : Dependencies {
            val testRepository: TestRepository by lazy { DefaultTestRepository() }
            val testRepository2: TestRepository2 by lazy { DefaultTestRepository2() }
            val testProduct: TestProduct by lazy {
                TestProduct(
                    TestName(),
                    TestID()
                )
            }
        }

        // when
        val testViewModel = inject<TestViewModel>()

        // then
        assertNotNull(testViewModel)
    }

    @Test
    fun `Singleton에 의존이 존재하지 않더라도 의존 관계의 모든 클래스에 생성자가 존재하면 TestViewModel 생성에 성공한다`() {
        // given
        DependencyInjector.dependencies = object : Dependencies {
            val testRepository: TestRepository by lazy { DefaultTestRepository() }
            val testRepository2: TestRepository2 by lazy { DefaultTestRepository2() }
        }

        // when
        val testViewModel = inject<TestViewModel>()

        // then
        assertNotNull(testViewModel)
    }
}
