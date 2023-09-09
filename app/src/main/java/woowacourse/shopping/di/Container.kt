package woowacourse.shopping.di

import woowacourse.shopping.data.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

object Container {
    private val nonAnnotationMap = mutableMapOf<KClass<*>, Any>()

    private val annotationMap = mutableMapOf<String, Any>()

    const val defaultQualifier = ""

    fun getInstance(type: KClass<*>): Any? {
        return nonAnnotationMap[type]
    }

    fun getInstance(qualifier: Qualifier): Any? {
        val qualifierName = qualifier.name
        return annotationMap[qualifierName]
    }

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
    }

    fun clear() {
        annotationMap.clear()
        nonAnnotationMap.clear()
    }
}
