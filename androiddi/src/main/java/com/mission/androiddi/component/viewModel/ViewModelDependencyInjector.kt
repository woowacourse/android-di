package com.mission.androiddi.component.viewModel

import com.mission.androiddi.scope.ViewModelScope
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

class ViewModelDependencyInjector(
    cache: Cache = DefaultCache(),
) : Injector(cache) {
    override val scopeAnnotation: KClass<out Annotation> = ViewModelScope::class
}
