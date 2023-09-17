package com.ki960213.sheath.component

import android.content.Context
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.extention.hasAnnotationOrHasAttachedAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.jvmErasure

internal object SheathComponentFactory {

    fun create(clazz: KClass<*>): SheathComponent1 {
        SheathComponentValidator.validate(clazz)

        val name = clazz.qualifiedName
            ?: throw IllegalArgumentException("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")

        return SheathComponent1(
            type = clazz.createType(),
            name = name,
            isSingleton = !clazz.hasAnnotationOrHasAttachedAnnotation<Prototype>(),
            dependentConditions = DependenciesExtractor.extractFrom(clazz),
        )
    }

    fun create(function: KFunction<*>): SheathComponent1 {
        SheathComponentValidator.validate(function)

        val name = function.returnType.jvmErasure.qualifiedName
            ?: throw IllegalArgumentException("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")

        return SheathComponent1(
            type = function.returnType,
            name = name,
            isSingleton = !function.hasAnnotationOrHasAttachedAnnotation<Prototype>(),
            dependentConditions = DependenciesExtractor.extractFrom(function),
        )
    }

    fun create(context: Context): SheathComponent1 = SheathComponent1(
        type = Context::class.createType(),
        name = Context::class.qualifiedName!!,
        isSingleton = true,
        dependentConditions = emptyMap(),
        absoluteInstance = context,
    )
}
