package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    // 객체 목록 (Key, Value) -> (클래스, 클래스 객체)
    val objectsMap: MutableMap<Any, Any> = mutableMapOf()

    fun hasObject(modelClass: Class<*>): Boolean = objectsMap.keys.contains(modelClass)

    // 객체를 탐색한다.
    fun <T : Any> searchObject(modelClass: Class<T>): T {
        if (hasObject(modelClass)) {
            return objectsMap[modelClass] as? T ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            val instance = createObject(modelClass)
            objectsMap[modelClass] = instance
            return instance
        }
    }

    // 만약 객체 목록 안에 클래스 key가 존재한다면, 해당 객체를 반환한다.
    // 그게 아니라면, 객체를 만들어서 추가하고 반환한다.
    // 지금 구조에서는 찾기와 객체 생성이 함께 꼬여있다. 이를 분리하면 객체를 잘 탐색하는지, 생성하는지를 알 수 있지 않을까?
    fun <T : Any> createObject(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor!!
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        if (types.isEmpty()) { // 파라미터가 없으면 그냥 생성한다.
            return constructor.call()
        } else { // 그게 아니라면 다시 탐색해서 객체를 찾아온다.
            val typesConstructors = types.map { type ->
                searchObject(type.java)
            }
            return constructor.call(*typesConstructors.toTypedArray())
        }
    }

    class DiViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return createObject(modelClass)
        }
    }
}
