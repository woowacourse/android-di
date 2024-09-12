package woowacourse.shopping

import woowacourse.shopping.DependencyInjector.findInstance
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

typealias qualifier = KClass<*>?
typealias instance = Any

object DependencyInjector {
    private val instances = mutableMapOf<Pair<KClass<*>, qualifier>, instance>()

    private const val CONSTRUCTOR_NOT_FOUND = "적합한 생성자를 찾을 수 없습니다."
    private const val DEPENDENCY_TYPE_IS_INVALID = "의존성 클래스 타입이 올바르지 않습니다."

    fun <T : Any> findInstance(
        clazz: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T = instances[clazz to qualifier] as? T ?: createInstance(clazz, qualifier)

    fun <T : Any> addInstance(
        clazz: KClass<T>,
        instance: T,
        qualifier: KClass<*>? = null,
    ) {
        instances[clazz to qualifier] = instance
    }

    private fun <T : Any> createInstance(
        clazz: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        val constructor: KFunction<T> = getPrimaryConstructor(clazz)
        val dependencies: List<Any?> = constructor.extractDependencies()
        val instance = constructor.call(*dependencies.toTypedArray())

        injectFields(clazz, instance, qualifier)

        return instance
    }

    private fun <T : Any> getPrimaryConstructor(clazz: KClass<T>): KFunction<T> =
        clazz.primaryConstructor ?: throw IllegalArgumentException(CONSTRUCTOR_NOT_FOUND)

    private fun <T : Any> KFunction<T>.extractDependencies(): List<Any?> =
        parameters.map { parameter ->
            when (val classifier = parameter.type.classifier) {
                is KClass<*> -> findInstance(classifier)
//                {
//                    Log.d("hye classifier", "$classifier")
//                    val qualifier: KClass<*>? = when {
//                        parameter.hasAnnotation<RoomDB>() -> {
//                            Log.d("hye RoomDB", "${parameter.hasAnnotation<RoomDB>()}")
//                            RoomDB::class
//                        }
//                        parameter.hasAnnotation<InMemory>() -> {
//                            Log.d("hye InMemory", "${parameter.hasAnnotation<InMemory>()}")
//                            InMemory::class
//                        }
//                        else -> null
//                    }
//                    findInstance(classifier, qualifier)
//                }
                else -> throw IllegalArgumentException(DEPENDENCY_TYPE_IS_INVALID)
            }
        }

    private fun <T : Any> injectFields(
        clazz: KClass<T>,
        instance: T,
        qualifier: KClass<*>? = null,
    ) {
        clazz.declaredMemberProperties.forEach { kProperty ->
            if (kProperty.hasAnnotation<Inject>()) {
                val classifier: KClass<*> = kProperty.returnType.classifier as KClass<*>
                val dependency = findInstance(classifier, qualifier)

                kProperty as KMutableProperty1
                kProperty.setter.call(instance, dependency)
            }
        }
    }
}
