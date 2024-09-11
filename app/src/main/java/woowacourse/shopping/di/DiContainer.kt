package woowacourse.shopping.di

import dalvik.system.DexFile
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

@Suppress("UNCHECKED_CAST")
class DiContainer(
    private val diModule: DiModule,
    private val packageName: String,
    private val packagePath: String,
    private val classLoader: ClassLoader,
) {
    private val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        addDependencies(diModule::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC })
    }

    fun <T : Any> getInstance(clazz: KClass<T>): T {
        return dependencies[clazz] as? T
            ?: findAndCreateImplementation(clazz)
    }

    private fun <T : Any> findAndCreateImplementation(interfaceClass: KClass<T>): T {
        val implementationClasses = mutableSetOf<Class<out T>>()
        val classNames = DexFile(packagePath).entries()
        classNames.iterator().forEach { className ->
            addImplementationClasses(className, interfaceClass, implementationClasses)
        }
        if (implementationClasses.isEmpty()) {
            throw IllegalArgumentException("No implementation found for interface ${interfaceClass.simpleName}")
        }

        return createInstance(implementationClasses.first().kotlin as KClass<T>) // 생성한 인스턴스를 반환
    }

    private fun <T : Any> addImplementationClasses(
        className: String,
        interfaceClass: KClass<T>,
        implementationClasses: MutableSet<Class<out T>>,
    ) {
        if (className.startsWith(packageName)) {
            val loadedClass = classLoader.loadClass(className)
            if (loadedClass.superclass == interfaceClass.java ||
                loadedClass.interfaces.contains(
                    interfaceClass.java,
                )
            ) {
                implementationClasses.add(loadedClass as Class<out T>)
            }
        }
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor =
            clazz.primaryConstructor
                ?: throw IllegalArgumentException("No constructor found: ${clazz.simpleName}")

        val params =
            constructor.parameters.map { param ->
                val paramClass = param.type.jvmErasure
                getInstance(paramClass)
            }
        val instance = constructor.call(*params.toTypedArray())
        addInstance(clazz, instance)

        return instance
    }

    private fun addDependencies(functions: Collection<KFunction<*>>) {
        functions.forEach { func ->
            val clazz = func.returnType.jvmErasure
            val instance = func.call(diModule)
            addInstance(clazz, instance)
        }
    }

    private fun addInstance(
        classType: KClass<*>,
        instance: Any?,
    ) {
        instance?.let {
            dependencies[classType] = it
        }
    }
}
