package olive.di.util

import olive.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

internal fun KClass<*>.fieldsToInject(): List<KMutableProperty1<out Any, *>> {
    return declaredMemberProperties
        .filter { it.isLateinit }
        .filter { it.hasAnnotation<Inject>() }
        .map { it as KMutableProperty1 }
}

internal fun KMutableProperty1<out Any, *>.toReturnType(): KClass<*> = returnType.classifier as KClass<*>
