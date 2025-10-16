package woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ContainerScopeTest {
    private class ActivityService

    private class ViewModelService

    private class ActivityTarget {
        @InjectField
        lateinit var service: ActivityService
    }

    @Test
    fun `같은_액티비티_스코프에서는_같은_인스턴스가_주입된다`() {
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
            }
        val activityKey = Any()

        val firstTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(activityKey))
            }
        val secondTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(activityKey))
            }

        assertThat(firstTarget.service).isSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `액티비티가_바뀌면_새로운_인스턴스가_주입된다`() {
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
            }

        val firstTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(Any()))
            }
        val secondTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(Any()))
            }

        assertThat(firstTarget.service).isNotSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `ViewModel_스코프는_ViewModel_키를_기반으로_유지된다`() {
        val container =
            Container().apply {
                bindScoped(ViewModelService::class, scopeType = ScopeType.VIEW_MODEL) { ViewModelService() }
            }
        val firstContext = ScopeContext.viewModel("viewModel")
        val secondContext = ScopeContext.viewModel("another")

        val firstInstance = container.get(ViewModelService::class, scopeContext = firstContext)
        val reusedInstance = container.get(ViewModelService::class, scopeContext = firstContext)
        val anotherInstance = container.get(ViewModelService::class, scopeContext = secondContext)

        assertThat(firstInstance).isSameInstanceAs(reusedInstance)
        assertThat(firstInstance).isNotSameInstanceAs(anotherInstance)
    }

    @Test
    fun `스코프를_정리하면_새로운_인스턴스가_생성된다`() {
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
            }
        val activityKey = Any()
        val context = ScopeContext.activity(activityKey)

        val first = container.get(ActivityService::class, scopeContext = context)
        container.clearScope(ScopeType.ACTIVITY, activityKey)
        val second = container.get(ActivityService::class, scopeContext = context)

        assertThat(first).isNotSameInstanceAs(second)
    }
}
