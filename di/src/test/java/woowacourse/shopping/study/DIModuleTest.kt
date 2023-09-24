package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import woowacourse.shopping.DIModule
import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.annotation.Provides
import woowacourse.shopping.annotation.Qualifier2

@Qualifier2
annotation class CatQualifier

@Qualifier2
annotation class DogQualifier

@Qualifier2
annotation class GloQualifier

@Qualifier2
annotation class GluQualifier

interface Animal
class Cat : Animal
class Dog : Animal
class Zoo1(@CatQualifier val animal: Animal)
class Zoo2(@DogQualifier val animal: Animal)

class Person(val name: String)
class Crew1(@GloQualifier val person: Person)
class Crew2(@GluQualifier val person: Person)

class FakeActivityModule(parentModule: DIModule?) : DIModule(parentModule)

class FakeApplicationModule(parentModule: DIModule?) : DIModule(parentModule) {
    @Binds
    @CatQualifier
    private lateinit var bindCat: Cat

    @Binds
    @DogQualifier
    private lateinit var bindDog: Dog

    @Provides
    @GloQualifier
    private fun provideGlo(): Person {
        return Person("글로")
    }

    @Provides
    @GluQualifier
    private fun provideGlu(): Person {
        return Person("글루")
    }
}

class DIModuleTest {
    private val applicationModule = FakeApplicationModule(null)
    private val activityModule = FakeActivityModule(applicationModule)

    @Test
    fun `Zoo1에 Cat을 주입할 수 있다`() {
        // given && when
        val zoo = activityModule.inject(Zoo1::class)

        // then
        assertThat(zoo.animal).isInstanceOf(Cat::class.java)
    }

    @Test
    fun `Zoo2에 Dog를 주입할 수 있다`() {
        // given && when
        val zoo = activityModule.inject(Zoo2::class)

        // then
        assertThat(zoo.animal).isInstanceOf(Dog::class.java)
    }

    @Test
    fun `Crew1에 글로라는 이름을 가진 Person을 주입할 수 있다`() {
        // given && when
        val crew = activityModule.inject(Crew1::class)

        // then
        assertThat(crew.person.name).isEqualTo("글로")
    }

    @Test
    fun `Crew2에 글루라는 이름을 가진 Person을 주입할 수 있다`() {
        // given && when
        val crew = activityModule.inject(Crew2::class)

        // then
        assertThat(crew.person.name).isEqualTo("글루")
    }
}
