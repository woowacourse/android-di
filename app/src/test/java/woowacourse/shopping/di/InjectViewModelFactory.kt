package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.fixture.TestAppContainer
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class InjectViewModelFactory(
    private val appContainer: TestAppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor = kClass.primaryConstructor
        val vm: T =
            if (constructor != null) {
                val args =
                    constructor.parameters.associateWith { param ->
                        val clazz = param.type.classifier as? KClass<*>
                        clazz?.let { appContainer.resolve(it) }
                    }
                constructor.callBy(args)
            } else {
                kClass.createInstance() as T
            }

        // 필드 주입: lateinit var, @Inject 붙은 프로퍼티
        kClass.memberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .forEach { prop ->
                val isInject = prop.annotations.any { it is Inject }
                if (!isInject) return@forEach

                val clazz = prop.returnType.classifier as? KClass<*>
                val dependency = clazz?.let { appContainer.resolve(it) }
                if (dependency != null) {
                    prop.setter.call(vm, dependency)
                }
            }

        return vm
    }
}
