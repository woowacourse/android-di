package com.example.di.container

import android.content.Context
import com.example.di.module.ActivityModule
import com.example.di.module.ApplicationModule
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class DiActivityModuleContainer(private val applicationModule: ApplicationModule) {
    private val moduleMap: MutableMap<Int, ActivityModule> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityModule> provideActivityModule(
        newOwner: Context,
        oldOwnerHashCode: Int?,
        clazz: Class<T>,
    ): T {
        val module = moduleMap.remove(oldOwnerHashCode) ?: createFromOwnerHashCode(newOwner, clazz)
        module.context = newOwner
        moduleMap[newOwner.hashCode()] = module
        return module as T
    }

    private fun <T : ActivityModule> createFromOwnerHashCode(
        owner: Context,
        clazz: Class<T>,
    ): T {
        val primaryConstructor = clazz.kotlin.primaryConstructor
            ?: throw NullPointerException("액티비티 모듈의 주 생성자는 애플리케이션 모듈만 매개변수로 선언되어있어야 합니다.")
        validateActivityModulePrimaryConstructor(primaryConstructor)
        return primaryConstructor.call(owner, applicationModule)
    }

    fun removeModule(ownerHashCode: Int) {
        moduleMap.remove(ownerHashCode)
    }

    private fun validateActivityModulePrimaryConstructor(primaryConstructor: KFunction<ActivityModule>) {
        check(primaryConstructor.valueParameters.size == 2) {
            ERROR_ACTIVITY_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
        }
        check(primaryConstructor.valueParameters.map { it.type.jvmErasure } == ACTIVITY_MODULE_VALUE_PARAMETER_TYPES) {
            ERROR_ACTIVITY_MODULE_PRIMARY_CONSTRUCTOR_CONDITION
        }
    }

    companion object {
        private const val ERROR_ACTIVITY_MODULE_PRIMARY_CONSTRUCTOR_CONDITION =
            "[ERROR] ActivityModule를 상속한 클래스의 생성자의 매개변수는 2개여야 하고, 그 타입은 Context와 ApplicationModule이어야 합니다."
        private val ACTIVITY_MODULE_VALUE_PARAMETER_TYPES =
            listOf(Context::class, ApplicationModule::class)
    }
}
