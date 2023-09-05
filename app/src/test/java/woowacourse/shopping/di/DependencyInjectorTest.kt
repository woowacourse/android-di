package woowacourse.shopping.di

import org.junit.Assert.assertNotNull
import org.junit.Test
import woowacourse.shopping.di.DependencyInjector.inject

class DependencyInjectorTest {
    interface TestRepository
    interface TestRepository2

    class TestName
    class TestID
    class TestProduct(
        private val testName: TestName,
        private val testID: TestID
    )

    class TestPerson(
        private val testName: TestName,
    )

    class DefaultTestRepository : TestRepository
    class DefaultTestRepository2 : TestRepository2

    inner class TestViewModel(
        private val testRepository: TestRepository,
        private val testRepository2: TestRepository2,
        private val testProduct: TestProduct
    ) {
        var testPerson: TestPerson? = null
    }

    @Test(expected = IllegalArgumentException::class)
    fun `설정한 의존에 의존이 모두 존재하지 않으면 TestViewModel 생성에 실패한다`() {
        // given
        DependencyInjector.dependencies = object : Dependencies {
            val testRepository: TestRepository by lazy { DefaultTestRepository() }
        }

        // when
        inject<TestViewModel>()

        // then
    }

    @Test
    fun `설정한 의존에 의존이 모두 존재하면 TestViewModel 생성에 성공한다`() {
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
    fun `설정한 의존에 의존이 존재하지 않더라도 의존 관계의 모든 클래스에 생성자가 존재하면 TestViewModel 생성에 성공한다`() {
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

    @Test
    fun `TestViewModel 생성에 성공하면, 필드 의존도 주입된다`() {
        // given
        DependencyInjector.dependencies = object : Dependencies {
            val testRepository: TestRepository by lazy { DefaultTestRepository() }
            val testRepository2: TestRepository2 by lazy { DefaultTestRepository2() }
        }

        // when
        val testViewModel = inject<TestViewModel>()

        // then
        assertNotNull(testViewModel.testPerson)
    }

}
