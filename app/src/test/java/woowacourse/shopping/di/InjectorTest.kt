package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import org.junit.Test
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.annotation.Inject
import woowacourse.shopping.repository.ProductRepository

interface Fake

class Crew(
    @Inject val name: String,
    val nickName: String = "Krrong",
)

class InjectorTest {
    @Test(expected = NullPointerException::class)
    fun `주입하려는 클래스의 주생성자가 없는 경우 NullPointerException을 발생시킨다`() {
        // when & then
        Injector.inject<Fake>(Fake::class)
    }

    @Test
    fun `inject 메서드를 통해 ProductRepository 인스턴스를 생성할 수 있다`() {
        // when
        val repository = Injector.inject<ProductRepository>(DefaultProductRepository::class)

        // then
        assertEquals(repository::class, DefaultProductRepository::class)
    }

    @Test
    fun `Container에 String이 있다면 inject 메서드로 Crew를 인스턴스화 수 있다`() {
        // given
        Container.addInstance(
            String::class,
            "강석진",
        )

        // when
        val crew = Injector.inject<Crew>(Crew::class)

        // then
        assertEquals(crew.name, "강석진")
        assertEquals(crew.nickName, "Krrong")
    }
}
