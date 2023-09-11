package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

class AppContainer(private val modules: List<Module>) {

    private val appContainer: MutableMap<String, Any?> = mutableMapOf()

    private val provideFunctions: Map<String, KFunction<*>> = modules.flatMap { module ->
        module::class.declaredMemberFunctions.map {
            it.identifyKey() to it
        }
    }.toMap()

    init {
        initContainer()
    }

    fun getInstance(identifyKey: String): Any {
        return appContainer[identifyKey]
            ?: throw IllegalArgumentException("의존성 주입할 인스턴스 없음: $identifyKey")
    }

    private fun initContainer() {
        modules.forEach { module ->
            module::class.declaredMemberFunctions.forEach { kFunc ->
                appContainer[kFunc.identifyKey()] =
                    createInstance(provideFunc = kFunc, receiver = module)
            }
        }
    }

    private fun createInstance(provideFunc: KFunction<*>, receiver: Module): Any? {
        val injectParams = provideFunc.parameters.filter { it.hasAnnotation<Inject>() }

        if (injectParams.isNotEmpty()) {
            injectParams.forEach { param ->
                val key = param.identifyKey()
                if (appContainer[key] == null) {
                    provideFunctions[key]?.let {
                        appContainer[key] = createInstance(it, receiver)
                    }
                }
            }
            val args = injectParams.associateWith { param ->
                appContainer[param.identifyKey()]
            }.toMutableMap()

            args[provideFunc.parameters.first()] = receiver

            return provideFunc.callBy(args)
        }
        return provideFunc.callBy(mapOf(provideFunc.parameters.first() to receiver))
    }
}
