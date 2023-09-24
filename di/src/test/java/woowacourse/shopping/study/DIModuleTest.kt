package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import woowacourse.shopping.DIModule
import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.annotation.Qualifier2

@Qualifier2
annotation class CatQualifier

@Qualifier2
annotation class DogQualifier

interface Animal

class Cat : Animal

class Dog : Animal

class Zoo1(@CatQualifier val animal: Animal)

class Zoo2(@DogQualifier val animal: Animal)

class Person

class FakeModule : DIModule() {
    @Binds
    @CatQualifier
    private lateinit var bindCat: Cat

    @Binds
    @DogQualifier
    private lateinit var bindDog: Dog
}

class DIModuleTest {
    private val module = FakeModule()

    @Test
    fun `Zoo1에 Cat을 주입할 수 있다`() {
        // given && when
        val zoo = module.inject(Zoo1::class)

        // then
        assertThat(zoo.animal).isInstanceOf(Cat::class.java)
    }

    @Test
    fun `Zoo2에 Dog를 주입할 수 있다`() {
        // given && when
        val zoo = module.inject(Zoo2::class)

        // then
        assertThat(zoo.animal).isInstanceOf(Dog::class.java)
    }

    @Test
    fun `Person을 주입할 수 있다`() {
        // given && when
        val person = module.inject(Person::class)

        // then
        assertThat(person).isInstanceOf(Person::class.java)
    }
}
