package woowacourse.shopping.data

import androidx.lifecycle.ViewModel
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object Injector {

    fun <T : ViewModel> inject(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor ?: throw IllegalStateException()
        val args = constructor.parameters.map {
            getRepositoryWithType(it.type)
        }

        return constructor.call(*args.toTypedArray())
    }

    private fun getRepositoryWithType(type: KType): Any {
        return RepositoryContainer::class.declaredMemberProperties.find {
            it.returnType == type
        }?.call(RepositoryContainer) ?: throw IllegalStateException()
    }
}
