package woowacourse.shopping.di

import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
open class Module(private val parentModule: Module? = null) {
    private val cache: MutableMap<String, Any> = mutableMapOf()

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
                kParameter.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }?.let {
                    getInstance {
                        findInstance(
                            kParameter.type.jvmErasure,
                            it.annotationClass,
                        )
                    }
                } ?: run {
                    findInstance(kParameter.type.jvmErasure)
                }
            instance
        }

    private fun findInstance(kClass: KClass<*>, qualifier: KClass<out Annotation>? = null): Any {
        val instances = searchFunctions(kClass, qualifier)
        return when (instances.size) {
            0 -> {
                createInstanceForPrimaryConstructor(kClass)
            }

            1 -> {
                val moduleFunction = instances.entries.first()
                createInstanceForFunction(moduleFunction.key, moduleFunction.value)
            }

            else -> {
                throw IllegalStateException(ERROR_CAN_NOT_INJECT)
            }
        }
    }

    private fun searchFunctions(
        kClass: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Map<KFunction<*>, Module> {
        return this::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .associateWith { this@Module }
            .filter { (func, _) -> kClass.isSubclassOf(func.returnType.jvmErasure) }
            .filter { (func, _) ->
                qualifier?.let {
                    hasQualifierAtFunc(func, it)
                } ?: true
            }.takeUnless { it.isEmpty() }
            ?: parentModule?.searchFunctions(kClass, qualifier) ?: mapOf()
    }

    private fun hasQualifierAtFunc(func: KFunction<*>, qualifier: KClass<out Annotation>): Boolean {
        return qualifier ==
            func.annotations.firstOrNull {
                it.annotationClass.hasAnnotation<Qualifier>()
            }?.annotationClass
    }

    private fun createInstanceForPrimaryConstructor(kClass: KClass<*>): Any {
        val primaryConstructor = kClass.primaryConstructor ?: throw NullPointerException(
            ERROR_PRIMARY_CONSTRUCTOR,
        )
        val arguments = getInstances(primaryConstructor)
        return primaryConstructor.call(*arguments.toTypedArray())
    }

    private fun <T : Any> createInstanceForFunction(
        kFunction: KFunction<*>,
        module: Module,
    ): T {
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

    private inline fun <reified T : Any> getInstance(create: () -> T): T {
        val qualifiedName = requireNotNull(T::class.qualifiedName)
        val instance = cache[qualifiedName] ?: create().also {
            cache[qualifiedName] = it
        }
        return instance as T
    }

    companion object {
        private const val ERROR_PRIMARY_CONSTRUCTOR = "[ERROR] 주 생성자가 없습니다."
        private const val ERROR_CAN_NOT_INJECT = "[ERROR] 주입할 수 없습니다."
    }
}
