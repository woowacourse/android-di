package woowacourse.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class DependencyContainerTest {
    @Test
    fun `Qualifier가 붙은 필드에 해당 구현체를 주입한다`() {
        val container = qualifiedContainer()
        val target = QualifiedTarget()

        container.inject(target)

        assertIs<RoomTestRepository>(target.roomRepository)
        assertIs<InMemoryTestRepository>(target.inMemoryRepository)
    }

    @Test
    fun `생성자 파라미터의 Qualifier에 해당하는 구현체를 주입한다`() {
        val target = assertIs<ConstructorQualifiedTarget>(qualifiedContainer().create(ConstructorQualifiedTarget::class))

        assertIs<RoomTestRepository>(target.repository)
    }

    @Test
    fun `같은 타입의 구현체가 여러 개이고 Qualifier가 없으면 오류가 발생한다`() {
        val container = qualifiedContainer()
        val target = UnqualifiedTarget()

        val exception = assertFailsWith<IllegalStateException> { container.inject(target) }

        assertContains(exception.message.orEmpty(), TestRepository::class.qualifiedName.orEmpty())
        assertContains(exception.message.orEmpty(), "Qualifier")
        assertContains(exception.message.orEmpty(), RoomTest::class.simpleName.orEmpty())
        assertContains(exception.message.orEmpty(), InMemoryTest::class.simpleName.orEmpty())
    }

    @Test
    fun `구현체가 하나여도 Qualifier가 없으면 오류가 발생한다`() {
        val container =
            DependencyContainer(
                bindings = mapOf(DependencyKey(TestRepository::class, RoomTest::class) to RoomTestRepository::class),
            )

        val exception = assertFailsWith<IllegalStateException> { container.inject(UnqualifiedTarget()) }

        assertContains(exception.message.orEmpty(), RoomTest::class.simpleName.orEmpty())
    }

    @Test
    fun `순환 의존성이 있으면 원인을 알 수 있는 오류가 발생한다`() {
        val exception = assertFailsWith<IllegalStateException> { DependencyContainer().create(CycleA::class) }

        assertContains(exception.message.orEmpty(), "CycleA -> CycleB -> CycleA")
    }

    @Test
    fun `애노테이션이 붙은 필드에만 의존성을 재귀적으로 주입하고 재사용한다`() {
        val container =
            DependencyContainer(
                bindings = mapOf(DependencyKey(TestRepository::class) to DefaultTestRepository::class),
            )
        val target = RecursiveTarget()
        val anotherTarget = RecursiveTarget()

        container.inject(target)
        container.inject(anotherTarget)

        assertIs<DefaultTestRepository>(target.repository)
        assertNotNull(target.repository.dependency)
        assertSame(target.repository, anotherTarget.repository)
        assertFalse(target.isIgnoredRepositoryInitialized())
    }

    private fun qualifiedContainer(): DependencyContainer =
        DependencyContainer(
            bindings =
                mapOf(
                    DependencyKey(TestRepository::class, RoomTest::class) to RoomTestRepository::class,
                    DependencyKey(TestRepository::class, InMemoryTest::class) to InMemoryTestRepository::class,
                ),
        )
}

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
private annotation class RoomTest

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
private annotation class InMemoryTest

private interface TestRepository {
    val dependency: TestDependency?
}

private class RoomTestRepository : TestRepository {
    override val dependency: TestDependency? = null
}

private class InMemoryTestRepository : TestRepository {
    override val dependency: TestDependency? = null
}

private class DefaultTestRepository(
    override val dependency: TestDependency,
) : TestRepository

private class TestDependency

private class QualifiedTarget {
    @Inject
    @RoomTest
    lateinit var roomRepository: TestRepository

    @Inject
    @InMemoryTest
    lateinit var inMemoryRepository: TestRepository
}

private class UnqualifiedTarget {
    @Inject
    lateinit var repository: TestRepository
}

private class ConstructorQualifiedTarget(
    @RoomTest val repository: TestRepository,
)

private class CycleA(
    val dependency: CycleB,
)

private class CycleB(
    val dependency: CycleA,
)

private class RecursiveTarget {
    @Inject
    lateinit var repository: TestRepository

    private lateinit var ignoredRepository: TestRepository

    fun isIgnoredRepositoryInitialized(): Boolean = ::ignoredRepository.isInitialized
}
