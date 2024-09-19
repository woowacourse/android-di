import com.google.common.truth.Truth.assertThat
import com.woowacourse.di.DependencyContainer
import com.woowacourse.di.annotation.Qualifier
import org.junit.After
import org.junit.Test

interface FakeRepository

class DefaultRepository1 : FakeRepository

class DefaultRepository2 : FakeRepository

class ConstructorTestViewModel1(
    val fooDependency: FakeRepository,
)

class ConstructorTestViewModel2(
    val fooDependency: FakeRepository,
)

class ConstructorTestViewModel3(
    @Qualifier("foo")
    val fooDependency: FakeRepository,
)

class ConstructorTestViewModel4(
    @Qualifier("foo")
    val fooDependency: FakeRepository,
    @Qualifier("bar")
    val barDependency: FakeRepository,
)

class QualifierInjectorTest {
    @After
    fun tearDown() {
        DependencyContainer.clear()
    }

    @Test
    fun `등록된 의존성이 존재하면 의존성 주입에 성공한다`() {
        // given
        val repository1 = DefaultRepository1()
        DependencyContainer.addInstance(FakeRepository::class, repository1)

        // when
        val viewModel =
            ConstructorTestViewModel1(DependencyContainer.instance(FakeRepository::class))

        // then
        assertThat(viewModel.fooDependency).isEqualTo(repository1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `등록된 의존성이 존재하지 않으면 의존성 주입에 실패한다`() {
        // given
        val repository1 = DefaultRepository1()
        val repository2 = DefaultRepository2()

        // when
        ConstructorTestViewModel2(DependencyContainer.instance(FakeRepository::class))
    }

    @Test
    fun `등록된 의존성이 여러개인 경우, Qualifier로 의존성 주입을 할 수 있다`() {
        // given
        val repository1 = DefaultRepository1()
        val repository2 = DefaultRepository2()
        DependencyContainer.addInstance(FakeRepository::class, repository1, "foo")
        DependencyContainer.addInstance(FakeRepository::class, repository2, "bar")

        // when
        val viewModel =
            ConstructorTestViewModel3(
                DependencyContainer.instance(FakeRepository::class, "foo"),
            )

        // then
        assertThat(viewModel.fooDependency).isEqualTo(repository1)
    }

    @Test
    fun `의존성을 여러개 주입해야 하는 경우, Qualifier로 의존성 주입을 할 수 있다`() {
        // given
        val repository1 = DefaultRepository1()
        val repository2 = DefaultRepository2()
        DependencyContainer.addInstance(FakeRepository::class, repository1, "foo")
        DependencyContainer.addInstance(FakeRepository::class, repository2, "bar")

        // when
        val viewModel =
            ConstructorTestViewModel4(
                DependencyContainer.instance(FakeRepository::class, "foo"),
                DependencyContainer.instance(FakeRepository::class, "bar"),
            )

        // then
        assertThat(viewModel.fooDependency).isEqualTo(repository1)
        assertThat(viewModel.barDependency).isEqualTo(repository2)
    }
}
