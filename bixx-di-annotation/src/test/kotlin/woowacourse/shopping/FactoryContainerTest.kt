package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FactoryContainerTest {
    private val dependencyContainer = DependencyContainer.getSingletonInstance()
    private val factoryContainer = Injector.getSingletonInstance().factoryContainer

    interface Person {
        val name: String
    }

    class Bixx(override val name: String) : Person
    class Sangun(override val name: String) : Person
    class MatPig(override val name: String) : Person
    class Sopt {
        @Inject
        @Qualifier("SoptMvp")
        lateinit var person: Person
    }

    class FakeFactory {
        @Singleton
        @Qualifier("Android Part")
        fun provideBixx(name: String): Person {
            return Bixx(name)
        }

        fun provideSangun(name: String): Person {
            return Sangun(name)
        }

        @Qualifier("SoptMvp")
        fun provideMatPig(name: String): Person {
            return MatPig(name)
        }

        fun provideSopt(): Sopt {
            return Sopt()
        }
    }

    @BeforeEach
    fun clear() {
        dependencyContainer.clear()
        factoryContainer.clear()
    }

    @Test
    fun `싱글톤 어노테이션을 붙인 생성 함수에 의해 생성된 인스턴스는 DependencyContainer에 보관된다`() {
        // given
        dependencyContainer.addInstance(String::class, emptyList(), "bixx")
        factoryContainer.addProvideFactory(FakeFactory())

        // when
        val createdInstance = factoryContainer.getInstance(Person::class, "Android Part")
        val storedInstance = dependencyContainer.getInstance(Person::class, "Android Part")

        // then
        assertThat(createdInstance).isEqualTo(storedInstance)
    }

    @Test
    fun `싱글톤 어노테이션을 붙이지 않은 생성 함수에 의해 생성된 인스턴스는 DependencyContainer에 보관되지 않는다`() {
        // given
        val name = "matPig"
        dependencyContainer.addInstance(String::class, emptyList(), name)
        factoryContainer.addProvideFactory(FakeFactory())

        // when
        val createdInstance = factoryContainer.getInstance(Person::class, "SoptMvp") as Person
        val storedInstance = dependencyContainer.getInstance(Person::class, "SoptMvp")

        // then
        assertThat(createdInstance.name).isEqualTo(name)
        assertThat(storedInstance).isEqualTo(null)
    }

    @Test
    fun `Inject 어노테이션이 붙은 프로퍼티는 주입 된다`() {
        // given
        factoryContainer.addProvideFactory(FakeFactory())
        dependencyContainer.addInstance(String::class, emptyList(), "matPig")

        // when
        val sopt = factoryContainer.getInstance(Sopt::class) as Sopt

        // then
        assertThat(sopt.person).isInstanceOf(MatPig::class.java)
    }
}
