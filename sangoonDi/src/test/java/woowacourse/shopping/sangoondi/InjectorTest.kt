package woowacourse.shopping.sangoondi

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.sangoondi.fake.Normal
import woowacourse.shopping.sangoondi.fake.Normal2
import woowacourse.shopping.sangoondi.fake.QualifiedClass3
import woowacourse.shopping.sangoondi.fake.RealSingle
import woowacourse.shopping.sangoondi.fake.TestModule
import woowacourse.shopping.sangoondi.fake.TestQualifier3
import kotlin.reflect.full.primaryConstructor

class InjectorTest {
    @Before
    fun setup() {
        `tempModule을 컨테이너에 등록한다`()
    }

    private fun `tempModule을 컨테이너에 등록한다`() {
        DiContainer.setModule(TestModule)
    }

    @Test
    fun `A 인스턴스가 생성된다`() {
        // given : A 클래스
        class A

        // when : inject를 호출하면
        val actual = Injector.inject<A>()

        // then : 해당 타입에 대한 인스턴스가 생성된다.
        assertEquals(true, actual is A)
    }

    @Test
    fun `생성자로 Normal을 갖는 A 인스턴스가 생성된다`() {
        // given : A 클래스는 Normal 생성자를 가지고 있다
        class A(normal: Normal)

        // when : inject를 호출하면
        val actual = Injector.inject<A>()

        // then : 해당 타입에 대한 인스턴스가 생성된다.
        assertEquals(true, actual is A)
    }

    @Test
    fun `생성자로 Normal2을 갖는 A 인스턴스가 생성된다`() {
        // given : Normal2는 Normal 생성자를 가지고 있다
        class A(normal: Normal2)

        // when : inject를 호출하면
        val actual = Injector.inject<A>()

        // then : 해당 타입에 대한 인스턴스가 생성된다.
        assertEquals(true, actual is A)
    }

    @Test
    fun `생성자로 QualifiedClass3을 갖는 A 인스턴스가 생성된다`() {
        // given : QualifiedClass3는 Qualifier를 가지고 있다
        class A(@TestQualifier3 qualifiedClass3: QualifiedClass3)

        // when : inject를 호출하면
        val actual = Injector.inject<A>()

        // then : 해당 타입에 대한 인스턴스가 생성된다.
        assertEquals(true, actual is A)
    }

    @Test
    fun `싱글톤 Single을 호출한다`() {
        // given : RealSingle의 생성자인 Single은 싱글톤이다.
        val params = RealSingle::class.primaryConstructor?.parameters?.map { it }!!

        // when : inject를 호출하면
        Injector.inject<RealSingle>()
        val singleParameter = params[0]
        val actual = Injector.getSingletonIfInstantiate(singleParameter)

        // then : 싱글톤 컨테이너의 벨류를 가져온다
        assertEquals(true, actual != null)
    }
}
