package com.harodi

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class DiManagerQualifierTest {
    @Test
    fun `Qualifier가 붙은 필드에는 해당 Qualifier로 등록한 구현체를 주입한다`() {
        // given
        val diManager = createDiManagerWithTwoRepositories()
        val scopeKey = createScopeKey()

        // when
        val consumer = diManager.resolve(InMemoryRepositoryConsumer::class.java, scopeKey)

        // then
        assertIs<InMemoryTestRepository>(consumer.repository)
    }

    @Test
    fun `같은 타입의 구현체가 둘 등록되어 있는데 Qualifier가 없으면 예외가 발생한다`() {
        // given
        val diManager = createDiManagerWithTwoRepositories()
        val scopeKey = createScopeKey()

        // when
        val exception =
            assertFailsWith<IllegalArgumentException> {
                diManager.resolve(TestRepository::class.java, scopeKey)
            }

        // then
        assertContains(exception.message.orEmpty(), "여러 구현체가 등록되어 있습니다")
        assertContains(exception.message.orEmpty(), "Qualifier를 지정해주세요")
    }

    @Test
    fun `등록되지 않은 Qualifier로 구현체를 요청하면 예외가 발생한다`() {
        // given
        val diManager = DiManager()
        diManager.addProvider(
            classType = TestRepository::class.java,
            qualifier = DefaultTestRepositoryQualifier::class,
            value = DefaultTestRepository::class.java,
        )
        val scopeKey = createScopeKey()

        // when
        val exception =
            assertFailsWith<IllegalArgumentException> {
                diManager.resolve(
                    modelClass = TestRepository::class.java,
                    scopeKey = scopeKey,
                    qualifier = InMemoryTestRepositoryQualifier::class,
                )
            }

        // then
        assertContains(exception.message.orEmpty(), "등록된 구현체가 없습니다")
    }

    @Test
    fun `생성자 주입으로 생성된 객체도 필드 주입을 받는다`() {
        // given
        val diManager =
            DiManager().apply {
                addScopePolicy(
                    classType = ConstructorDependency::class.java,
                    qualifier = null,
                    scopeKind = QualifierTestScopeKind.APPLICATION,
                )
                addScopePolicy(
                    classType = FieldDependency::class.java,
                    qualifier = null,
                    scopeKind = QualifierTestScopeKind.APPLICATION,
                )
            }
        val scopeKey = createScopeKey()

        // when
        val consumer = diManager.resolve(ConstructorInjectionConsumer::class.java, scopeKey)

        // then
        assertIs<ConstructorDependency>(consumer.constructorDependency)
        assertIs<FieldDependency>(consumer.constructorDependency.fieldDependency)
    }

    private fun createDiManagerWithTwoRepositories(): DiManager =
        DiManager().apply {
            addProvider(
                classType = TestRepository::class.java,
                qualifier = DefaultTestRepositoryQualifier::class,
                value = DefaultTestRepository::class.java,
            )
            addProvider(
                classType = TestRepository::class.java,
                qualifier = InMemoryTestRepositoryQualifier::class,
                value = InMemoryTestRepository::class.java,
            )
            addScopePolicy(
                classType = TestRepository::class.java,
                qualifier = DefaultTestRepositoryQualifier::class,
                scopeKind = QualifierTestScopeKind.APPLICATION,
            )
            addScopePolicy(
                classType = TestRepository::class.java,
                qualifier = InMemoryTestRepositoryQualifier::class,
                scopeKind = QualifierTestScopeKind.APPLICATION,
            )
        }

    private fun createScopeKey(): ScopeKey =
        ScopeKey(
            parentKey = null,
            scopeKind = QualifierTestScopeKind.APPLICATION,
        )
}

private enum class QualifierTestScopeKind : ScopeKind {
    APPLICATION,
}

interface TestRepository

class DefaultTestRepository : TestRepository

class InMemoryTestRepository : TestRepository

internal class ConstructorInjectionConsumer(
    val constructorDependency: ConstructorDependency,
)

internal class ConstructorDependency {
    @Inject
    lateinit var fieldDependency: FieldDependency
}

internal class FieldDependency

internal class InMemoryRepositoryConsumer {
    @Inject
    @InMemoryTestRepositoryQualifier
    lateinit var repository: TestRepository
}

@Qualifier
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultTestRepositoryQualifier

@Qualifier
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryTestRepositoryQualifier
