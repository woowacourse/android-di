package com.angrypig.autodi.lifeCycleScopeHandler

import androidx.appcompat.app.AppCompatActivity
import com.angrypig.autodi.lifeCycleScopeHandler.annotation.activity.ActivityLifeCycleScope.*
import com.angrypig.autodi.lifeCycleScopeHandler.annotation.activity.ActivityScope
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

abstract class AutoDIScopedActivity<T : AppCompatActivity> : AppCompatActivity() {

    abstract val registerScope: T

    override fun onDestroy() {
        super.onDestroy()
        val activityLifeCycleScope =
            registerScope::class.findAnnotation<ActivityScope>()
                ?: throw IllegalStateException(UNSPECIFIED_SCOPE_ERROR)

        when (activityLifeCycleScope.lifeCycleScope) {
            ACTIVITY_RETAINED -> {
                if (registerScope.isFinishing) disconnectReference()
            }
            ACTIVITY -> {
                disconnectReference()
            }
        }
    }

    private fun disconnectReference() {
        for (property in registerScope::class.declaredMemberProperties) {
            val scopedAnnotation = property.findAnnotation<ScopedProperty>()
            val clazz: Class<out T> = registerScope::class.java
            if (scopedAnnotation != null) {
                val field = when (scopedAnnotation.delegated) {
                    true -> clazz.getDeclaredField(DELEGATE_PROPERTY_NAME.format(property.name))
                    false -> clazz.getDeclaredField(property.name)
                }
                field.isAccessible = true
                field.set(registerScope, null)
            }
        }
    }

    companion object {
        private const val DELEGATE_PROPERTY_NAME = "%s\$delegate"
        private const val UNSPECIFIED_SCOPE_ERROR =
            "스코프 Annotation을 지정하지 않았습니다.class 단위 Annotation을 확인하세요"
    }
}
