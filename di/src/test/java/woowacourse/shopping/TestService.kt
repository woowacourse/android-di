package woowacourse.shopping

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
