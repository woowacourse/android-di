package com.dygames.di

import com.dygames.di.DependencyInjector.inject
import com.dygames.di.annotation.Injectable
import com.dygames.di.annotation.Qualifier
import junit.framework.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import kotlin.reflect.typeOf

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
    class TestRepository3(
        @TestRemote val testProductDao: TestProductDao
    )

    interface TestProductDao
    class RemoteTestProductDao : TestProductDao
    class LocalTestProductDao : TestProductDao

    class TestViewModel(
        private val testRepository: TestRepository,
        private val testRepository2: TestRepository2,
        private val testProduct: TestProduct
    ) {
        @Injectable
        var testPerson: TestPerson? = null

        var testProduct2: TestProduct? = null
    }

    @Qualifier
    annotation class TestLocal

    @Qualifier
    annotation class TestRemote

    class TestViewModel2(
        val testRepository3: TestRepository3,
    )

    @Test(expected = IllegalArgumentException::class)
    fun `설정한 의존에 의존이 모두 존재하지 않으면 TestViewModel 생성에 실패한다`() {
        // given
        dependencies {
            provider<TestRepository>(typeOf<DefaultTestRepository>())
        }

        // when
        inject<TestViewModel>()

        // then
    }

    @Test
    fun `설정한 의존에 의존이 모두 존재하면 TestViewModel 생성에 성공한다`() {
        // given
        dependencies {
            provider<TestRepository>(typeOf<DefaultTestRepository>())
            provider<TestRepository2>(typeOf<DefaultTestRepository2>())
            provider {
                TestProduct(
                    TestName(), TestID()
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
        // testProduct의 생성자는 재귀적으로 모두 존재
        dependencies {
            provider<TestRepository>(typeOf<DefaultTestRepository>())
            provider<TestRepository2>(typeOf<DefaultTestRepository2>())
        }

        // when
        val testViewModel = inject<TestViewModel>()

        // then
        assertNotNull(testViewModel)
    }

    @Test
    fun `TestViewModel을 생성 할 때, 필드 의존도 주입어야 생성에 성공한다`() {
        // given
        dependencies {
            provider<TestRepository>(typeOf<DefaultTestRepository>())
            provider<TestRepository2>(typeOf<DefaultTestRepository2>())
        }

        // when
        val testViewModel = inject<TestViewModel>()

        // then
        assertNotNull(testViewModel.testPerson)
    }

    @Test
    fun `필드 의존을 주입 할 때, @Injectable이 선언되지 않은 필드에는 의존이 주입되지 않는다`() {
        // given
        dependencies {
            provider<TestRepository>(typeOf<DefaultTestRepository>())
            provider<TestRepository2>(typeOf<DefaultTestRepository2>())
        }

        // when
        val testViewModel = inject<TestViewModel>()

        // then
        assertNull(testViewModel.testProduct2)
    }

    @Test
    fun `TestViewModel2를 생성 할 때, @Qualifier인 @TestRemote 어노테이션이 선언된 RemoteTestProductDao를 TestRepository3에 주입한다`() {
        // given
        dependencies {
            qualifier(TestLocal()) {
                provider<TestProductDao>(typeOf<LocalTestProductDao>())
            }
            qualifier(TestRemote()) {
                provider<TestProductDao>(typeOf<RemoteTestProductDao>())
            }
        }

        // when
        val testViewModel2 = inject<TestViewModel2>()

        // then
        assertEquals(true, testViewModel2.testRepository3.testProductDao is RemoteTestProductDao)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `TestViewModel2를 생성 할 때, 인터페이스 의존에 @Qualifier가 선언된 객체가 없으면 생성에 실패한다`() {
        // given
        dependencies {
            provider<TestProductDao>(typeOf<LocalTestProductDao>())
        }

        // when
        inject<TestViewModel2>()

        // then
    }
}
