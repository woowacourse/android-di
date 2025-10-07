package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyFactory {
    fun <T : Any> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("주 생성자가 없습니다: ${modelClass.simpleName}")

        val params =
            constructor.parameters.associateWith { param ->
                val mKClass = param.type.classifier as? KClass<*>
                    ?: throw IllegalArgumentException("KClass<*>로 캐스팅할 수 없습니다: ${param.type.classifier}")
                Dependency.get(mKClass)
            }

        return constructor.callBy(params)
    }
}
