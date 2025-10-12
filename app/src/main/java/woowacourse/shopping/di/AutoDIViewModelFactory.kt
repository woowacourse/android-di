package woowacourse.shopping.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.annotation.InjectClass
import woowacourse.shopping.di.annotation.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

class AutoDIViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = create(modelClass, CreationExtras.Empty)

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val appDependencies = extractAppDependencies(extras)
        val kClass = modelClass.kotlin

        val instance = createInstance(kClass, extras, appDependencies)

        if (kClass.hasAnnotation<InjectClass>()) {
            injectFields(instance, kClass, appDependencies)
        }

        return instance
    }

    private fun extractAppDependencies(extras: CreationExtras): AppDependencies =
        (extras[APPLICATION_KEY] as? AppDependencies)
            ?: throw IllegalStateException("extras에 AppDependencies가 없습니다.")

    private fun <T : ViewModel> createInstance(
        kClass: KClass<T>,
        extras: CreationExtras,
        appDependencies: AppDependencies,
    ): T {
        val constructor =
            kClass.primaryConstructor ?: return kClass.java.getDeclaredConstructor().newInstance()

        val args =
            constructor.parameters
                .associateWith { param ->
                    when (param.type.classifier) {
                        SavedStateHandle::class -> extras.createSavedStateHandle()
                        CartRepository::class -> appDependencies.cartRepository
                        ProductRepository::class -> appDependencies.productRepository
                        else -> if (param.isOptional) null else throw IllegalArgumentException("주입 불가: ${param.name}")
                    }
                }.filterValues { it != null }

        return constructor.callBy(args)
    }

    private fun <T : ViewModel> injectFields(
        instance: T,
        kClass: KClass<out ViewModel>,
        appDependencies: AppDependencies,
    ) {
        kClass.declaredMemberProperties.forEach { property ->
            val field = property.javaField ?: return@forEach
            if (field.isAnnotationPresent(InjectField::class.java)) {
                val dependency =
                    when (property.returnType.classifier) {
                        CartRepository::class -> appDependencies.cartRepository
                        ProductRepository::class -> appDependencies.productRepository
                        else -> throw IllegalArgumentException("필드 주입 불가: ${property.returnType.classifier}")
                    }
                field.isAccessible = true
                field.set(instance, dependency)
            }
        }
    }
}
