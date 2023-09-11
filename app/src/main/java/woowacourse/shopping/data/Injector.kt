package woowacourse.shopping.data

import androidx.lifecycle.ViewModel
import woowacourse.shopping.annotation.KoalaRepository
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties

object Injector {

    fun <T : ViewModel> inject(modelClass: Class<T>): T {
        val properties = modelClass.kotlin.declaredMemberProperties
        val viewModel = modelClass.newInstance()

        properties.forEach { property ->
            property.annotations.forEach { annotation ->
                if (annotation is KoalaRepository && property is KMutableProperty1<*, *>) {
                    property.setter.call(viewModel, getRepositoryWithType(property.returnType))
                }
            }
        }

        return viewModel
    }

    private fun getRepositoryWithType(type: KType): Any {
        return RepositoryContainer::class.declaredMemberProperties.find {
            it.returnType == type
        }?.call(RepositoryContainer) ?: throw IllegalStateException("해당 타입을 찾을 수 없습니다.")
    }
}
