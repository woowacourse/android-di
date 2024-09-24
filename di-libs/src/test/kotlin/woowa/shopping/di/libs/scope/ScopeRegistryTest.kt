package woowa.shopping.di.libs.scope

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import woowa.shopping.di.libs.annotation.InternalApi
import woowa.shopping.di.libs.factory.ScopedInstanceFactory
import woowa.shopping.di.libs.qualify.named

@OptIn(InternalApi::class)
class ScopeRegistryTest {

    @Test
    fun `Scope 와 Scope의 생명주기를 따르는 InstanceFactory 를 등록한다`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val instanceFactory = ScopedInstanceFactory(
            instanceClazz = String::class,
            factory = { "string" }
        )
        // when & then
        shouldNotThrow<IllegalArgumentException> {
            scopeRegistry.registerInstanceFactory(scopeQualifier, instanceFactory)
        }
    }

    @Test
    fun `Scope 의 생명주기를 따르는 InstanceFactory 가 이미 있을 경우 예외 반환`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val instanceFactory = ScopedInstanceFactory(
            instanceClazz = String::class,
            factory = { "string" }
        )
        // when & then
        scopeRegistry.registerInstanceFactory(scopeQualifier, instanceFactory)
        shouldThrow<IllegalArgumentException> {
            scopeRegistry.registerInstanceFactory(scopeQualifier, instanceFactory)
        }
    }

    @Test
    fun `Lock 된 Scope 에 해당하는 객체를 가져올 경우 예외가 발생`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val factories = ScopedInstanceFactory(
            instanceClazz = String::class,
            factory = { "string" }
        )
        // when & then
        scopeRegistry.registerInstanceFactory(scopeQualifier, factories)
        shouldThrow<IllegalArgumentException> {
            scopeRegistry.resolve(scopeQualifier, String::class)
        }
    }

    @Test
    fun `UnLock 된 Scope 에 해당하는 객체만 가져올 수 있다`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val factories = ScopedInstanceFactory(
            instanceClazz = String::class,
            factory = { "string" }
        )
        // when
        scopeRegistry.registerInstanceFactory(scopeQualifier, factories)
        scopeRegistry.unlockScope(scopeQualifier)
        val result = scopeRegistry.resolve(scopeQualifier, String::class)
        // then
        result shouldBe "string"
    }

    @Test
    fun `객체를 가져올 때, Scope 가 등록되어 있지 않으면 예외 발생`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val scopeQualifer2 = named("scope2")
        val factories = ScopedInstanceFactory(
            instanceClazz = String::class,
            factory = { "string" }
        )
        // when
        scopeRegistry.registerInstanceFactory(scopeQualifier, factories)
        scopeRegistry.unlockScope(scopeQualifier)
        shouldThrow<IllegalArgumentException> {
            scopeRegistry.resolve(scopeQualifer2, String::class)
        }
    }

    @Test
    fun `객체를 가져올 때, Scope 의 생명주기를 따르는 객체가 등록되어 있지 않으면 예외 발생`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val factories = ScopedInstanceFactory(
            instanceClazz = String::class,
            factory = { "string" }
        )
        // when
        scopeRegistry.registerInstanceFactory(scopeQualifier, factories)
        scopeRegistry.unlockScope(scopeQualifier)
        val instance = scopeRegistry.resolve(scopeQualifier, Int::class)
        // then
        instance.shouldBeNull()
    }

    @Test
    fun `Qualifier 를 통해 같은 타입의 객체도 등록할 수 있다`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val factories = listOf(
            ScopedInstanceFactory(
                qualifier = named("string1"),
                instanceClazz = String::class,
                factory = { "string1" }
            ),
            ScopedInstanceFactory(
                qualifier = named("string2"),
                instanceClazz = String::class,
                factory = { "string2" }
            ),
        )
        // when
        scopeRegistry.registerInstanceFactories(scopeQualifier, factories)
        scopeRegistry.unlockScope(scopeQualifier)
        val result1 = scopeRegistry.resolve(scopeQualifier, String::class, named("string1"))
        val result2 = scopeRegistry.resolve(scopeQualifier, String::class, named("string2"))
        // then
        result1 shouldBe "string1"
        result2 shouldBe "string2"
    }

    @Test
    fun `Scope 의 생명주기가 끝나면, Scope 에 등록된 객체들은 모두 Lock 이 걸린다`() {
        // given
        val scopeRegistry = ScopeRegistry()
        val scopeQualifier = named("scope")
        val factories = listOf(
            ScopedInstanceFactory(
                qualifier = named("string1"),
                instanceClazz = String::class,
                factory = { "string1" }
            ),
            ScopedInstanceFactory(
                qualifier = named("string2"),
                instanceClazz = String::class,
                factory = { "string2" }
            ),
        )
        // when
        val scope = scopeRegistry.registerInstanceFactories(scopeQualifier, factories)
        scopeRegistry.unlockScope(scopeQualifier)
        scopeRegistry.clearScope(scopeQualifier)
        // then
        shouldThrow<IllegalArgumentException> {
            scopeRegistry.resolve(scopeQualifier, String::class, named("string1"))
        }
    }
}