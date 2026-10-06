package woowacourse.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class DependencyContainerTest {
    @Test
    fun `Qualifier가 붙은 필드에 해당 구현체를 주입한다`() {
        val scope = qualifiedContainer().openScope(testScope)
        val target = QualifiedTarget()

        scope.inject(target)

        assertIs<RoomTestRepository>(target.roomRepository)
        assertIs<InMemoryTestRepository>(target.inMemoryRepository)
    }

    @Test
    fun `생성자 파라미터의 Qualifier에 해당하는 구현체를 주입한다`() {
        val target =
            assertIs<ConstructorQualifiedTarget>(
                qualifiedContainer().openScope(testScope).create(ConstructorQualifiedTarget::class),
            )

        assertIs<RoomTestRepository>(target.repository)
    }

    @Test
    fun `같은 타입의 구현체가 여러 개이고 Qualifier가 없으면 오류가 발생한다`() {
        val scope = qualifiedContainer().openScope(testScope)
        val target = UnqualifiedTarget()

        val exception = assertFailsWith<IllegalStateException> { scope.inject(target) }

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

        val exception = assertFailsWith<IllegalStateException> { container.openScope(testScope).inject(UnqualifiedTarget()) }

        assertContains(exception.message.orEmpty(), RoomTest::class.simpleName.orEmpty())
    }

    @Test
    fun `순환 의존성이 있으면 원인을 알 수 있는 오류가 발생한다`() {
        val exception =
            assertFailsWith<IllegalStateException> {
                DependencyContainer().openScope(testScope).create(CycleA::class)
            }

        assertContains(exception.message.orEmpty(), "CycleA -> CycleB -> CycleA")
    }

    @Test
    fun `애노테이션이 붙은 필드에만 의존성을 재귀적으로 주입하고 재사용한다`() {
        val container =
            DependencyContainer(
                bindings = mapOf(DependencyKey(TestRepository::class) to DefaultTestRepository::class),
                scopes = mapOf(DependencyKey(TestRepository::class) to testScope),
            )
        val scope = container.openScope(testScope)
        val target = RecursiveTarget()
        val anotherTarget = RecursiveTarget()

        scope.inject(target)
        scope.inject(anotherTarget)

        assertIs<DefaultTestRepository>(target.repository)
        assertNotNull(target.repository.dependency)
        assertSame(target.repository, anotherTarget.repository)
        assertFalse(target.isIgnoredRepositoryInitialized())
    }

    @Test
    fun `앱 ViewModel 화면 스코프는 각각 재사용하고 종료 후 새 인스턴스를 만든다`() {
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        val screen = ScopeType("screen")
        val container =
            DependencyContainer(
                scopes =
                    mapOf(
                        DependencyKey(AppService::class) to app,
                        DependencyKey(ViewModelService::class) to viewModel,
                        DependencyKey(ScreenService::class) to screen,
                    ),
            )
        val appScope = container.openScope(app)
        val firstScreen = appScope.openChild(screen)
        val firstViewModel = firstScreen.openChild(viewModel)
        val secondViewModel = firstScreen.openChild(viewModel)
        val secondScreen = appScope.openChild(screen)
        val firstScreenService = firstScreen.get(ScreenService::class)

        assertSame(firstViewModel.get(AppService::class), secondScreen.get(AppService::class))
        assertSame(firstScreenService, secondViewModel.get(ScreenService::class))
        assertNotSame(firstScreenService, secondScreen.get(ScreenService::class))
        assertSame(firstViewModel.get(ViewModelService::class), firstViewModel.get(ViewModelService::class))
        assertNotSame(firstViewModel.get(ViewModelService::class), secondViewModel.get(ViewModelService::class))

        firstViewModel.close()
        assertFailsWith<IllegalStateException> { firstViewModel.get(ViewModelService::class) }
        assertNotNull(secondViewModel.get(ViewModelService::class))
        firstScreen.close()
        assertFailsWith<IllegalStateException> { secondViewModel.get(ScreenService::class) }
        assertNotSame(firstScreenService, appScope.openChild(screen).get(ScreenService::class))
        assertNotNull(appScope.get(AppService::class))
    }

    @Test
    fun `앱에서 새 스코프 종류를 정의할 수 있고 스코프 밖에서는 생성하지 않는다`() {
        val custom = ScopeType("custom")
        val container = DependencyContainer(scopes = mapOf(DependencyKey(CustomService::class) to custom))
        val root = container.openScope(ScopeType("root"))

        assertFailsWith<IllegalStateException> { root.get(CustomService::class) }
        val customScope = root.openChild(custom)
        assertSame(customScope.get(CustomService::class), customScope.get(CustomService::class))
        customScope.close()
        assertFailsWith<IllegalStateException> { customScope.get(CustomService::class) }
        assertNotNull(root.openChild(custom).get(CustomService::class))
    }

    @Test
    fun `스코프가 없는 의존성은 요청할 때마다 생성한다`() {
        val scope = DependencyContainer().openScope(testScope)

        assertNotSame(scope.get(CustomService::class), scope.get(CustomService::class))
    }

    @Test
    fun `긴 스코프의 객체는 짧은 스코프의 객체를 붙잡지 않는다`() {
        val app = ScopeType("app")
        val screen = ScopeType("screen")
        val container =
            DependencyContainer(
                scopes =
                    mapOf(
                        DependencyKey(AppCapturesScreen::class) to app,
                        DependencyKey(ScreenService::class) to screen,
                    ),
            )
        val scope = container.openScope(app).openChild(screen)

        val exception = assertFailsWith<IllegalStateException> { scope.get(AppCapturesScreen::class) }
        assertContains(exception.message.orEmpty(), "screen")
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

private val testScope = ScopeType("test")

private class AppService

private class ViewModelService

private class ScreenService

private class AppCapturesScreen(
    val screen: ScreenService,
)

private class CustomService

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
