package com.bandal.fullmoon

import junit.framework.TestCase
import org.junit.Test

class FullMoonInjectorTest {

    @Test
    fun `통합테스트로 퉁쳐서 죄송합니다`() {
        // given
        val injector = FullMoonInjector(AppContainer(FakeModule))

        // when
        val injectedClass: FakeClass = injector.inject(FakeClass::class)

        // then
        TestCase.assertNotNull(injectedClass.fakeDataBaseRepository)
        TestCase.assertNotNull(injectedClass.fakeInMemoryRepository)
        TestCase.assertNotNull(injectedClass.fakeDateFormatter)
        TestCase.assertNotNull(injectedClass.fakeDataBaseRepository2)
    }
}
