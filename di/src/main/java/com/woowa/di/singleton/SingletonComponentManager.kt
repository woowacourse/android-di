package com.woowa.di.singleton

import com.woowa.di.component.ComponentManager
import com.woowa.di.component.NoParent
import com.woowa.di.component.ParentManager
import com.woowa.di.viewmodel.ViewModelComponent
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

@ParentManager(NoParent::class)
object SingletonComponentManager:ComponentManager {
    private val binderClazzs = mutableListOf<KClass<*>>()

    override fun getDIInstanceOrNull(type: KClass<*>, qualifier: KClass<out Annotation>?): Any? {
        val binderType = getBinderTypeOrNull(type) ?: return getParentDIInstance(type, qualifier)
        return SingletonComponent.getInstance(binderType).getDIInstanceOrNull(type, qualifier)
    }

    private fun getParentDIInstance(type: KClass<*>, qualifier: KClass<out Annotation>?): Any? {
        require(this::class.findAnnotation<ParentManager>()?.manager != NoParent::class) {
            "${type.simpleName}이 binder에 정의되어 있지 않습니다."
        }

        val parentManager =
            this::class.findAnnotation<ParentManager>()?.manager?.objectInstance ?: return null
        return parentManager.getDIInstanceOrNull(type, qualifier)
    }


    override fun getBinderType(key: KClass<*>): KClass<*> {
        return binderClazzs.find { it.declaredMemberFunctions.find { it.returnType.jvmErasure == key } != null }
            ?: error("${key.simpleName}에 해당하는 객체가 binder에 등록되지 않았습니다.")
    }

    override fun getBinderTypeOrNull(key: KClass<*>): KClass<*>? {
        return binderClazzs.find { it.declaredMemberFunctions.find { it.returnType.jvmErasure == key } != null }
    }

    override fun <binder : Any> registerBinder(binderClazz: KClass<binder>) {
        require(binderClazz.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
            .none { it.returnType.isMarkedNullable }) {
            "nullable 타입은 주입할 수 없습니다."
        }

        binderClazzs.add(binderClazz)
    }
}
