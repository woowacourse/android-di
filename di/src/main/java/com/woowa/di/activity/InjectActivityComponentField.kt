package com.woowa.di.activity

import com.woowa.di.ActivityContext
import com.woowa.di.component.DIBuilder
import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.kotlinProperty

fun injectActivityComponentField(instance: Any) {
    instance::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter { it.isAnnotationPresent(Inject::class.java) }.map { field ->
        val fieldInstance =
            ActivityComponentManager.getDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        field.set(instance, fieldInstance)
    }
 }

fun getApplicationContextInjectedInstance(
    binderInstance: Any,
    kFunc: KFunction<*>,
): Any? {
    return if (kFunc.parameters.any { it.hasAnnotation<ActivityContext>() }) {
        kFunc.call(binderInstance, DIBuilder.applicationContext)
    } else {
        kFunc.call(binderInstance)
    }
}
