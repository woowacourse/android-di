package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import woowacourse.shopping.dependency.DependencyContainer

class CreatorTest {
    private lateinit var creator: Creator
    private lateinit var injector: Injector
    private lateinit var dependencyContainer: DependencyContainer

    @BeforeEach
    fun setup() {
        injector = Injector()
        creator = injector.creator
        dependencyContainer = injector.dependencyContainer
        dependencyContainer.clear()
    }

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
        dependencyContainer.addInstance(String::class, listOf(), "bixx")
        dependencyContainer.addInstance(Int::class, listOf(), 27)

        val instance: Bixx = creator.createInstance(Bixx::class) as Bixx
        assertThat(instance.age).isEqualTo(27)
        assertThat(instance.name).isEqualTo("bixx")
        assertThat(instance).isEqualTo(dependencyContainer.getInstance(Bixx::class))
    }

    @Test
    fun `target의 생성자 파라미터가 없을 때 Creator가 잘 생성하는지 테스트`() {
        dependencyContainer.addInstance(String::class, listOf(), "sangun")

        val instance = creator.createInstance(Sangun::class) as Sangun
        assertThat(instance.name).isEqualTo("sangun")
        assertThrows<UninitializedPropertyAccessException> { instance.hobby }
        assertThat(instance).isEqualTo(dependencyContainer.getInstance(Sangun::class))
    }
}
