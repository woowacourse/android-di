package com.harodi

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

data class DependencyKey(
    val classType: Class<*>,
    val qualifier: KClass<out Annotation>?,
)

class DiManager {
    private val instanceMap: MutableMap<DependencyKey, Any> = mutableMapOf()

    // 인터페이스의 경우 어떤 클래스를 구현해야할지 매핑해서 알려준다.
    private val providerMap: MutableMap<DependencyKey, Any> = mutableMapOf()

    fun addInstance(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        value: Any,
    ) {
        val dependencyKey = DependencyKey(classType, qualifier)
        instanceMap[dependencyKey] = value
    }

    fun addProvider(
        classType: Class<*>,
        qualifier: KClass<out Annotation>?,
        value: Any,
    ) {
        val dependencyKey = DependencyKey(classType, qualifier)
        providerMap[dependencyKey] = value
    }

    fun hasInstance(dependencyKey: DependencyKey): Boolean = instanceMap.keys.contains(dependencyKey)

    // 인터페이스를 받았을 때 구현할 구현체의 클래스가 무엇인지 조건을 구분한다.
    // 만약 providerMap에 없으면 modelClass를 반환한다.
    fun filterModelClass(dependencyKey: DependencyKey): Class<*> =
        providerMap.getOrElse(dependencyKey) {
            dependencyKey.classType
        } as Class<*>

    // 객체를 탐색한다.
    fun searchInstance(dependencyKey: DependencyKey): Any {
        if (hasInstance(dependencyKey)) {
            return instanceMap[dependencyKey] ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            val instance = createInstance(dependencyKey)
            instanceMap[dependencyKey] = instance
            return instance
        }
    }

    fun createInstance(dependencyKey: DependencyKey): Any {
        val modelClass = filterModelClass(dependencyKey)
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("생성자를 찾을 수 없습니다 : $modelClass")
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        if (types.isEmpty()) { // 파라미터가 없으면 그냥 생성한다.
            return constructor.call()
        } else { // 그게 아니라면 다시 탐색해서 객체를 찾아온다.
            val typesConstructors =
                types.map { type ->
                    val qualifier = type.annotations.firstOrNull { annotation ->
                        annotation.annotationClass.annotations.any {
                            it is Qualifier
                        }
                    }?.annotationClass
                    val dependencyKey = DependencyKey(type.java, qualifier)
                    searchInstance(dependencyKey)
                }
            return constructor.call(*typesConstructors.toTypedArray())
        }
    }

    fun <T : Any> fieldInject(modelClass: Class<T>): T {
        val instance = modelClass.kotlin.primaryConstructor?.call()
            ?: throw IllegalArgumentException("인스턴스를 생성할 수 없어요. $modelClass")
        val lateinitProperties =
            modelClass.kotlin.memberProperties.filter { property ->
                property.isLateinit && property.annotations.any { it is Inject }
            }
        lateinitProperties.forEach {
            modelClass.getDeclaredField(it.name).apply {
                val dependancyKClass = it.returnType.classifier as? KClass<*>
                    ?: throw IllegalArgumentException("프로퍼티 타입을 찾울 수 없어요: $it")
                val qualifier = it.annotations.firstOrNull { annotation ->
                    annotation.annotationClass.annotations.any {
                        it is Qualifier
                    }
                }?.annotationClass
                val dependencyKey = DependencyKey(dependancyKClass.java, qualifier)

                isAccessible = true
                set(instance, searchInstance(dependencyKey))
            }
        }
        return instance
    }
}
