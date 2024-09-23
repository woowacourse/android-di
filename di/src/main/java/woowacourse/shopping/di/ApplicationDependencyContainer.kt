package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.LifecycleOwner

typealias ClassQualifier = Pair<DependencyType, AnnotationQualifier>

class ApplicationDependencyContainer(
    private val dependencyStorage: HashMap<ClassQualifier, DependencyProvider> = hashMapOf(),
) : LifecycleDependencyContainer {
    private lateinit var applicationContext: Context

    override fun <T : DependencyInstance> getInstance(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): T? = dependencyStorage[dependency to qualifier]?.getInstance()

    override fun <T : Any> getImplement(
        dependency: DependencyType,
        qualifier: AnnotationQualifier,
    ): ImplementationClass<T>? =
        dependencyStorage[dependency to qualifier]?.getImplement()

    override fun <T : Any> setDependency(
        dependency: DependencyType,
        implementation: ImplementationClass<T>,
        qualifier: AnnotationQualifier,
    ) {
        val dependencyProvider = ComponentDependencyProvider()
        dependencyProvider.setDependency(implementation)
        dependencyStorage[dependency to qualifier] = dependencyProvider
    }

    override fun setInstance(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
    ) {
        if (dependencyStorage.contains(dependency to qualifier)) {
            dependencyStorage[dependency to qualifier]?.setInstance(instance)
        }
    }

    override fun setApplicationContext(context: Context) {
        applicationContext = context
    }

    override fun getApplicationContext(): Context = applicationContext

    override fun setInstanceWithinLifecycle(
        dependency: DependencyType,
        instance: DependencyInstance,
        qualifier: AnnotationQualifier,
        lifecycleOwner: LifecycleOwner?,
        lifecycleAware: LifecycleAwareAnnotation,
    ) {
        println("hodu")
        println("\nhodu: setInstanceWitinLifecycle")
        println("hodu: instance being set = $instance")
        val dependencyProvider: DependencyProvider =
            dependencyStorage[dependency to qualifier]
                ?: ComponentDependencyProvider()
        dependencyProvider.setInstance(instance)
        when (lifecycleAware) {
            ApplicationLifecycleAware::class -> {
                // 어플리케이션 lifecycle에 aware
                println("hodu")
                println("\nhodu: 어플리케이션 lifecycle에 aware")
                println(
                    "hodu: 사용 중인 DependencyContainer가 이미 어플리케이션의 Lifecycle에 종속되어 있기 때문에, 따로 옵저빙을 하지 않는다."
                )
            }

            ActivityLifecycleAware::class -> {
                // 액티비티 lifecycle에 aware
                println("hodu")
                println("\nhodu: 액티비티 lifecycle에 aware")
                lifecycleOwner?.lifecycle?.addObserver(ComponentLifecycleObserver {
                    dependencyProvider.deleteInstance()
                    println("hodu: instance 삭제됨!")
                    println("hodu: instance = ${dependencyProvider.getInstance<Any>()}")
                })
                println("hodu: 액티비티 lifecycle에 옵저버 등록")
            }

            FragmentLifecycleAware::class -> {
                // 프래그먼트 lifecycle에 aware
                println("hodu")
                println("\nhodu: 프래그먼트 lifecycle에 aware")
                lifecycleOwner?.lifecycle?.addObserver(ComponentLifecycleObserver {
                    dependencyProvider.deleteInstance()
                    println("hodu: instance 삭제됨!")
                    println("hodu: instance = ${dependencyProvider.getInstance<Any>()}")
                })
                println("hodu: 프래그먼트 lifecycle에 옵저버 등록")
            }

            ViewModelLifecycleAware::class -> {
                // 뷰모델 lifecycle에 aware
                println("hodu")
                println("\nhodu: 뷰모델 lifecycle에 aware")
                lifecycleOwner?.lifecycle?.addObserver(
                    ViewModelLifecycleObserver(
                        {
                            dependencyProvider.setInstance(instance)
                            println("hodu: instance 설정됨!")
                            println("hodu: instance = ${dependencyProvider.getInstance<Any>()}")
                        },
                        {
                            dependencyProvider.deleteInstance()
                            println("hodu: instance 삭제됨!")
                            println("hodu: instance = ${dependencyProvider.getInstance<Any>()}")
                        },
                    )
                )
                println("hodu: 뷰모델 lifecycle에 옵저버 등록")
            }

            else -> {}
        }
    }
}
