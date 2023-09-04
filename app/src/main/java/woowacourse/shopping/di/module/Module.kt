package woowacourse.shopping.di.module

import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions

abstract class Module(val parentModule: Module? = null) {
    protected val cache = mutableMapOf<String, Any>() // 모듈은 자신의 생명이 살아있는 한, 한 번 만들었던 동일한 객체를 뱉어내야 한다.

    protected inline fun <reified T : Any> getInstance(crossinline create: () -> T): T {
        val name = T::class.qualifiedName ?: throw NullPointerException("클래스 이름 없음")
        val instance = cache[name] as T?
        return instance ?: kotlin.run {
            create().also { cache[name] = it }
        }
    }

    fun getPublicMethodMap(): Map<KFunction<*>, Module> {
        return mutableMapOf<KFunction<*>, Module>().apply {
            this@Module::class.declaredMemberFunctions.filter {
                it.visibility == KVisibility.PUBLIC
            }.forEach { this[it] = this@Module }
        }
    }
}
