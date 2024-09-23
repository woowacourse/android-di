package com.example.di

import android.app.Application
import com.example.di.annotation.Provides
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class SourceContainer {
    private val sources = mutableMapOf<KClass<*>, Any>()
    private val modules = mutableListOf<Any>(ApplicationContextModule)

    fun initApplication(app: Application) {
        ApplicationContextModule.initApplication(app)
    }

    fun addModule(vararg newModules: Any) {
        modules.addAll(newModules)
    }

    fun getSourceOrNull(sourceType: KClass<*>): Any? {
        return sources[sourceType] ?: run {
            val targetModuleAndFunction = findSourceTypeProvide(sourceType)
            if (targetModuleAndFunction.isEmpty()) {
                null
            } else {
                val newSource = create(targetModuleAndFunction)
                addSource(sourceType, newSource)
                newSource
            }
        }
    }

    private fun findSourceTypeProvide(sourceType: KClass<*>): Map<Any, KFunction<*>> =
        modules
            .filter { it::class.objectInstance != null }
            .associateWith { it::class.getAnnotatedFunctions() }
            .entries
            .flatMap { (module, functions) ->
                functions.filter {
                    it.isReturnTypeMatchedWith(sourceType)
                }.map { module to it }
            }
            .toMap()

    private fun KClass<*>.getAnnotatedFunctions() =
        functions.filter { function ->
            function.hasAnnotation<Provides>()
        }

    private fun KFunction<*>.isReturnTypeMatchedWith(sourceType: KClass<*>) = returnType.classifier == sourceType

    private fun create(targetModuleAndFunction: Map<Any, KFunction<*>>): Any {
        val targetFunction = targetModuleAndFunction.values.first()
        val targetModule = targetModuleAndFunction.keys.first()
        val params = targetFunction.parameters.resolve(targetModule)
        return targetFunction.callBy(params)!!
    }

    private fun List<KParameter>.resolve(targetModule: Any): Map<KParameter, Any?> {
        return associateWith { param ->
            if (param.kind == KParameter.Kind.INSTANCE) {
                targetModule
            } else {
                getSourceOrNull(param.type.classifier as KClass<*>)
            }
        }
    }

    private fun addSource(
        classType: KClass<*>,
        source: Any,
    ) {
        sources[classType] = source
    }
}
