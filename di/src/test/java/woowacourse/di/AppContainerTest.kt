package woowacourse.di

import com.example.di.AppContainer
import com.example.di.MyInjector
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AppContainerTest {
    @Before
    fun setUp() {
        val field =
            AppContainer::class.java.getDeclaredField("providers").apply { isAccessible = true }
        val map = field.get(AppContainer) as MutableMap<*, *>
        map.clear()
    }

    @Test
    fun `provide로 등록한 인스턴스를 resolve하면 해당 인스턴스가 반환된다`() {
        // given
        val provided = ProvidedA("A-Provided")
        AppContainer
            .provide(ProvidedA::class, provided)

        // when
        val resolved =
            AppContainer
                .resolve(ProvidedA::class)

        // then
        assertSame(provided, resolved)
    }

    @Test
    fun `provideModule을 호출하면 object의 모든 프로퍼티가 등록된다`() {
        // when
        AppContainer
            .provideModule(TestModule::class)

        // then
        val ra =
            AppContainer
                .resolve(TestModule.ModuleA::class)
        val rb =
            AppContainer
                .resolve(TestModule.ModuleB::class)

        assertSoftly { softly ->
            softly.assertThat(ra.v).isEqualTo(42)
            softly.assertThat(rb.name).isEqualTo("Bee")
        }
    }

    @Test
    fun `resolve 호출 시 overrides 인스턴스가 우선적으로 사용된다`() {
        // given
        val provided = OverFoo("provided")
        val override = OverFoo("override")
        AppContainer
            .provide(OverFoo::class, provided)

        // when
        val resolved =
            AppContainer
                .resolve<NeedsFoo>(mapOf(OverFoo::class to override))

        // then
        assertSoftly { softly ->
            softly.assertThat(resolved.foo.tag).isEqualTo("override")
            softly.assertThat(resolved.foo).isNotSameAs(provided)
        }
    }

    @Test
    fun `MyInjector 어노테이션이 붙은 프로퍼티에 자동으로 인스턴스가 주입된다`() {
        // given
        val repoProvided = Repo("MainRepo")
        AppContainer
            .provide(Repo::class, repoProvided)

        // when
        val holder =
            AppContainer
                .resolve(Holder::class)

        // then
        assertSoftly { softly ->
            softly.assertThat(holder.repo).isNotNull
            softly.assertThat(holder.repo).isEqualTo(holder.repo)
        }
    }

    @Test
    fun `public 생성자가 없는 클래스는 resolve 시 예외가 발생한다`() {
        // when & then
        assertThrows(java.lang.IllegalArgumentException::class.java) {
            AppContainer
                .resolve(NoPrimaryConstructor::class)
        }
    }

    @Test
    fun `의존성이 다른 의존성을 포함할 때도 중첩 주입이 정상적으로 수행된다`() {
        // when
        val root =
            AppContainer
                .resolve(Root::class)

        // then
        assertSoftly { softly ->
            softly.assertThat(root).isNotNull
            softly.assertThat(root.middle).isNotNull
            softly.assertThat(root.middle.leaf).isNotNull
        }
    }
}

data class ProvidedA(
    val id: String = "A1",
)

object TestModule {
    data class ModuleA(
        val v: Int = 10,
    )

    data class ModuleB(
        val name: String = "B",
    )

    val a = ModuleA(42)
    val b = ModuleB("Bee")
}

data class OverFoo(
    val tag: String = "default",
)

class NeedsFoo
    @MyInjector
    constructor(
        val foo: OverFoo,
    )

data class Repo(
    val name: String,
)

class Holder {
    @MyInjector
    var repo: Repo? = null
}

class NoPrimaryConstructor {
    constructor()
}

class Leaf

class Middle
    @MyInjector
    constructor(
        val leaf: Leaf,
    )

class Root
    @MyInjector
    constructor(
        val middle: Middle,
    )
