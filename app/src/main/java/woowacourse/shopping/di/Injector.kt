package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor

class Injector(private val modules: List<Module>) {

    private val container: MutableMap<String, Any?> = mutableMapOf()

    private val provideFunctions: Map<String, KFunction<*>> = modules.flatMap { module ->
        module::class.declaredMemberFunctions.map { it.returnType.toString() to it }
    }.toMap()

    init {
        initContainer()
    }

    private fun initContainer() {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach { kFunc ->
                val identifier = kFunc.returnType.toString()
                container[identifier] = createInstance(kFunc = kFunc, receiver = module)
            }
        }
    }

    private fun createInstance(kFunc: KFunction<*>, receiver: Module): Any? {
        val requiredParameters = kFunc.parameters.subList(1, kFunc.parameters.size)
        if (requiredParameters.isNotEmpty()) {
            val args = requiredParameters.map { param ->
                val identifier = param.type.toString()
                if (container[identifier] == null) {
                    provideFunctions[identifier]?.let {
                        createInstance(it, receiver)
                    }
                }
                container[identifier]
            }

            return kFunc.call(receiver, *args.toTypedArray())
        }
        return kFunc.call(receiver)
    }

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")

        val args = getArgumentsFromContainer(primaryConstructor.parameters)

        return primaryConstructor.call(*args.toTypedArray())
    }

    private fun getArgumentsFromContainer(parameters: List<KParameter>): List<Any> {
        return parameters.map {
            val paramType = it.type.toString()
            container[paramType] ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $paramType")
        }
    }
}
