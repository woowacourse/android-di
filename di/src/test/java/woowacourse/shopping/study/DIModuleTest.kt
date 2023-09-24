package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import woowacourse.shopping.DIModule
import woowacourse.shopping.annotation.ActivityContext
import woowacourse.shopping.annotation.ApplicationContext
import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.annotation.Provides
import woowacourse.shopping.annotation.Qualifier2
import woowacourse.shopping.annotation.Singleton

@Qualifier2
annotation class CatQualifier

@Qualifier2
annotation class DogQualifier

@Qualifier2
annotation class GloQualifier

@Qualifier2
annotation class GluQualifier

internal interface Animal
internal class Cat : Animal
internal class Dog : Animal
internal class Zoo1(@CatQualifier val animal: Animal)
internal class Zoo2(@DogQualifier val animal: Animal)

internal class Person(val name: String)
internal class Crew1(@GloQualifier val person: Person)
internal class Crew2(@GluQualifier val person: Person)

internal interface FakeContext
internal class FakeApplicationContext : FakeContext
internal class FakeActivityContext : FakeContext
internal class FakeDatabaseWithApplicationContext(val context: FakeContext)
internal class FakeDatabaseWithActivityContext(val context: FakeContext)

internal class FakeActivityModule(parentModule: DIModule?) : DIModule(parentModule) {
    @Provides
    private fun provideFakeDatabaseWithApplicationContext(@ApplicationContext context: FakeContext) =
        FakeDatabaseWithApplicationContext(context)

    @Provides
    private fun provideFakeDatabaseWithActivityContext(@ActivityContext context: FakeContext) =
        FakeDatabaseWithActivityContext(context)
}

internal class FakeApplicationModule(parentModule: DIModule?) : DIModule(parentModule) {
    @Binds
    @CatQualifier
    @Singleton
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

internal class DIModuleTest {
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

    @Test
    fun `Singleton 어노테이션이 붙은 Cat은 싱글 인스턴스를 가진다`() {
        // given && when
        val cat1 = activityModule.inject(Cat::class)
        val cat2 = activityModule.inject(Cat::class)

        // then
        assertThat(cat1).isEqualTo(cat2)
    }

    @Test
    fun `Singleton 어노테이션이 붙지 않은 Dog는 서로 다른 인스턴스를 가진다`() {
        // given && when
        val dog1 = activityModule.inject(Dog::class)
        val dog2 = activityModule.inject(Dog::class)

        // then
        assertThat(dog1).isNotEqualTo(dog2)
    }

    @Test
    fun `ContextType이 ApplicationContext면 FakeApplicationContext를 주입받는다`() {
        // given
        applicationModule.addInstance((FakeContext::class to ApplicationContext()), FakeApplicationContext())
        activityModule.addInstance((FakeContext::class to ActivityContext()), FakeActivityContext())

        // when
        val database = activityModule.inject(FakeDatabaseWithApplicationContext::class)

        // then
        assertThat(database.context).isInstanceOf(FakeApplicationContext::class.java)
    }

    @Test
    fun `ContextType이 ActivityContext면 FakeActivityContext를 주입받는다`() {
        // given
        applicationModule.addInstance((FakeContext::class to ApplicationContext()), FakeApplicationContext())
        activityModule.addInstance((FakeContext::class to ActivityContext()), FakeActivityContext())

        // when
        val database = activityModule.inject(FakeDatabaseWithActivityContext::class)

        // then
        assertThat(database.context).isInstanceOf(FakeActivityContext::class.java)
    }
}
