package woowacourse.shopping

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import shopping.di.DIContainer
import shopping.di.Scope

class DITest {

    @Test
    fun `DI 컨테이너가 의존성을 제대로 주입하는지 테스트`() {
        // 의존성 등록 시 qualifier와 scope를 명시적으로 지정
        DIContainer.register(TestService::class.java, TestServiceImpl(), scope = Scope.APP)
        DIContainer.register(TestService::class.java, AnotherTestServiceImpl(), qualifier = "another", scope = Scope.APP)

        // 의존성을 주입받을 객체 생성
        val testClass = DIContainer.resolve(TestClass::class.java)

        // 주입된 의존성 확인
        assertNotNull(testClass.testService)
        assertEquals("Test Service Implementation", testClass.testService.getMessage())

        assertNotNull(testClass.anotherTestService)
        assertEquals("Another Test Service Implementation", testClass.anotherTestService.getMessage())
    }
}
