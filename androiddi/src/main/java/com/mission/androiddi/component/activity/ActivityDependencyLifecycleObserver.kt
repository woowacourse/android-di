package com.mission.androiddi.component.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.DependencyKey
import kotlin.reflect.KClass

class ActivityDependencyLifecycleObserver<out T : AppCompatActivity>(
    activityClazz: KClass<T>,
    private val activity: T,
) : DefaultLifecycleObserver {
    private lateinit var dependencyInjector: ActivityDependencyInjector

    init {
        val parentCache = activity.application
        if (parentCache is Cache) {
            dependencyInjector = ActivityDependencyInjector(DefaultCache(parentCache))
        }

        injectActivityContext(activity.baseContext)
        injectActivityMembers(activityClazz, activity)
    }

    private fun injectActivityContext(context: Context) {
        val contextKey = DependencyKey.createDependencyKey(Context::class)
        dependencyInjector.caching(contextKey, context)
    }

    private fun injectActivityMembers(activityClazz: KClass<T>, activity: T) {
        dependencyInjector.injectMemberProperties(activityClazz, activity)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (!activity.isChangingConfigurations) {
            dependencyInjector.clear()
        }
    }
}
