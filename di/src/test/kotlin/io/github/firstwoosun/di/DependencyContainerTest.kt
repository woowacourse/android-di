package io.github.firstwoosun.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class DependencyContainerTest {
    @Test
    fun `주입 표시된 필드에만 주입하고 같은 키의 인스턴스를 재사용한다`() {
        val container =
            dependencyContainer(
                DependencyBinding(Catalog::class, CatalogImpl::class, Local::class),
            )
        val target = CatalogTarget()

        container.inject(target)

        assertThat(target.first).isSameAs(target.second)
        assertThat(target.first).isSameAs(container.getInstance(Catalog::class, Local::class))
        assertThat(target.unmarked).isNull()
    }

    @Test
    fun `생성자 의존성을 재귀적으로 만들고 provider 인스턴스를 전달한다`() {
        val config = AppConfig()
        val container =
            dependencyContainer(
                DependencyBinding(Engine::class, EngineImpl::class, Local::class),
                DependencyBinding(Feature::class, FeatureImpl::class),
                instanceProvider = InstanceProvider { type -> if (type == AppConfig::class) config else null },
            )

        val feature = container.getInstance(Feature::class) as FeatureImpl

        assertThat(feature.engine).isInstanceOf(EngineImpl::class.java)
        assertThat((feature.engine as EngineImpl).config).isSameAs(config)
    }

    @Test
    fun `등록되지 않은 타입은 명확한 오류를 낸다`() {
        val container = dependencyContainer()

        assertThatThrownBy { container.getInstance(UnknownDependency::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("등록되지 않은 의존성 타입")
    }

    @Test
    fun `같은 타입과 qualifier의 중복 바인딩을 거부한다`() {
        val binding = DependencyBinding(Catalog::class, CatalogImpl::class, Local::class)

        assertThatThrownBy { dependencyContainer(binding, binding) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("중복된 의존성 바인딩")
    }

    @Test
    fun `타입에 할당할 수 없는 구현체 바인딩을 거부한다`() {
        assertThatThrownBy {
            dependencyContainer(DependencyBinding(Catalog::class, Unrelated::class))
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("구현체가 아닙니다")
    }

    @Test
    fun `순환 생성 의존성에서 오류를 낸다`() {
        val container =
            dependencyContainer(
                DependencyBinding(Left::class, LeftImpl::class),
                DependencyBinding(Right::class, RightImpl::class),
            )

        assertThatThrownBy { container.getInstance(Left::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("순환 의존성 발생")
    }

    @Test
    fun `qualifier 없는 주입 지점의 여러 후보를 거부한다`() {
        val container =
            dependencyContainer(
                DependencyBinding(Catalog::class, CatalogImpl::class, Local::class),
                DependencyBinding(Catalog::class, RemoteCatalog::class, Remote::class),
            )

        assertThatThrownBy { container.inject(UnqualifiedCatalogTarget()) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("모호한 의존성 타입")
    }

    private fun dependencyContainer(
        vararg bindings: DependencyBinding,
        instanceProvider: InstanceProvider = InstanceProvider { null },
    ) = DependencyContainer(instanceProvider, bindings.toList())
}

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Local

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote

interface Catalog

class CatalogImpl : Catalog

class RemoteCatalog : Catalog

class Unrelated

class CatalogTarget {
    @field:CustomFieldInjection
    @field:Local
    lateinit var first: Catalog

    @field:CustomFieldInjection
    @field:Local
    lateinit var second: Catalog

    var unmarked: Catalog? = null
}

class AppConfig

interface Engine

class EngineImpl(
    val config: AppConfig,
) : Engine

interface Feature

class FeatureImpl(
    @Local val engine: Engine,
) : Feature

interface UnknownDependency

interface Left

interface Right

class LeftImpl(
    val right: Right,
) : Left

class RightImpl(
    val left: Left,
) : Right

class UnqualifiedCatalogTarget {
    @field:CustomFieldInjection
    lateinit var catalog: Catalog
}
