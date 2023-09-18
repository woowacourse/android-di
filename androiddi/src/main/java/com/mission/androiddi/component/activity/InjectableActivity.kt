package com.mission.androiddi.component.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mission.androiddi.component.activity.default.ActivityDependencyInjector
import com.mission.androiddi.component.activity.retain.RetainedActivityDependencyLifecycleObserver
import com.mission.androiddi.component.application.InjectableApplication
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.injector.Injectable
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

abstract class InjectableActivity : AppCompatActivity(), Injectable {
    abstract val activityClazz: KClass<out InjectableActivity>
    override val cache: Cache by lazy { DefaultCache(retainActivityDependencyInjector.cache) }

    private val activityDependencyInjector by lazy {
        ActivityDependencyInjector(cache)
    }
    private val retainActivityDependencyInjector by lazy {
        getDependencyInjector().apply { injectActivityDependencies() }
    }

    private val retainDependencyLifecycleObserver by lazy {
        RetainedActivityDependencyLifecycleObserver(retainActivityDependencyInjector, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDependencyInjector.injectActivityMembers()
        lifecycle.addObserver(retainDependencyLifecycleObserver)
    }

    private fun getDependencyInjector(): Injector {
        val injectableApplication = application as InjectableApplication
        val activityInjectManager = injectableApplication.requireActivityInjectManager()

        return activityInjectManager.getInjector(this, activityClazz.qualifiedName)
    }

    private fun Injector.injectActivityDependencies() {
        injectActivityContext()
        injectActivityMembers()
    }

    private fun Injector.injectActivityContext() {
        val contextKey = DependencyKey.createDependencyKey(Context::class)
        caching(contextKey, this@InjectableActivity)
    }

    private fun Injector.injectActivityMembers() {
        injectMemberProperties(activityClazz, this@InjectableActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(retainDependencyLifecycleObserver)
    }
}
