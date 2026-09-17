package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object KirbyDIFactory : ViewModelProvider.Factory {
    private val dependencyInstances = mutableMapOf<KClass<*>, Any>()

    // ViewModelProvider.Factory의 create는 ViewModel 타입을 Java Class로 받습니다.
    // 때문에 createInstance에서 주 생성자를 Kotlin 리플렉션으로 찾을 수 있도록 KClass로 변환해야 하네요!
    override fun <T : ViewModel> create(modelClass: Class<T>): T = createInstance(modelClass.kotlin)

    private fun <T : Any> createInstance(type: KClass<T>): T {
        // 요청받은 클래스의 주 생성자를 찾습니다.
        val constructor =
            type.primaryConstructor ?: throw IllegalArgumentException("주 생성자가 없습니다: $type")
        // 생성자 파라미터 순서대로 각 자리에 넣을 의존성 객체를 찾습니다.
        val arguments =
            constructor.parameters.map { parameter ->
                // 파라미터의 타입 정보가 createInstance에 전달할 수 있는 KClass인지 확인합니다.
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("의존성 타입을 확인할 수 없습니다: $parameter")
                // 이미 만든 객체가 있으면 재사용하고, 없으면 새로 만듭니다.
                resolveDependency(dependencyType)
            }

        // 각 객체들을 생성자의 인자로 전달합니다.
        return constructor.call(*arguments.toTypedArray())
    }

    private fun resolveDependency(type: KClass<*>): Any {
        // 같은 타입의 의존성 인스턴스가 이미 있으면 그 인스턴스를 반환합니다.
        dependencyInstances[type]?.let { return it }

        // 없으면 생성자를 따라 객체를 만들고 다음 요청에서도 쓰도록 보관합니다.
        val instance = createInstance(type)
        dependencyInstances[type] = instance
        return instance
    }
}
