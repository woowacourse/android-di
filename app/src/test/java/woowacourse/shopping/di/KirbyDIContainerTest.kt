package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class KirbyDIContainerTest {
    class LeafDependency

    class OneDependency(
        val dependency: LeafDependency,
    )

    interface SampleRepository

    class RealSampleRepository(
        val dependency: LeafDependency,
    ) : SampleRepository

    class OtherSampleRepository(
        val dependency: LeafDependency,
    ) : SampleRepository

    class NeedsRepository(
        val repository: SampleRepository,
    )

    @KirbyQualifier
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class First

    @KirbyQualifier
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Second

    class QualifiedFieldTarget {
        @KirbyInject
        @property:First
        lateinit var repository: SampleRepository
    }

    class QualifiedConstructorTarget(
        @param:Second val repository: SampleRepository,
    )

    class UnqualifiedFieldTarget {
        @KirbyInject
        lateinit var repository: SampleRepository
    }

    class DoubleQualifiedFieldTarget {
        @KirbyInject
        @property:First
        @property:Second
        lateinit var repository: SampleRepository
    }

    class FieldTarget {
        @KirbyInject
        lateinit var injected: LeafDependency

        var notInjected: LeafDependency? = null
    }

    class CyclicA(
        val b: CyclicB,
    )

    class CyclicB(
        val a: CyclicA,
    )

    @Test
    fun `애노테이션이 붙은 필드만 주입되고 붙지 않은 필드는 주입되지 않는다`() {
        val container = KirbyDIContainer()

        val target = container.createInstance(FieldTarget::class)

        assertThat(target.injected).isNotNull()
        assertThat(target.notInjected).isNull()
    }

    @Test
    fun `생성자 파라미터의 의존성까지 재귀적으로 해결한다`() {
        val container = KirbyDIContainer()

        val instance = container.createInstance(OneDependency::class)

        assertThat(instance.dependency).isNotNull()
    }

    @Test
    fun `인터페이스를 요청하면 바인딩된 구현체로 생성한다`() {
        val container = KirbyDIContainer()
        container.registerBinding(SampleRepository::class, RealSampleRepository::class)

        val repository = container.resolve(SampleRepository::class)

        assertThat(repository).isInstanceOf(RealSampleRepository::class.java)
    }

    @Test
    fun `의존성은 공유하고 최상위 인스턴스는 요청할 때마다 새로 만든다`() {
        val container = KirbyDIContainer()

        val first = container.createInstance(OneDependency::class)
        val second = container.createInstance(OneDependency::class)

        assertThat(first).isNotSameAs(second)
        assertThat(first.dependency).isSameAs(second.dependency)
    }

    @Test
    fun `순환 의존성이면 예외를 던진다`() {
        val container = KirbyDIContainer()

        assertThatThrownBy { container.createInstance(CyclicA::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `같은 인터페이스의 두 구현체를 Qualifier로 각각 선택한다`() {
        val container = containerWithTwoRepositories()

        assertThat(container.resolve(SampleRepository::class, First::class))
            .isInstanceOf(RealSampleRepository::class.java)
        assertThat(container.resolve(SampleRepository::class, Second::class))
            .isInstanceOf(OtherSampleRepository::class.java)
    }

    @Test
    fun `두 후보를 Qualifier 없이 요청하면 후보를 알려주는 오류를 낸다`() {
        val container = containerWithTwoRepositories()

        assertThatThrownBy { container.resolve(SampleRepository::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("SampleRepository")
            .hasMessageContaining("First")
            .hasMessageContaining("Second")
        assertThatThrownBy { container.createInstance(UnqualifiedFieldTarget::class) }
            .hasMessageContaining("Qualifier 없이")
    }

    @Test
    fun `후보가 하나면 Qualifier 없이 조회해도 같은 인스턴스를 반환한다`() {
        val container = KirbyDIContainer()
        container.registerBinding(SampleRepository::class, RealSampleRepository::class, First::class)

        val withoutQualifier = container.resolve(SampleRepository::class)
        val withQualifier = container.resolve(SampleRepository::class, First::class)

        assertThat(withoutQualifier).isSameAs(withQualifier)
    }

    @Test
    fun `서로 다른 Qualifier의 캐시는 섞이지 않는다`() {
        val container = containerWithTwoRepositories()

        val first = container.resolve(SampleRepository::class, First::class)
        val second = container.resolve(SampleRepository::class, Second::class)

        assertThat(first).isNotSameAs(second)
        assertThat(container.resolve(SampleRepository::class, First::class)).isSameAs(first)
        assertThat(container.resolve(SampleRepository::class, Second::class)).isSameAs(second)
    }

    @Test
    fun `같은 타입과 Qualifier를 중복 등록하면 오류를 낸다`() {
        val container = KirbyDIContainer()
        container.registerBinding(SampleRepository::class, RealSampleRepository::class, First::class)

        assertThatThrownBy {
            container.registerBinding(SampleRepository::class, OtherSampleRepository::class, First::class)
        }.hasMessageContaining("이미 등록")
    }

    @Test
    fun `이미 해결한 타입의 바인딩은 뒤늦게 변경할 수 없다`() {
        val container = KirbyDIContainer()
        container.resolve(LeafDependency::class)

        assertThatThrownBy {
            container.registerBinding(LeafDependency::class, LeafDependency::class)
        }.hasMessageContaining("이미 해결한 타입")
    }

    @Test
    fun `의존성 해결이 실패하면 바인딩을 등록하고 다시 시도할 수 있다`() {
        val container = KirbyDIContainer()

        assertThatThrownBy { container.createInstance(NeedsRepository::class) }
            .hasMessageContaining("구현체가 등록되지 않았습니다")

        container.registerBinding(SampleRepository::class, RealSampleRepository::class)

        assertThat(container.createInstance(NeedsRepository::class).repository)
            .isInstanceOf(RealSampleRepository::class.java)
    }

    @Test
    fun `등록되지 않은 Qualifier를 요청하면 오류를 낸다`() {
        val container = KirbyDIContainer()
        container.registerBinding(SampleRepository::class, RealSampleRepository::class, First::class)

        assertThatThrownBy { container.resolve(SampleRepository::class, Second::class) }
            .hasMessageContaining("등록되지 않았습니다")
    }

    @Test
    fun `생성자와 프로퍼티에서 Qualifier를 해석한다`() {
        val container = containerWithTwoRepositories()

        val field = container.createInstance(QualifiedFieldTarget::class)
        val constructor = container.createInstance(QualifiedConstructorTarget::class)

        assertThat(field.repository).isInstanceOf(RealSampleRepository::class.java)
        assertThat(constructor.repository).isInstanceOf(OtherSampleRepository::class.java)
        assertThat(field.repository).isSameAs(container.resolve(SampleRepository::class, First::class))
    }

    @Test
    fun `한 주입 지점에 Qualifier가 둘이면 오류를 낸다`() {
        val container = containerWithTwoRepositories()

        assertThatThrownBy { container.createInstance(DoubleQualifiedFieldTarget::class) }
            .hasMessageContaining("Qualifier를 둘 이상")
    }

    @Test
    fun `최상위 객체는 매번 새로 만들고 자격이 있는 의존성은 공유한다`() {
        val container = containerWithTwoRepositories()

        val first = container.createInstance(QualifiedFieldTarget::class)
        val second = container.createInstance(QualifiedFieldTarget::class)

        assertThat(first).isNotSameAs(second)
        assertThat(first.repository).isSameAs(second.repository)
    }

    private fun containerWithTwoRepositories(): KirbyDIContainer =
        KirbyDIContainer().apply {
            registerBinding(SampleRepository::class, RealSampleRepository::class, First::class)
            registerBinding(SampleRepository::class, OtherSampleRepository::class, Second::class)
        }
}
