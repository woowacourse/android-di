package woowacourse.shopping.di

import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val module: Module,
) {
    fun <T : Any> inject(clazz: Class<T>): T {
        val primaryConstructor =
            clazz.kotlin.primaryConstructor ?: throw NullPointerException(ERROR_PRIMARY_CONSTRUCTOR)

        val instances = getInstances(primaryConstructor)
        val instance = primaryConstructor.call(*instances.toTypedArray()).apply {
            injectAnnotationFields(this)
        }

        return instance
    }

    private fun getInstances(kFunction: KFunction<*>): List<Any?> =
        kFunction.valueParameters.map { kParameter ->
            val instance =
                kParameter.findAnnotation<Qualifier>()?.let {
                    findInstance(it.clazz)
                } ?: run {
                    findInstance(kParameter.type.jvmErasure)
                }
            instance
        }

    private fun findInstance(kClass: KClass<*>): Any {
        val instances = module.searchFunctions(kClass)
        return when (instances.size) {
            0 -> {
                createInstanceForPrimaryConstructor(kClass)
            }

            1 -> {
                val moduleFunction = module.searchFunction(kClass)
                createInstanceForFunction(moduleFunction)
            }

            else -> {
                throw IllegalStateException(ERROR_CAN_NOT_INJECT)
            }
        }
    }

    private fun createInstanceForPrimaryConstructor(kClass: KClass<*>): Any {
        val primaryConstructor = kClass.primaryConstructor ?: throw NullPointerException(
            ERROR_PRIMARY_CONSTRUCTOR,
        )
        val arguments = getInstances(primaryConstructor)
        return primaryConstructor.call(*arguments.toTypedArray())
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createInstanceForFunction(kFunction: KFunction<*>): T {
        val arguments = getInstances(kFunction)
        return kFunction.call(module, *arguments.toTypedArray()) as T
    }

    fun <T : Any> injectAnnotationFields(instance: T) {
        val clazz = instance::class
        val fields = clazz.filterHasAnnotationFields<Inject>()
        when (fields.isEmpty()) {
            true -> return
            false -> {
                instance.injectFields(fields)
            }
        }
    }

    private fun <T : Any> T.injectFields(fields: List<Field>) {
        fields.forEach { field: Field ->
            val obj = findInstance(field.type.kotlin)
            field.set(this, obj)
        }
    }

    private inline fun <reified T : Annotation> KClass<*>.filterHasAnnotationFields(): List<Field> =
        declaredMemberProperties.filter {
            it.hasAnnotation<T>()
        }.map { kProperty ->
            requireNotNull(
                kProperty.apply {
                    isAccessible = true
                }.javaField,
            )
        }

    companion object {
        private const val ERROR_PRIMARY_CONSTRUCTOR = "[ERROR] 주 생성자가 없습니다."
        private const val ERROR_CAN_NOT_INJECT = "[ERROR] 주입할 수 없습니다."
    }
}
