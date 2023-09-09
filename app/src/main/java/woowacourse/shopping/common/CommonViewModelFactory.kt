package woowacourse.shopping.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object CommonViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val primaryConstructorParameters = getPrimaryConstructorParameters(modelClass)
        val injectProperties = getPropertiesForInject(primaryConstructorParameters)

        return modelClass.constructors.first().newInstance(*injectProperties.toTypedArray()) as T
    }

    private fun <T : ViewModel> getPrimaryConstructorParameters(modelClass: Class<T>): List<KParameter> {
        return modelClass.kotlin.primaryConstructor?.parameters
            ?: throw NullPointerException("주 생성자를 찾을 수 없습니다.")
    }

    private fun getPropertiesForInject(needParameters: List<KParameter>): List<Any?> {
        val injectProperties = mutableListOf<Any?>()
        val defaultProperties =
            ShoppingApplication.defaultAppContainer::class.declaredMemberProperties

        needParameters.forEach { needParameter ->
            val inject =
                defaultProperties.find { it.returnType == needParameter.type }?.let {
                    it.isAccessible = true
                    it.getter.call(ShoppingApplication.defaultAppContainer)
                }
            injectProperties.add(inject)
        }
        return injectProperties
    }
}
