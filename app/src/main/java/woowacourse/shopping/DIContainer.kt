package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    inline fun <reified T : Any> createViewModel(
        modelClass: KClass<T>,
        vararg any: Any? = emptyArray(),
    ): T {
        var index = 0
        val primaryConstructor = modelClass.primaryConstructor ?: return modelClass.createInstance()
        val constructorArgs = primaryConstructor.parameters.map { parameter ->
            if (parameter.hasAnnotation<Inject>()) {
                val dependencyClass = parameter.type.classifier as? KClass<*>

                val getMethod =
                    Module::class.java.declaredMethods.find {
                        it.returnType == dependencyClass?.java
                    }
                getMethod?.invoke(Module)
            } else {
                any[index++]
            }
        }.toTypedArray()

        return primaryConstructor.call(*constructorArgs)
    }
}
