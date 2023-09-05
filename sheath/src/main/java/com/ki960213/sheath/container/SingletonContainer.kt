package com.ki960213.sheath.container

import kotlin.reflect.KClass

interface SingletonContainer {

    fun getInstance(clazz: KClass<*>): Any
}
