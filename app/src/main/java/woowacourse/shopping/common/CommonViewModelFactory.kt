package woowacourse.shopping.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object CommonViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val primaryConstructor: KFunction<T> = modelClass.kotlin.primaryConstructor
            ?: throw NullPointerException("주 생성자를 찾을 수 없습니다.")

        val injectProperties = getPropertiesForInject(primaryConstructor.parameters)

        return primaryConstructor.call(*injectProperties.toTypedArray())
    }

    private fun getPropertiesForInject(needParameters: List<KParameter>): List<Any?> {
        val defaultProperties: Collection<KProperty1<out AppContainer, *>> =
            ShoppingApplication.defaultAppContainer::class.declaredMemberProperties

        return needParameters.map { needParameter ->
            defaultProperties.find { defaultProperty ->
                defaultProperty.returnType == needParameter.type
            }?.let { matchedProperty ->
                getDefaultProperty(matchedProperty)
            }
        }
    }

    private fun getDefaultProperty(matchedProperty: KProperty1<out AppContainer, *>): Any? {
        matchedProperty.isAccessible = true
        return matchedProperty.getter.call(ShoppingApplication.defaultAppContainer)
    }
}
