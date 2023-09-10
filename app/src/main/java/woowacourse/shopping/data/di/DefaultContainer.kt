package woowacourse.shopping.data.di

import kotlin.reflect.KClass

object DefaultContainer:Container {
    override val instances:MutableMap<KClass<*>, Any> = mutableMapOf()

    override fun addInstance(clazz:KClass<*>, instance:Any){
        instances[clazz] = instance
    }

    override fun getInstance(clazz: KClass<*>):Any?{
        return instances[clazz]
    }
}