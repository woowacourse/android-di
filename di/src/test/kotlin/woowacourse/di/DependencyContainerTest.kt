package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class DependencyContainerTest {
    @Test
    fun `등록한 객체를 같은 인스턴스로 반환한다`() {
        val container = DependencyContainer()
        val dependency = RegisteredDependency()
        container.registerInstance(RegisteredDependency::class, dependency)

        assertThat(container.get(RegisteredDependency::class)).isSameAs(dependency)
    }

    @Test
    fun `주 생성자 의존성을 재귀적으로 만들고 공유한다`() {
        val container = DependencyContainer()
        val dependency = RegisteredDependency()
        container.registerInstance(RegisteredDependency::class, dependency)

        val first = container.get(IntermediateDependency::class)
        val second = container.get(IntermediateDependency::class)

        assertThat(first.dependency).isSameAs(dependency)
        assertThat(second).isSameAs(first)
    }

    @Test
    fun `create는 매번 새 루트 객체를 만들고 의존성은 공유한다`() {
        val container = DependencyContainer()
        val dependency = RegisteredDependency()
        container.registerInstance(RegisteredDependency::class, dependency)

        val first = container.create(IntermediateDependency::class)
        val second = container.create(IntermediateDependency::class)

        assertThat(first).isNotSameAs(second)
        assertThat(first.dependency).isSameAs(second.dependency)
    }

    @Test
    fun `필드 Annotation이 붙은 필드만 주입한다`() {
        val container = DependencyContainer()
        val dependency = RegisteredDependency()
        container.registerInstance(RegisteredDependency::class, dependency)
        val target = FieldInjectionFixture()

        container.injectMembers(target)

        assertThat(target.injectedDependency).isSameAs(dependency)
        assertThat(target.unannotatedDependency).isNull()
    }

    @Test
    fun `Qualifier로 생성자 의존성을 선택한다`() {
        val container = createQualifiedContainer()

        val target = container.create(PrimaryChoiceConsumer::class)

        assertThat(target.choice).isInstanceOf(FirstChoice::class.java)
    }

    @Test
    fun `Qualifier로 필드 의존성을 선택한다`() {
        val container = createQualifiedContainer()
        val target = QualifiedFieldConsumer()

        container.injectMembers(target)

        assertThat(target.choice).isInstanceOf(SecondChoice::class.java)
    }

    @Test
    fun `Qualifier 없이 구현체가 여러 개면 명확한 모호성 오류를 낸다`() {
        val container = createQualifiedContainer()

        assertThatThrownBy { container.get(Choice::class) }
            .isInstanceOf(AmbiguousDependencyException::class.java)
            .hasMessageContaining("Qualifier를 지정하세요")
    }

    @Test
    fun `등록하지 않은 인터페이스를 요청하면 오류가 발생한다`() {
        val container = DependencyContainer()

        assertThatThrownBy { container.get(UnregisteredDependency::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("주 생성자를 찾을 수 없습니다")
    }

    @Test
    fun `순환 의존성은 경로를 포함한 오류를 낸다`() {
        val container = DependencyContainer()

        assertThatThrownBy { container.get(CircularDependencyA::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("CircularDependencyA -> CircularDependencyB -> CircularDependencyA")
    }

    private fun createQualifiedContainer(): DependencyContainer {
        val container = DependencyContainer()
        container.registerInstance(Choice::class, FirstChoice(), FirstChoiceKey::class)
        container.registerInstance(Choice::class, SecondChoice(), SecondChoiceKey::class)
        return container
    }
}

class RegisteredDependency

class IntermediateDependency(
    val dependency: RegisteredDependency,
)

class FieldInjectionFixture {
    @field:Inject
    lateinit var injectedDependency: RegisteredDependency

    var unannotatedDependency: RegisteredDependency? = null
}

class PrimaryChoiceConsumer(
    @param:FirstChoiceKey val choice: Choice,
)

class QualifiedFieldConsumer {
    @field:Inject
    @field:SecondChoiceKey
    lateinit var choice: Choice
}

interface Choice

class FirstChoice : Choice

class SecondChoice : Choice

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FirstChoiceKey

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SecondChoiceKey

interface UnregisteredDependency

class CircularDependencyA(
    val dependency: CircularDependencyB,
)

class CircularDependencyB(
    val dependency: CircularDependencyA,
)
