package com.harodi

import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame

class DiManagerScopeTest {
    @Test
    fun `같은 ViewModel 스코프에서는 같은 인스턴스를 반환하고 다른 ViewModel 스코프에서는 다른 인스턴스를 반환한다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val firstViewModelScope = createViewModelScope(applicationScope)
        val secondViewModelScope = createViewModelScope(applicationScope)

        // when
        val firstRequest =
            diManager.resolve(
                modelClass = ScopedConsumer::class.java,
                scopeKey = firstViewModelScope,
            )
        val secondRequest =
            diManager.resolve(
                modelClass = ScopedConsumer::class.java,
                scopeKey = firstViewModelScope,
            )
        val requestFromAnotherScope =
            diManager.resolve(
                modelClass = ScopedConsumer::class.java,
                scopeKey = secondViewModelScope,
            )

        // then
        assertSame(firstRequest.viewModelScopedRepository, secondRequest.viewModelScopedRepository)
        assertNotSame(firstRequest.viewModelScopedRepository, requestFromAnotherScope.viewModelScopedRepository)
        assertSame(firstRequest.applicationScopedRepository, requestFromAnotherScope.applicationScopedRepository)
    }

    @Test
    fun `ViewModel 스코프를 제거하면 해당 스코프의 의존성만 새로 생성한다`() {
        // given
        val diManager = createDiManager()
        val applicationScope = createApplicationScope()
        val firstViewModelScope = createViewModelScope(applicationScope)
        val secondViewModelScope = createViewModelScope(applicationScope)
        val firstViewModelBeforeRemoval = diManager.resolve(ScopedConsumer::class.java, firstViewModelScope)
        val secondViewModelBeforeRemoval = diManager.resolve(ScopedConsumer::class.java, secondViewModelScope)

        // when
        diManager.removeScope(firstViewModelScope)
        val firstViewModelAfterRemoval = diManager.resolve(ScopedConsumer::class.java, firstViewModelScope)
        val secondViewModelAfterRemoval = diManager.resolve(ScopedConsumer::class.java, secondViewModelScope)

        // then
        assertNotSame(
            firstViewModelBeforeRemoval.viewModelScopedRepository,
            firstViewModelAfterRemoval.viewModelScopedRepository,
        )
        assertSame(
            secondViewModelBeforeRemoval.viewModelScopedRepository,
            secondViewModelAfterRemoval.viewModelScopedRepository,
        )
        assertSame(
            firstViewModelBeforeRemoval.applicationScopedRepository,
            firstViewModelAfterRemoval.applicationScopedRepository,
        )
    }

    @Test
    fun `Screen 스코프는 같은 화면에서 재사용하고 제거 후 새로 생성한다`() {
        // given
        val diManager =
            createDiManager().apply {
                addScopePolicy(
                    classType = ScreenScopedFormatter::class.java,
                    qualifier = null,
                    scopeKind = TestScopeKind.SCREEN,
                )
            }
        val applicationScope = createApplicationScope()
        val viewModelScope = createViewModelScope(applicationScope)
        val firstScreenScope = createScreenScope(viewModelScope)
        val secondScreenScope = createScreenScope(viewModelScope)

        // when
        val firstRequest = diManager.resolve(ScreenScopedConsumer::class.java, firstScreenScope)
        val repeatedRequest = diManager.resolve(ScreenScopedConsumer::class.java, firstScreenScope)
        val requestFromAnotherScreen = diManager.resolve(ScreenScopedConsumer::class.java, secondScreenScope)
        diManager.removeScope(firstScreenScope)
        val removedFormatter =
            diManager.searchScope(
                firstScreenScope,
                DependencyKey(ScreenScopedFormatter::class.java, null),
            )
        val requestAfterRemoval = diManager.resolve(ScreenScopedConsumer::class.java, firstScreenScope)

        // then
        assertSame(firstRequest.formatter, repeatedRequest.formatter)
        assertNotSame(firstRequest.formatter, requestFromAnotherScreen.formatter)
        assertNull(removedFormatter)
        assertNotSame(firstRequest.formatter, requestAfterRemoval.formatter)
        assertSame(requestFromAnotherScreen.formatter, diManager.resolve(ScreenScopedConsumer::class.java, secondScreenScope).formatter)
    }

    private fun createDiManager(): DiManager =
        DiManager().apply {
            addScopePolicy(
                classType = ViewModelScopedRepository::class.java,
                qualifier = null,
                scopeKind = TestScopeKind.VIEW_MODEL,
            )
            addScopePolicy(
                classType = ApplicationScopedRepository::class.java,
                qualifier = null,
                scopeKind = TestScopeKind.APPLICATION,
            )
        }

    private fun createApplicationScope(): ScopeKey =
        ScopeKey(
            parentKey = null,
            scopeKind = TestScopeKind.APPLICATION,
        )

    private fun createViewModelScope(applicationScope: ScopeKey): ScopeKey =
        ScopeKey(
            parentKey = applicationScope,
            scopeKind = TestScopeKind.VIEW_MODEL,
        )

    private fun createScreenScope(viewModelScope: ScopeKey): ScopeKey =
        ScopeKey(
            parentKey = viewModelScope,
            scopeKind = TestScopeKind.SCREEN,
        )
}

private enum class TestScopeKind : ScopeKind {
    APPLICATION,
    VIEW_MODEL,
    SCREEN,
}

class ScopedConsumer {
    @Inject
    lateinit var viewModelScopedRepository: ViewModelScopedRepository

    @Inject
    lateinit var applicationScopedRepository: ApplicationScopedRepository
}

class ViewModelScopedRepository

class ApplicationScopedRepository

class ScreenScopedConsumer {
    @Inject
    lateinit var formatter: ScreenScopedFormatter
}

class ScreenScopedFormatter
