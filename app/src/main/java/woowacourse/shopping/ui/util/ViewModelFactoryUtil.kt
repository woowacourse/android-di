package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.di.Injector
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object ViewModelFactoryUtil {
    inline fun <reified T : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                // 뷰모델의 주 생성자를 가져와요!!
                val primaryConstructor =
                    T::class.primaryConstructor ?: throw IllegalStateException()

                // 뷰 모델의 생성자의 파라미터들을 KClass 로 바꿔줘요!
                val constructorParameterKClasses =
                    primaryConstructor.parameters.map { it.type.jvmErasure }

                // 파라미터들의 KClass에 해당하는 객체를 주입해요!
                val arguments = constructorParameterKClasses.map { kClazz ->
                    Injector.getService(kClazz).instance
                }.toTypedArray()
                primaryConstructor.call(*arguments)
            }
        }
}
