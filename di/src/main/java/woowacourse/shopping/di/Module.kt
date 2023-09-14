package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.jvmErasure

interface Module {
    fun searchFunctions(kClass: KClass<*>): List<KFunction<*>> =
        this::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .filter { it.returnType.jvmErasure == kClass }

    fun searchFunction(kClass: KClass<*>): KFunction<*> =
        this::class.declaredFunctions
            .filter { it.visibility == KVisibility.PUBLIC }
            .find { it.returnType.jvmErasure == kClass }
            ?: throw IllegalArgumentException("[ERROR] 존재하지 않는 함수입니다.")
}
