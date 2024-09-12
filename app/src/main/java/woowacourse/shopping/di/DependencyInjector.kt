package woowacourse.shopping.di

import javax.inject.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DependencyInjector(private val registry: DependencyRegistry) {
    // classType에 싱글톤 어노테이션이 있고, registry에 인스턴스가 존재하면 가져온다.
    // 아니면 새로 만든다.
    fun inject(classType: KClass<*>): Any {
        val isSingleton = classType.findAnnotation<Singleton>() != null
        return registry.getInstanceOrNull(classType)?.takeIf { isSingleton } ?: createInstance(
            classType,
        )
    }

    private fun createInstance(classType: KClass<*>): Any {
        val constructor = getPrimaryConstructor(classType)
        val parameterDependencies = resolveConstructorParameters(constructor.parameters)
        val instance = constructor.callBy(parameterDependencies)
        injectFields(instance)
        registry.addInstance(classType, instance)
        return instance
    }

    private fun <T : Any> getPrimaryConstructor(classType: KClass<T>): KFunction<T> {
        return classType.primaryConstructor
            ?: throw IllegalArgumentException("${classType.qualifiedName}의 주생성자가 존재하지 않습니다.")
    }

    private fun resolveConstructorParameters(parameters: List<KParameter>): Map<KParameter, Any?> {
        return parameters.associateWith { parameter ->
            (parameter.type.classifier as? KClass<*>)?.let { classifier ->
                inject(classifier)
            }
        }
    }

    private fun <T : Any> injectFields(target: T) {
        target::class.declaredMemberProperties
            .filter { kProperty ->
                kProperty.findAnnotation<Inject>() != null && kProperty is KMutableProperty1<*, *>
            }.forEach { kProperty ->
                val mutableProperty = kProperty as KMutableProperty1<out T, *>
                injectProperty(target, mutableProperty)
            }
    }

    private fun <T : Any> injectProperty(
        target: T,
        mutableProperty: KMutableProperty1<out T, *>,
    ) {
        val classifier = mutableProperty.returnType.classifier as? KClass<*>
        classifier?.let { kClass ->
            val instance = inject(kClass)
            mutableProperty.isAccessible = true
            mutableProperty.setter.call(target, instance)
        }
    }
}
