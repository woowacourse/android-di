package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class AppContainer(private val modules: List<Module>) {
    private val appContainer: MutableMap<String, Any?> = mutableMapOf()

    private val provideFunctions: Map<String, KFunction<*>> = modules.flatMap { module ->
        module::class.declaredMemberFunctions.map { it.returnType.toString() to it }
    }.toMap()

    init {
        initContainer()
    }

    fun getInstance(type: KType): Any {
        val paramType = type.toString()
        return appContainer[paramType]
            ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $paramType")
    }

    private fun initContainer() {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach { kFunc ->
                val identifier = kFunc.returnType.toString()
                appContainer[identifier] = createInstance(kFunc = kFunc, receiver = module)
            }
        }
    }

    private fun createInstance(kFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams = kFunc.parameters
            .subList(1, kFunc.parameters.size)
            .filter { it.hasAnnotation<Inject>() }

        if (injectParams.isNotEmpty()) {
            injectParams.forEach { param ->
                val identifier = param.type.toString()
                if (appContainer[identifier] == null) {
                    provideFunctions[identifier]?.let {
                        createInstance(it, receiver)
                    }
                }
                appContainer[identifier]
            }
            val args = injectParams.associateWith {
                appContainer[it.type.toString()]
            }.toMutableMap()
            args[kFunc.parameters.first()] = receiver
            return kFunc.callBy(args)
        }
        return kFunc.call(receiver)
    }
}
