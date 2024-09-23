package com.woowa.di.component

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParentManager(val manager: KClass<out ComponentManager>)

data object NoParent : ComponentManager() {
    override fun <T : Any> getComponentInstance(componentType: KClass<out T>): Component {
        throw IllegalArgumentException()
    }
}
