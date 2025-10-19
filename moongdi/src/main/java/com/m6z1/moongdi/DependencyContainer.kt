package com.m6z1.moongdi

import com.m6z1.moongdi.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DependencyContainer {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val qualifierDependencies: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val singletonCache: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun register(instance: Any) {
        val clazz = instance::class
        val isQualifier = clazz.hasAnnotation<Qualifier>()

        if (isQualifier) {
            qualifierDependencies[clazz] = instance

            clazz.supertypes.forEach { superType ->
                val superClass = superType.classifier as? KClass<*>

                if (superClass != null &&
                    superClass != Any::class &&
                    (superClass.isAbstract || superClass.java.isInterface)
                ) {
                    qualifierDependencies[superClass] = instance
                }
            }
        } else {
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
    }

    fun cacheSingleton(
        clazz: KClass<*>,
        instance: Any,
    ) {
        val isQualifier = clazz.hasAnnotation<Qualifier>()

        if (isQualifier) {
            qualifierDependencies[clazz] = instance

            clazz.supertypes.forEach { superType ->
                val superClass = superType.classifier as? KClass<*>

                if (superClass != null &&
                    superClass != Any::class &&
                    (superClass.isAbstract || superClass.java.isInterface)
                ) {
                    qualifierDependencies[superClass] = instance
                }
            }
        }

        singletonCache[clazz] = instance

        clazz.supertypes.forEach { superType ->
            val superClass = superType.classifier as? KClass<*>

            if (superClass != null &&
                superClass != Any::class &&
                (superClass.isAbstract || superClass.java.isInterface)
            ) {
                singletonCache[superClass] = instance
            }
        }
    }

    fun provide(clazz: Class<*>): Any {
        val kClazz: KClass<*> = clazz.kotlin

        if (kClazz.hasAnnotation<Qualifier>()) {
            return qualifierDependencies[kClazz]
                ?: throw IllegalStateException("@Qualifier가 붙은 $kClazz 가 등록되지 않았습니다.")
        }

        singletonCache[kClazz]?.let { return it }

        val result =
            dependencies[kClazz]
                ?: throw IllegalStateException("$kClazz 가 등록되지 않았습니다.")

        return result
    }
}
