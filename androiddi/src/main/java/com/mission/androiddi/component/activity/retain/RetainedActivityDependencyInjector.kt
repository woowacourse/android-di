package com.mission.androiddi.component.activity.retain

import com.mission.androiddi.scope.RetainActivityScope
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

class RetainedActivityDependencyInjector(
    cache: Cache = DefaultCache(),
) : Injector(cache) {
    override val scopeAnnotation: KClass<out Annotation> = RetainActivityScope::class
}
