package com.di.berdi

import android.content.Context
import com.di.berdi.annotation.Inject
import com.di.berdi.util.hasQualifier
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class Injector(private val container: Container, private val context: Context) {

    fun injectBy(modules: List<Module>) {
        modules.forEach { injectBy(it) }
    }

    fun injectBy(module: Module) {
        val functions = module::class.declaredFunctions
        functions.forEach { func -> storeInstance(functions, func, module) }
    }

    private fun storeInstance(
        functions: Collection<KFunction<*>>,
        func: KFunction<*>,
        module: Module,
    ) {
        val funcClazz = func.returnType.jvmErasure
        val annotation = func.qualifierAnnotations

        // 이미 생성했다면 return
        if (container.getInstance(funcClazz, annotation) != null) return

        // 파라미터가 없다면 만든다
        if (func.valueParameters.isEmpty()) {
            func.call(module)?.let { container.setInstance(it, funcClazz, annotation) }
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
        container.setInstance(newInstance, funcClazz, annotation)
    }

    private val KFunction<*>.qualifierAnnotations get() = annotations.firstOrNull { it.hasQualifier() }

    private fun getParamInstance(
        param: KParameter,
        functions: Collection<KFunction<*>>,
        module: Module,
    ): Any {
        val paramClazz = param.type.jvmErasure
        val annotation =
            param.annotations.firstOrNull { it.hasQualifier() }

        // 없으면 재귀로 생성
        if (container.getInstance(type = paramClazz, annotation = annotation) == null) {
            storeInstance(functions, functions.first { it.returnType == param.type }, module)
        }

        // 있으면 그대로 반환
        return container.getInstance(type = paramClazz, annotation = annotation)!!
    }

    fun <T : Any> createBy(constructor: KFunction<T>): T {
        val params = getInstancesParamsOf(constructor)
        return constructor.call(*params.toTypedArray()).apply { injectProperties(this) }
    }

    private fun getInstancesParamsOf(constructor: KFunction<Any>): List<Any?> {
        return constructor.parameters.map { param ->
            val annotation = param.annotations.firstOrNull { it.hasQualifier() }
            requireNotNull(container.getInstance(param.type.jvmErasure, annotation)) {
                "No matching same type in param | type : ${param.type}"
            }
        }
    }

    private fun <T : Any> injectProperties(target: T) {
        val properties = target::class.declaredMemberProperties.filter {
            it.hasAnnotation<Inject>()
        }
        properties.forEach { property -> property.inject(target) }
    }

    private fun <T : Any> KProperty<*>.inject(target: T) {
        val annotation = annotations.firstOrNull { it.hasQualifier() }
        val instance = container.getInstance(this.returnType.jvmErasure, annotation)
        javaField?.apply {
            isAccessible = true
            set(target, instance)
        }
    }
}
