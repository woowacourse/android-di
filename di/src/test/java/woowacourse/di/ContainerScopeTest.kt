package woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ContainerScopeTest {
    private class ActivityService

    private class ViewModelService

    private class FragmentScopedService

    private class ServiceScopedService

    private class ActivityTarget {
        @InjectField
        lateinit var service: ActivityService
    }

    private class FragmentTarget {
        @InjectField
        lateinit var service: FragmentScopedService
    }

    private class FragmentWithActivityTarget {
        @InjectField
        lateinit var activityService: ActivityService

        @InjectField
        lateinit var fragmentService: FragmentScopedService
    }

    private class ServiceTarget {
        @InjectField
        lateinit var service: ServiceScopedService
    }

    @Test
    fun `같은_액티비티_스코프에서는_같은_인스턴스가_주입된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
            }
        val activityKey = Any()

        // when
        val firstTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(activityKey))
            }
        val secondTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(activityKey))
            }

        // then
        assertThat(firstTarget.service).isSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `액티비티가_바뀌면_새로운_인스턴스가_주입된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
            }

        // when
        val firstTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(Any()))
            }
        val secondTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(Any()))
            }

        // then
        assertThat(firstTarget.service).isNotSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `ViewModel_스코프는_ViewModel_키를_기반으로_유지된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ViewModelService::class, scopeType = ScopeType.VIEW_MODEL) { ViewModelService() }
            }
        val firstContext = ScopeContext.viewModel("viewModel")
        val secondContext = ScopeContext.viewModel("another")

        // when
        val firstInstance = container.get(ViewModelService::class, scopeContext = firstContext)
        val reusedInstance = container.get(ViewModelService::class, scopeContext = firstContext)
        val anotherInstance = container.get(ViewModelService::class, scopeContext = secondContext)

        // then
        assertThat(firstInstance).isSameInstanceAs(reusedInstance)
        assertThat(firstInstance).isNotSameInstanceAs(anotherInstance)
    }

    @Test
    fun `프래그먼트_스코프에서는_같은_프래그먼트에_같은_인스턴스가_주입된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(FragmentScopedService::class, scopeType = ScopeType.FRAGMENT) { FragmentScopedService() }
            }
        val fragmentKey = Any()

        // when
        val firstTarget =
            FragmentTarget().also {
                FieldInjector.inject(it, container, ScopeContext.fragment(fragmentKey))
            }
        val secondTarget =
            FragmentTarget().also {
                FieldInjector.inject(it, container, ScopeContext.fragment(fragmentKey))
            }

        // then
        assertThat(firstTarget.service).isSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `프래그먼트_스코프는_액티비티_스코프와_함께_사용될_수_있다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
                bindScoped(FragmentScopedService::class, scopeType = ScopeType.FRAGMENT) { FragmentScopedService() }
            }
        val activityKey = Any()
        val fragmentKey = Any()

        // when
        val activityTarget =
            ActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.activity(activityKey))
            }
        val fragmentTarget =
            FragmentWithActivityTarget().also {
                FieldInjector.inject(it, container, ScopeContext.fragment(activityKey, fragmentKey))
            }
        val anotherFragmentTarget =
            FragmentTarget().also {
                FieldInjector.inject(it, container, ScopeContext.fragment(activityKey, fragmentKey))
            }

        // then
        assertThat(fragmentTarget.activityService).isSameInstanceAs(activityTarget.service)
        assertThat(fragmentTarget.fragmentService).isSameInstanceAs(anotherFragmentTarget.service)
    }

    @Test
    fun `다른_프래그먼트_스코프에서는_새로운_인스턴스가_주입된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(FragmentScopedService::class, scopeType = ScopeType.FRAGMENT) { FragmentScopedService() }
            }

        // when
        val firstTarget =
            FragmentTarget().also {
                FieldInjector.inject(it, container, ScopeContext.fragment(Any()))
            }
        val secondTarget =
            FragmentTarget().also {
                FieldInjector.inject(it, container, ScopeContext.fragment(Any()))
            }

        // then
        assertThat(firstTarget.service).isNotSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `서비스_스코프에서는_같은_서비스에_같은_인스턴스가_주입된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ServiceScopedService::class, scopeType = ScopeType.SERVICE) { ServiceScopedService() }
            }
        val serviceKey = Any()

        // when
        val firstTarget =
            ServiceTarget().also {
                FieldInjector.inject(it, container, ScopeContext.service(serviceKey))
            }
        val secondTarget =
            ServiceTarget().also {
                FieldInjector.inject(it, container, ScopeContext.service(serviceKey))
            }

        // then
        assertThat(firstTarget.service).isSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `서비스_스코프가_다르면_새로운_인스턴스가_주입된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ServiceScopedService::class, scopeType = ScopeType.SERVICE) { ServiceScopedService() }
            }

        // when
        val firstTarget =
            ServiceTarget().also {
                FieldInjector.inject(it, container, ScopeContext.service(Any()))
            }
        val secondTarget =
            ServiceTarget().also {
                FieldInjector.inject(it, container, ScopeContext.service(Any()))
            }

        // then
        assertThat(firstTarget.service).isNotSameInstanceAs(secondTarget.service)
    }

    @Test
    fun `스코프를_정리하면_새로운_인스턴스가_생성된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(ActivityService::class, scopeType = ScopeType.ACTIVITY) { ActivityService() }
            }
        val activityKey = Any()
        val context = ScopeContext.activity(activityKey)

        // when
        val first = container.get(ActivityService::class, scopeContext = context)
        container.clearScope(ScopeType.ACTIVITY, activityKey)
        val second = container.get(ActivityService::class, scopeContext = context)

        // then
        assertThat(first).isNotSameInstanceAs(second)
    }
}
