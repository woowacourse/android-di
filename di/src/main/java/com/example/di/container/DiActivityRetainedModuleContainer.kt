package com.example.di.container

import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class DiActivityRetainedModuleContainer(private val applicationModule: ApplicationModule) {
    private val moduleMap: MutableMap<Int, ActivityRetainedModule> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityRetainedModule> provideActivityModule(
        newOwnerHashCode: Int,
        oldOwnerHashCode: Int?,
        clazz: Class<T>,
    ): T {
        val module = moduleMap.remove(oldOwnerHashCode) ?: createFromOwnerHashCode(clazz)
        moduleMap[newOwnerHashCode] = module
        return module as T
    }

    private fun <T : ActivityRetainedModule> createFromOwnerHashCode(
        clazz: Class<T>,
    ): T {
        val primaryConstructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("ActivityRetainedModule의 주 생성자가 존재하지 않습니다.")
        validateActivityRetainedModulePrimaryConstructor(primaryConstructor)
        return primaryConstructor.call(applicationModule)
    }

    fun removeModule(ownerHashCode: Int) {
        moduleMap.remove(ownerHashCode)
    }

    private fun validateActivityRetainedModulePrimaryConstructor(primaryConstructor: KFunction<ActivityRetainedModule>) {
        check(primaryConstructor.valueParameters.size == 1) {
            ERROR_ACTIVITY_RETAINED_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
        }
        check(primaryConstructor.valueParameters.map { it.type.jvmErasure } == ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES) {
            ERROR_ACTIVITY_RETAINED_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
        }
    }

    companion object {
        private const val ERROR_ACTIVITY_RETAINED_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ActivityRetainedModule의 상속한 클래스의 생성자의 매개변수는 1개여야 하고, 그 타입은 ApplicationModule이어야 합니다."
        private val ACTIVITY_RETAINED_MODULE_VALUE_PARAMETER_TYPES =
            listOf(ApplicationModule::class)
    }
}
