package com.mission.androiddi.component.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

abstract class InjectableActivity : AppCompatActivity() {
    abstract val activityClazz: KClass<out InjectableActivity>
    private val dependencyLifecycleObserver by lazy {
        val injector = getDependencyInjector().apply {
            injectActivityContext()
            injectActivityMembers()
        }
        ActivityDependencyLifecycleObserver(injector, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(dependencyLifecycleObserver)
    }

    private fun getDependencyInjector(): Injector {
        return NonConfigurationActivityInjectorManager.getInjector(this, activityClazz.qualifiedName)
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
        lifecycle.removeObserver(dependencyLifecycleObserver)
    }
}
