package woowacourse.shopping.sangoondi

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.sangoondi.fake.RealSingle
import woowacourse.shopping.sangoondi.fake.TestModule

class DiContainerTest {

    @Before
    fun setup() {
        `tempModule을 컨테이너에 등록한다`()
    }

    @Test
    fun qualifiedInstanceTest() {
        // given : 모듈이 등록 되면
        // when : 모듈을 순회하며, @Qualifier의 어노테이션들을 찾아 qualifiedInstance에 추가한다
        val actual = DiContainer.qualifiedInstance

        // then :
        assertEquals(3, actual.size)
    }

    @Test
    fun instanceTest() {
        // given : 모듈이 등록 되면
        // when : 모듈을 순회하며, @Qualifier의 어노테이션이 없으면 instance에 추가한다
        val actual = DiContainer.instance

        // then :
        assertEquals(4, actual.size)
    }

    @Test
    fun singletonInstanceTest() {
        // given : RealSingle의 생성자인 Single은 싱글톤이다.
        class A(realSingle: RealSingle)

        // when : inject를 호출하면
        Injector.inject<A>()
        val actual = DiContainer.singletonInstance

        // then : 해당 타입에 대한 인스턴스가 생성된다.
        assertEquals(1, actual.size)
    }

    private fun `tempModule을 컨테이너에 등록한다`() {
        DiContainer.setModule(TestModule)
    }
}
