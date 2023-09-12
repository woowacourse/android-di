package woowacourse.shopping.di.module

import android.content.Context
import kotlin.reflect.KClass

interface DependencyModule{
    fun invoke(context: Context): Map<KClass<*>, KClass<*>>
}