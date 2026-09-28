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

        // when
        val consumer = diManager.fieldInject(InMemoryRepositoryConsumer::class.java)

        // then
        assertIs<InMemoryTestRepository>(consumer.repository)
    }

    @Test
    fun `같은 타입의 구현체가 둘 등록되어 있는데 Qualifier가 없으면 예외가 발생한다`() {
        // given
        val diManager = createDiManagerWithTwoRepositories()
        val dependencyKey = DependencyKey(TestRepository::class.java, null)

        // when
        val exception =
            assertFailsWith<IllegalArgumentException> {
                diManager.searchInstance(dependencyKey)
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
        val dependencyKey =
            DependencyKey(
                classType = TestRepository::class.java,
                qualifier = InMemoryTestRepositoryQualifier::class,
            )

        // when
        val exception =
            assertFailsWith<IllegalArgumentException> {
                diManager.searchInstance(dependencyKey)
            }

        // then
        assertContains(exception.message.orEmpty(), "등록된 구현체가 없습니다")
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
        }
}

interface TestRepository

class DefaultTestRepository : TestRepository

class InMemoryTestRepository : TestRepository

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
