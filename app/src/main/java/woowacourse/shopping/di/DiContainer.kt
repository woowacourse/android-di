package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private val instanceMap: MutableMap<Any, Any> = mutableMapOf()

    // 인터페이스의 경우 어떤 클래스를 구현해야할지 매핑해서 알려준다.
    private val providerMap: MutableMap<Any, Any> = mutableMapOf()

    fun hasObject(modelClass: Class<*>): Boolean = instanceMap.keys.contains(modelClass)

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
    fun <T : Any> searchObject(modelClass: Class<T>): T {
        if (hasObject(modelClass)) {
            return instanceMap[modelClass] as? T ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            val modelClass = filterModelClass(modelClass)
            val instance = createObject(modelClass)
            instanceMap[modelClass] = instance
            return instance
        }
    }

    fun <T : Any> createObject(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("생성자를 찾을 수 없습니다 : $modelClass")
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        if (types.isEmpty()) { // 파라미터가 없으면 그냥 생성한다.
            return constructor.call()
        } else { // 그게 아니라면 다시 탐색해서 객체를 찾아온다.
            val typesConstructors =
                types.map { type ->
                    searchObject(type.java)
                }
            return constructor.call(*typesConstructors.toTypedArray())
        }
    }

    class DiViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = createObject(modelClass)
    }
}
