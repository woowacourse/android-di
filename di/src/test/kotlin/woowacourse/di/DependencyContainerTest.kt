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

    @Test
    fun `등록한 스코프 키에 따라 의존성을 재사용하고 서로 다른 스코프는 분리한다`() {
        val scopeKey = ScopeKey("request")
        val container = DependencyContainer()
        var creations = 0
        container.registerFactory(ScopedDependency::class, scope = scopeKey) {
            creations += 1
            ScopedDependency()
        }
        val firstScope = container.openScope(scopeKey, "request-1")
        val secondScope = container.openScope(scopeKey, "request-2")

        val first = container.getInScope(ScopedDependency::class, firstScope)
        val reused = container.getInScope(ScopedDependency::class, firstScope)
        val second = container.getInScope(ScopedDependency::class, secondScope)

        assertThat(reused).isSameAs(first)
        assertThat(second).isNotSameAs(first)
        assertThat(creations).isEqualTo(2)
    }

    @Test
    fun `스코프를 닫으면 보유 인스턴스를 비우고 다시 사용할 수 없다`() {
        val scopeKey = ScopeKey("request")
        val container = DependencyContainer()
        container.registerFactory(ScopedDependency::class, scope = scopeKey) { ScopedDependency() }
        val scope = container.openScope(scopeKey, "request-1")
        container.getInScope(ScopedDependency::class, scope)

        scope.close()

        assertThat(scope.isClosed).isTrue()
        assertThat(scope.cachedInstanceCount()).isZero()
        assertThatThrownBy { container.getInScope(ScopedDependency::class, scope) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("종료된 스코프")
    }

    @Test
    fun `새로운 종류의 스코프를 추가해도 컨테이너 분기 없이 재귀 의존성을 해석한다`() {
        val customScope = ScopeKey("customLifetime")
        val container = DependencyContainer()
        container.registerFactory(ScopedLeaf::class, scope = customScope) { ScopedLeaf() }
        container.registerFactory(ScopedParent::class, scope = customScope) { resolver ->
            ScopedParent(resolver.get(ScopedLeaf::class))
        }
        val scope = container.openScope(customScope, "custom-1")

        val first = container.getInScope(ScopedParent::class, scope)
        val second = container.getInScope(ScopedParent::class, scope)

        assertThat(second).isSameAs(first)
        assertThat(second.leaf).isSameAs(container.getInScope(ScopedLeaf::class, scope))
    }

    @Test
    fun `스코프가 필요한 의존성을 일반 해석하면 필요한 스코프를 안내한다`() {
        val scopeKey = ScopeKey("request")
        val container = DependencyContainer()
        container.registerFactory(ScopedDependency::class, scope = scopeKey) { ScopedDependency() }

        assertThatThrownBy { container.get(ScopedDependency::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("request")
    }

    private fun createQualifiedContainer(): DependencyContainer {
        val container = DependencyContainer()
        container.registerInstance(Choice::class, FirstChoice(), FirstChoiceKey::class)
        container.registerInstance(Choice::class, SecondChoice(), SecondChoiceKey::class)
        return container
    }
}

class RegisteredDependency

class ScopedDependency

class ScopedLeaf

class ScopedParent(
    val leaf: ScopedLeaf,
)

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
