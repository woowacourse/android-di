package woowacourse.shopping

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import shopping.di.DIContainer
import shopping.di.Inject
import shopping.di.Qualifier

interface TestService {
    fun getMessage(): String
}

class TestServiceImpl : TestService {
    override fun getMessage(): String = "Test Service Implementation"
}

class AnotherTestServiceImpl : TestService {
    override fun getMessage(): String = "Another Test Service Implementation"
}

class TestClass {

    @Inject
    lateinit var testService: TestService

    @Inject
    @Qualifier("another")
    lateinit var anotherTestService: TestService
}

class DITest {

    @Test
    fun `DI 컨테이너가 의존성을 제대로 주입하는지 테스트`() {
        DIContainer.register(TestService::class.java, TestServiceImpl())
        DIContainer.register(TestService::class.java, AnotherTestServiceImpl(), "another")

        val testClass = DIContainer.resolve(TestClass::class.java)

        assertNotNull(testClass.testService)
        assertEquals("Test Service Implementation", testClass.testService.getMessage())

        assertNotNull(testClass.anotherTestService)
        assertEquals(
            "Another Test Service Implementation",
            testClass.anotherTestService.getMessage()
        )
    }
}
