package com.mission.androiddi.component.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injectable
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass

abstract class InjectableFragment : Fragment(), Injectable {
    abstract val fragmentClazz: KClass<out InjectableFragment>
    override val cache: Cache by lazy { createCache() }
    private val dependencyInjector by lazy {
        FragmentDependencyInjector(cache)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dependencyInjector.injectFragmentMembers()
    }

    private fun Injector.injectFragmentMembers() {
        injectMemberProperties(fragmentClazz, this@InjectableFragment)
    }

    private fun createCache(): Cache {
        val parentActivity = requireActivity()
        if (parentActivity is Injectable) {
            return DefaultCache(parentActivity.cache)
        }
        return DefaultCache()
    }
}
