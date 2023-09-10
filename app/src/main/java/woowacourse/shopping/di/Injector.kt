package woowacourse.shopping.di

import android.content.Context
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class Injector(private val container: Container, private val context: Context) {

    fun inject(module: Module) {
        val functions = module::class.declaredFunctions
        functions.forEach { func -> recursive(functions, func, module) }
    }

    private fun recursive(
        functions: Collection<KFunction<*>>,
        func: KFunction<*>,
        module: Module,
    ) {
        val funcClazz = func.returnType.jvmErasure
        // 이미 생성했다면 return
        if (container.getInstance(funcClazz) != null) return

        // 파라미터가 없다면 만든다
        if (func.valueParameters.isEmpty()) {
            func.call(module)?.let { container.setInstance(funcClazz, it) }
            return
        }

        // 파라미터를 하나씩 채운다
        val params = func.valueParameters.map { param ->
            when (param.type.jvmErasure) {
                Context::class -> context
                else -> getParamInstance(param, functions, module)
            }
        }

        // 생성하고 컨테이너 에 삽입
        val newInstance = requireNotNull(func.call(module, *params.toTypedArray()))
        container.setInstance(funcClazz, newInstance)
    }

    private fun getParamInstance(
        param: KParameter,
        functions: Collection<KFunction<*>>,
        module: Module,
    ): Any {
        val paramClazz = param.type.jvmErasure

        // 없으면 재귀로 생성
        if (container.getInstance(paramClazz) == null) {
            recursive(functions, functions.first { it.returnType == param.type }, module)
        }

        // 있으면 그대로 반환
        return container.getInstance(paramClazz)!!
    }
}
