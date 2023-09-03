package woowacourse.shopping.di

import woowacourse.shopping.di.module.Module
import woowacourse.shopping.di.module.NormalModule
import woowacourse.shopping.di.module.SingletonModule
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

class AutoInjector(
    private val singletonModule: SingletonModule,
    private val normalModule: NormalModule,
) : Injector {
    private val moduleFunctions = mutableMapOf<KFunction<*>, Module>().apply {
        listOf(singletonModule, normalModule).forEach { module ->
            module::class.declaredMemberFunctions.filter { it.visibility == KVisibility.PUBLIC }
                .forEach { function ->
                    this[function] = module
                }
        }
    }

    override fun <T : Any> inject(modelClass: Class<T>): T {
        val injectableFunctions = mutableListOf<KFunction<*>>()
        for (function in moduleFunctions.keys) {
            val returnKClass = function.returnType.classifier as KClass<*>
            if (modelClass.kotlin.isSubclassOf(returnKClass)) {
                injectableFunctions.add(function)
            }
        }

        // 해당 객체를 만들기 위한 필요 구성 요소들을 얻는 과정.
        if (injectableFunctions.size == 1) {
            return createWithModuleFunc(injectableFunctions.first())
        } else if (injectableFunctions.size >= 2) {
            // 이 메소드들 중 어노테이션으로 판단해서 일치하는 메소드를 실행시켜서 반환시킬 것임.
        }

        val primaryConstructor =
            modelClass.kotlin.primaryConstructor ?: throw RuntimeException("주생성자 없음")
        return createWithPrimaryConstructor(primaryConstructor)
    }

    private fun <T : Any> createWithModuleFunc(func: KFunction<*>): T {
        val params = mutableListOf<Any>()
        val module = moduleFunctions[func] ?: throw RuntimeException()
        params.add(module)
        func.valueParameters.forEach { param ->
            val paramKClass = param.type.classifier as KClass<*>
            params.add(inject(paramKClass.java))
        }
        @Suppress("UNCHECKED_CAST")
        return func.call(*params.toTypedArray()) as T
    }

    private fun <T : Any> createWithPrimaryConstructor(primaryConstructor: KFunction<T>): T {
        val params = mutableListOf<Any>()
        primaryConstructor.valueParameters.forEach { param ->
            val kcalss = param.type.classifier as KClass<*>
            params.add(inject(kcalss.java))
        }
        return primaryConstructor.call(*params.toTypedArray())
    }
}
