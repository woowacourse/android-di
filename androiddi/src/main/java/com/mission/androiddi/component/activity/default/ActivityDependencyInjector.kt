package com.mission.androiddi.component.activity.default

import com.mission.androiddi.scope.ActivityScope
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

class ActivityDependencyInjector(
    cache: Cache = DefaultCache(),
) : Injector(cache) {
    override val scopeAnnotation: KClass<out Annotation> = ActivityScope::class
}
