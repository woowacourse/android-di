package woowacourse.shopping.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.annotation.InjectClass
import woowacourse.shopping.di.annotation.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

class AutoDIViewModelFactory(
    private val dependencies: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = create(modelClass, CreationExtras.Empty)

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val constructor: KFunction<T>? = kClass.primaryConstructor

        val instance: T =
            if (constructor != null) {
                val args =
                    constructor.parameters
                        .associateWith { param ->
                            when {
                                param.type.classifier == SavedStateHandle::class -> extras.savedStateHandle()
                                dependencies[param.type.classifier] != null -> dependencies[param.type.classifier]
                                param.isOptional -> null
                                else -> throw IllegalArgumentException("생성자 주입 불가: ${param.name}")
                            }
                        }.filterValues { it != null }

                constructor.callBy(args)
            } else {
                modelClass.getDeclaredConstructor().newInstance()
            }

        if (kClass.hasAnnotation<InjectClass>()) {
            injectFields(instance)
        }

        return instance
    }

    private fun injectFields(target: Any) {
        target::class
            .allSuperclasses
            .plus(target::class)
            .forEach { clazz ->
                clazz.declaredMemberProperties.forEach { property ->
                    val field = property.javaField ?: return@forEach
                    if (field.isAnnotationPresent(InjectField::class.java)) {
                        val type = property.returnType.classifier as? KClass<*>
                        val dependency =
                            dependencies[type]
                                ?: throw IllegalArgumentException("필드 주입 불가: ${property.name} (${type?.simpleName})")
                        field.isAccessible = true
                        field.set(target, dependency)
                    }
                }
            }
    }

    private fun CreationExtras.savedStateHandle(): SavedStateHandle = createSavedStateHandle()
}
