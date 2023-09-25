package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreatorTest {
    class Bixx(val age: Int) {
        @Inject
        lateinit var name: String
    }

    class Sangun() {
        @Inject
        lateinit var name: String
        lateinit var hobby: String
    }

    @Test
    fun `Creator가 target을 잘 생성하는지 테스트`() {
        val creator = Injector().creator
        creator.dependencyContainer.addInstance(String::class, listOf(), "bixx")
        creator.dependencyContainer.addInstance(Int::class, listOf(), 27)

        val instance: Bixx = creator.createInstance(Bixx::class) as Bixx
        assertThat(instance.age).isEqualTo(27)
        assertThat(instance.name).isEqualTo("bixx")
        assertThat(instance).isEqualTo(creator.dependencyContainer.getInstance(Bixx::class))
    }

    @Test
    fun `target의 생성자 파라미터가 없을 때 Creator가 잘 생성하는지 테스트`() {
        val creator = Injector().creator
        creator.dependencyContainer.addInstance(String::class, listOf(), "sangun")

        val instance = creator.createInstance(Sangun::class) as Sangun
        assertThat(instance.name).isEqualTo("sangun")
        assertThrows<UninitializedPropertyAccessException> { instance.hobby }
        assertThat(instance).isEqualTo(creator.dependencyContainer.getInstance(Sangun::class))
    }
}
