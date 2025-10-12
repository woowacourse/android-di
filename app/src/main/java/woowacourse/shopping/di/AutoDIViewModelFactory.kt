package woowacourse.shopping.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.InjectClass
import woowacourse.shopping.di.annotation.InjectField
import woowacourse.shopping.di.annotation.RoomDb
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
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
            kClass.primaryConstructor
                ?: return kClass.java.getDeclaredConstructor().newInstance()

        val args =
            constructor.parameters
                .associateWith { param ->
                    getDependency(param, appDependencies, extras)
                        ?: if (param.isOptional) {
                            null
                        } else {
                            throw IllegalArgumentException("주입 불가: ${param.name}")
                        }
                }.filterValues { it != null }

        return constructor.callBy(args)
    }

    private fun getDependency(
        param: KParameter,
        appDependencies: AppDependencies,
        extras: CreationExtras,
    ): Any? {
        val annotations = param.annotations
        val isInMemory = annotations.any { it.annotationClass == InMemory::class }
        val isRoom = annotations.any { it.annotationClass == RoomDb::class }

        return when (param.type.classifier) {
            SavedStateHandle::class -> extras.createSavedStateHandle()
            CartRepository::class ->
                when {
                    isInMemory -> appDependencies.inMemoryCartRepository
                    isRoom -> appDependencies.roomCartRepository
                    else -> appDependencies.roomCartRepository
                }

            ProductRepository::class -> appDependencies.productRepository

            else -> null
        }
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
                    getFieldDependency(property, appDependencies)
                        ?: throw IllegalArgumentException("필드 주입 불가: ${property.name}")
                field.isAccessible = true
                field.set(instance, dependency)
            }
        }
    }

    private fun getFieldDependency(
        prop: KProperty1<*, *>,
        appDependencies: AppDependencies,
    ): Any? {
        val annotations = prop.annotations
        val isInMemory = annotations.any { it.annotationClass == InMemory::class }
        val isRoom = annotations.any { it.annotationClass == RoomDb::class }

        return when (prop.returnType.classifier) {
            CartRepository::class ->
                when {
                    isInMemory -> appDependencies.inMemoryCartRepository
                    isRoom -> appDependencies.roomCartRepository
                    else -> appDependencies.roomCartRepository
                }

            ProductRepository::class -> appDependencies.productRepository

            else -> null
        }
    }
}
