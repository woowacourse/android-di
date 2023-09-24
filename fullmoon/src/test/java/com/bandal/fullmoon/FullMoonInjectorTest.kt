package com.bandal.fullmoon

import junit.framework.TestCase
import org.junit.Test

class FullMoonInjectorTest {

    @Test
    fun `FullMoonInjector를 통해 FakeClass에 인스턴스를 주입시킬 수 있다`() {
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
