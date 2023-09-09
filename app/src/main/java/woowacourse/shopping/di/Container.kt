package woowacourse.shopping.di

import woowacourse.shopping.data.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

object Container {
    private val nonAnnotationMap = mutableMapOf<KClass<*>, Any>()

    private val annotationMap = mutableMapOf<String, Any>()

    private const val defaultQualifier = ""

    fun getInstance(type: KClass<*>): Any? {
        return nonAnnotationMap[type]
    }

    fun getInstance(qualifier: Qualifier): Any? {
        // Default 값인 경우 annotation이 붙은 클래스임
        val qualifierName = qualifier.name
        return annotationMap[qualifierName]
    }

//    fun getInstance(type: KClass<*>): Any? {
//        // Default 값인 경우 annotation이 붙은 클래스임
//        if (nonAnnotationMap[type] == defaultQualifier) {
//            val qualifier = type.findAnnotation<Qualifier>()
//            val qualifierName = qualifier?.name ?: throw NullPointerException("Qualifier 이름이 없습니다.")
//            return annotationMap[qualifierName]
//        }
//        return nonAnnotationMap[type]
//    }

    fun addInstance(type: KClass<*>, instance: Any) {
        // 어노테이션이 있으면 Annotation맵에 저장
        if (instance::class.hasAnnotation<Qualifier>()) {
            val qualifier = instance::class.findAnnotation<Qualifier>()
            val qualifierName = qualifier?.name ?: throw NullPointerException("Qualifier 이름이 없습니다.")

            // getInstance를 가능하게 하기 위해 Qualifier가 있는 경우 Default 값 저장
            nonAnnotationMap[type] = defaultQualifier
            annotationMap[qualifierName] = instance
        } else {
            // 어노테이션이 없으면 nonAnnotationMap에 저장
            nonAnnotationMap[type] = instance
        }
//        nonAnnotationMap[type] = instance
    }

    fun clear() {
        annotationMap.clear()
        nonAnnotationMap.clear()
    }
}
