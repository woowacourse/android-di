package woowacourse.shopping.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.common.DIError.NotFoundInstanceForInject
import woowacourse.shopping.common.DIError.NotFoundPrimaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class CommonViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val primaryConstructor: KFunction<T> = modelClass.kotlin.primaryConstructor
            ?: throw NotFoundPrimaryConstructor()

        val classInstance: T = primaryConstructor.call(
            *getPrimaryConstructorPropertiesForInject(primaryConstructor).toTypedArray(),
        )

        val fieldsNeedInject: List<KProperty1<out T, *>> =
            getFieldsNeedInject(classInstance, primaryConstructor)

        injectFields(fieldsNeedInject, classInstance)

        return classInstance
    }

    private fun <T : ViewModel> getPrimaryConstructorPropertiesForInject(primaryConstructor: KFunction<T>): List<Any> {
        if (primaryConstructor.hasAnnotation<BandalInject>()) {
            val primaryConstructorParameters: List<KClass<*>> =
                primaryConstructor.parameters.map { kParameter -> kParameter.type.jvmErasure }

            return getPropertiesForInject(primaryConstructorParameters)
        }
        return emptyList()
    }

    private fun getPropertiesForInject(needParameters: List<KClass<*>>): List<Any> {
        return needParameters.map { needParameter ->
            val needType: KClass<*> = needParameter
            appContainer.getInstance(needType)
                ?: throw NotFoundInstanceForInject(needType)
        }
    }

    private fun <T : ViewModel> getFieldsNeedInject(
        classInstance: T,
        primaryConstructor: KFunction<T>,
    ): List<KProperty1<out T, *>> = classInstance::class.declaredMemberProperties
        .filter { property -> property.hasAnnotation<BandalInject>() }
        .filterNot { property ->
            primaryConstructor.parameters
                .map { parameter -> parameter.name }
                .contains(property.name)
        }

    private fun <T : ViewModel> injectFields(
        memberProperties: List<KProperty1<out T, *>>,
        instance: T,
    ) {
        memberProperties.forEach { property ->
            property.isAccessible = true
            (property as KMutableProperty<*>).setter.call(
                instance,
                appContainer.getInstance(property.returnType.jvmErasure)
                    ?: throw NotFoundInstanceForInject(property.returnType.jvmErasure),
            )
        }
    }
}
