package woowacourse.shopping.data

import androidx.lifecycle.ViewModel
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object Injector {

    fun <T : ViewModel> inject(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor ?: throw IllegalStateException()
        val params = constructor.parameters
        val args = params.map {
            getService(it.type)
        }.toTypedArray()

        return constructor.call(*args)
    }

    private fun getService(type: KType): Any {
        return RepositoryContainer::class.declaredMemberProperties.find {
            it.returnType == type
        }?.let {
            it.isAccessible = true
            it.getter.call(RepositoryContainer)
        } ?: throw IllegalStateException()
    }
}
