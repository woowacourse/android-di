package woowacourse.shopping.data

import androidx.lifecycle.ViewModel
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object Injector {

    fun <T : ViewModel> inject(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor ?: throw NullPointerException("주 생성자가 null 입니다")
        val args = constructor.parameters.map {
            getRepositoryWithType(it.type)
        }

        return constructor.call(*args.toTypedArray())
    }

    private fun getRepositoryWithType(type: KType): Any {
        return RepositoryContainer::class.declaredMemberProperties.find {
            it.returnType == type
        }?.call(RepositoryContainer) ?: throw IllegalStateException("해당 타입을 찾을 수 없습니다.")
    }
}
