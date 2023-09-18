package com.mission.androiddi.component.fragment

import com.mission.androiddi.scope.FragmentScope
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

class FragmentDependencyInjector(
    cache: Cache = DefaultCache(),
) : Injector(cache) {
    override val scopeAnnotation: KClass<out Annotation> = FragmentScope::class
}
