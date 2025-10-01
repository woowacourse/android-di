package woowacourse.shopping.common

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

class DIViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val viewModelKClass = modelClass.kotlin
        val primaryConstructor: KFunction<*> =
            viewModelKClass.primaryConstructor
                ?: throw IllegalArgumentException(
                    "${viewModelKClass.qualifiedName} 클래스는 기본 생성자를 선언해야 합니다.",
                )

        val application: Application? = extras[APPLICATION_KEY]

        val args =
            primaryConstructor.parameters.associateWith { parameter ->
                val parameterType: KType = parameter.type
                val classifier = parameterType.classifier

                when (classifier) {
                    Application::class ->
                        requireNotNull(application) { "CreationExtras 안에 Application이 없습니다." }

                    else ->
                        appContainer.resolve(parameterType)
                            ?: throw IllegalStateException(
                                "타입에 대한 제공자가 없습니다",
                            )
                }
            }

        return primaryConstructor.callBy(args) as T
    }
}

fun AppContainer.resolve(requestedType: KType): Any? {
    val kClass = requestedType.classifier as? KClass<*> ?: return null
    return when (kClass) {
        ProductRepository::class, ProductRepositoryImpl::class -> productRepository
        CartRepository::class, CartRepositoryImpl::class -> cartRepository
        else -> null
    }
}
