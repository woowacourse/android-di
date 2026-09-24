package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class DiManager {
    private val instanceMap: MutableMap<Any, Any> = mutableMapOf()

    // 인터페이스의 경우 어떤 클래스를 구현해야할지 매핑해서 알려준다.
    private val providerMap: MutableMap<Any, Any> = mutableMapOf()

    fun hasInstance(modelClass: Class<*>): Boolean = instanceMap.keys.contains(modelClass)

    fun addInstance(
        key: Class<*>,
        value: Any,
    ) {
        instanceMap[key] = value
    }

    fun addProvider(
        key: Class<*>,
        value: Any,
    ) {
        providerMap[key] = value
    }

    // 인터페이스를 받았을 때 구현할 구현체의 클래스가 무엇인지 조건을 구분한다.
    // 만약 providerMap에 없으면 modelClass를 반환한다.
    fun <T : Any> filterModelClass(modelClass: Class<T>): Class<T> =
        providerMap.getOrElse(modelClass) {
            modelClass
        } as Class<T>

    // 객체를 탐색한다.
    fun <T : Any> searchInstance(modelClass: Class<T>): T {
        if (hasInstance(modelClass)) {
            return instanceMap[modelClass] as? T ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            val instance = createInstance(modelClass)
            instanceMap[modelClass] = instance
            return instance
        }
    }

    fun <T : Any> createInstance(modelClass: Class<T>): T {
        val modelClass = filterModelClass(modelClass)
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("생성자를 찾을 수 없습니다 : $modelClass")
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        if (types.isEmpty()) { // 파라미터가 없으면 그냥 생성한다.
            return constructor.call()
        } else { // 그게 아니라면 다시 탐색해서 객체를 찾아온다.
            val typesConstructors =
                types.map { type ->
                    searchInstance(type.java)
                }
            return constructor.call(*typesConstructors.toTypedArray())
        }
    }

    fun <T : Any> fieldInject(modelClass: Class<T>): T {
        val instance = modelClass.kotlin.primaryConstructor?.call() ?: throw IllegalArgumentException("인스턴스를 생성할 수 없어요. $modelClass")
        val lateinitProperties =
            modelClass.kotlin.memberProperties.filter { property ->
                property.isLateinit
            }
        lateinitProperties.forEach {
            modelClass.getDeclaredField(it.name).apply {
                val dependancyKClass = it.returnType.classifier as? KClass<*> ?: throw IllegalArgumentException("프로퍼티 타입을 찾울 수 없어요: $it")
                isAccessible = true
                set(instance, searchInstance(dependancyKClass.java))
            }
        }
        return instance
    }

    inner class DiViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = fieldInject(modelClass)
    }
}
