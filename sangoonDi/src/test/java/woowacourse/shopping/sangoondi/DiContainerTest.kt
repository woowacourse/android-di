package woowacourse.shopping.sangoondi

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.sangoondi.annotation.Qualifier
import woowacourse.shopping.sangoondi.annotation.Singleton

class DiContainerTest {

    @Before
    fun setup() {
        `tempModule을 컨테이너에 등록한다`()
    }

    private fun `tempModule을 컨테이너에 등록한다`() {
        DiContainer.setModule(TestModule)
    }

    @Test
    fun `모듈이 등록되면, modules에 모듈이 추가된다`() {
        // given :
        // when :
        val actual = DiContainer.modules

        // then :
        assertEquals(1, actual.size)
    }

    @Test
    fun `같은 모듈은 등록되지 않는다`() {
        // given :
        // when :

        DiContainer.setModule(TestModule)
        val actual = DiContainer.modules

        // then :
        assertEquals(true, actual.size == 1)
    }

    @Test
    fun `모듈이 등록되면, @Singleton 어노테이션만 붙은 인스턴스를 추가한다`() {
        // given :
        // when :
        val actual = DiContainer.singletons
        // then :
        assertEquals(1, actual.size)
    }

    @Test
    fun `모듈이 등록되면, @Singleton, @Qualifier 어노테이션이 붙은 인스턴스를 추가한다`() {
        // given :
        // when :
        val actual = DiContainer.qualifiedSingletons
        // then :
        assertEquals(2, actual.size)
    }

    class Temp
    companion object TestModule {
        fun provide(): Temp = Temp()

        @Singleton
        fun provideSingletonInstance(): Temp = Temp()

        @Qualifier
        fun provideQualifiedInstance(): Temp = Temp()

        @Singleton
        @Qualifier
        fun provideQualifiedSingletonInstance1(): Temp = Temp()

        @Qualifier
        @Singleton
        fun provideQualifiedSingletonInstance2(): Temp = Temp()
    }
}
