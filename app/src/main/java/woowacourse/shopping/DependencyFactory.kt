package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties

object DependencyFactory {
    // 생성자 주입
//    fun <T : Any> create(modelClass: Class<T>): T {
//        val kClass = modelClass.kotlin
//        val constructor =
//            kClass.primaryConstructor
//                ?: throw IllegalArgumentException("주 생성자가 없습니다: ${modelClass.simpleName}")
//
//        val params =
//            constructor.parameters.associateWith { param ->
//                val mKClass = param.type.classifier as? KClass<*>
//                    ?: throw IllegalArgumentException("KClass<*>로 캐스팅할 수 없습니다: ${param.type.classifier}")
//                Dependency.get(mKClass)
//            }
//
//        return constructor.callBy(params)
//    }

    // 필드 주입
    fun <T : Any> create(modelClass: Class<T>): T {
        val instance = modelClass.getDeclaredConstructor().newInstance()
        val kClass = modelClass.kotlin
        kClass.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .forEach { property ->
                val fieldType = property.returnType.classifier as? KClass<*>
                if (fieldType != null) {
                    val dependency = Dependency.get(fieldType)
                    (property as KMutableProperty1<T, Any>).set(instance, dependency)
                }
            }

        return instance
    }
}
