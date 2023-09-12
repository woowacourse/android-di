package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class DependencyContainerTest {
    interface Person {
        val name: String
    }

    class Bixx(override val name: String = "bixx") : Person
    class Sangun(override val name: String = "sangun") : Person
    class MatPig(override val name: String = "matPig") : Person
    class Sopt(
        @Qualifier("MatPig")
        val person: Person,
    )

    class Jason {
        @Inject
        lateinit var role: String
        lateinit var github: String
    }

    @Test
    fun `컨테이너에 넣은 인스턴스가 제대로 반환되는지 테스트`() {
        // given
        val expected = Bixx()

        // when
        DependencyContainer.addInstance(Bixx::class, expected)
        val actual = DependencyContainer.getInstance(Bixx::class)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `여러 구현체가 있을 때 지정한 구현체로 주입되는지 테스트(싱글톤)`() {
        // given
        DependencyContainer.addInstance(Person::class, Bixx())
        DependencyContainer.addInstance(Person::class, Sangun())
        DependencyContainer.addInstance(Person::class, MatPig())

        // when
        val sopt = Injector.injectSingleton<Sopt>(Sopt::class)
        val actual = sopt.person.name

        // then
        assertThat(actual).isEqualTo("matPig")
    }

    @Test
    fun `여러 구현체가 있을 때 지정한 구현체로 주입되는지 테스트`() {
        // given
        DependencyContainer.addInstance(Person::class, Bixx())
        DependencyContainer.addInstance(Person::class, Sangun())
        DependencyContainer.addInstance(Person::class, MatPig())

        // when
        val sopt = Injector.inject<Sopt>(Sopt::class)
        val actual = sopt.person.name

        // then
        assertThat(actual).isEqualTo("matPig")
    }

    @Test
    fun `생성자가 아닌 프로퍼티에 값이 제대로 주입됐는지 테스트(싱글톤)`() {
        // given
        DependencyContainer.addInstance(String::class, "Captain")

        // when
        val jason = Injector.injectSingleton<Jason>(Jason::class)

        // then
        assertAll(
            { assertThat(jason.role).isEqualTo("Captain") },
            { assertThrows<UninitializedPropertyAccessException> { jason.github } },
        )
    }

    @Test
    fun `생성자가 아닌 프로퍼티에 값이 제대로 주입됐는지 테스트`() {
        // given
        DependencyContainer.addInstance(String::class, "Captain")

        // when
        val jason = Injector.inject<Jason>(Jason::class)

        // then
        assertAll(
            { assertThat(jason.role).isEqualTo("Captain") },
            { assertThrows<UninitializedPropertyAccessException> { jason.github } },
        )
    }
}
