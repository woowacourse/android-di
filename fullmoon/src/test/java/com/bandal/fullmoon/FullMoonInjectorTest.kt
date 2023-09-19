package com.bandal.fullmoon

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class FullMoonInjectorTest {

    private val fakeAppContainer: AppContainer = TestAppContainer
    private val injector = FullMoonInjector(fakeAppContainer)

    @Test
    fun `주생성자 안에 하나의 인터페이스와 구현체 하나가 있을 때 주입할 수 있다`() {
        // given

        // when
        val injectedClass: FakeImplementWithQualifierDatabase =
            injector.inject(FakeImplementWithQualifierDatabase::class)

        // then
        assertNotNull(injectedClass.fakeLocalDataSource)
    }

    @Test
    fun `같은 인터페이스를 구현하는 두 구현체를 주입시킬 수 있다`() {
        // given

        // when
        val injectedClass = injector.inject(FakeClass::class)

        // then
        assertNotNull(injectedClass.fakeDataBaseRepository)
        assertNotNull(injectedClass.fakeInMemoryRepository)
        assertNotNull(injectedClass.fakeDateFormatter)
        assertNotNull(injectedClass.fakeDataBaseRepository2)

        assertEquals(injectedClass.fakeDataBaseRepository2, injectedClass.fakeDataBaseRepository)
    }
}
