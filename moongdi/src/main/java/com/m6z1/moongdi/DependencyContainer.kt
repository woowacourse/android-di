package com.m6z1.moongdi

import com.m6z1.moongdi.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DependencyContainer {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val qualifierDependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun register(instance: Any) {
        val clazz = instance::class

        if (clazz.hasAnnotation<Qualifier>()) {
            qualifierDependencies[clazz] = instance
            return
        }

        dependencies[clazz] = instance

        clazz.supertypes.forEach { superType ->
            val superClass = superType.classifier as? KClass<*>

            if (superClass != null &&
                superClass != Any::class &&
                (superClass.isAbstract || superClass.java.isInterface)
            ) {
                dependencies[superClass] = instance
            }
        }
    }

    fun provide(clazz: Class<*>): Any {
        val kClazz: KClass<*> = clazz.kotlin

        if (kClazz.hasAnnotation<Qualifier>()) {
            return qualifierDependencies[kClazz]
                ?: throw IllegalStateException("@Qualifier 가 붙은 $kClazz 클래스가 등록되지 않았습니다.")
        }
        val result =
            dependencies[kClazz]
                ?: throw IllegalStateException("$kClazz 클래스가 등록되지 않았습니다.")

        return result
    }
}
