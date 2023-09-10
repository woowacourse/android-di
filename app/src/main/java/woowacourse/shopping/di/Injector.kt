package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmName

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
            container[module::class.jvmName] = module
            module::class.declaredMemberFunctions.forEach { kFunc ->
                val identifier = kFunc.returnType.toString()
                container[identifier] = createInstance(kFunc = kFunc, receiver = module)
            }
        }
    }

    private fun createInstance(kFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams =
            kFunc.parameters.subList(1, kFunc.parameters.size).filter { it.hasAnnotation<Inject>() }
        if (injectParams.isNotEmpty()) {
            injectParams.forEach { param ->
                val identifier = param.type.toString()
                if (container[identifier] == null) {
                    provideFunctions[identifier]?.let {
                        createInstance(it, receiver)
                    }
                }
                container[identifier]
            }
            val args = injectParams.associateWith { container[it.type.toString()] }.toMutableMap()
            val subReceiver = kFunc.parameters.first()
            args[subReceiver] = container[subReceiver.type.toString()]
            return kFunc.callBy(args)
        }
        return kFunc.call(receiver)
    }

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")

        val filteredParams = primaryConstructor.parameters.filter { it.hasAnnotation<Inject>() }

        val args = filteredParams.associateWith { getArgumentFromContainer(it) }

        return primaryConstructor.callBy(args)
    }

    private fun getArgumentFromContainer(parameter: KParameter): Any {
        val paramType = parameter.type.toString()
        return container[paramType] ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $paramType")
    }
}
