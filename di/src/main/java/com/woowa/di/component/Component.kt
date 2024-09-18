package com.woowa.di.component

import kotlin.reflect.KClass

interface Component {

    /**
     * Returns the instance you want to inject, or `null` if the instance does not exist
     */
    fun getDIInstanceOrNull(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any?
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InstallIn(val component: KClass<out Component>)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParentManager(val manager: KClass<out ComponentManager>)

data object NoParent:ComponentManager {
    override fun getDIInstanceOrNull(type: KClass<*>, qualifier: KClass<out Annotation>?): Any? {
        TODO("Not yet implemented")
    }

    override fun getBinderType(key: KClass<*>): KClass<*> {
        TODO("Not yet implemented")
    }

    override fun getBinderTypeOrNull(key: KClass<*>): KClass<*>? {
        TODO("Not yet implemented")
    }

    override fun <binder : Any> registerBinder(binderClazz: KClass<binder>) {
        TODO("Not yet implemented")
    }
}