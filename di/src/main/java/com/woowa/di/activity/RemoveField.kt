package com.woowa.di.activity

// inline fun <reified T : Activity> removeInstancesOnDestroyed(instance: T) {
//    T::class.java.declaredFields.onEach { field ->
//        field.isAccessible = true
//    }.filter { it.isAnnotationPresent(Inject::class.java) }.forEach { field ->
//        ActivityComponentManager.deleteDIInstance(
//            field.type.kotlin,
//            field.kotlinProperty?.findQualifierClassOrNull(),
//        )
//    }
// }
