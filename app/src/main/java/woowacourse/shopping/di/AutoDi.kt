package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object AutoDi : ViewModelProvider.Factory {
    private val instances = mutableMapOf<KClass<*>, Any>()

    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = createInstance(targetClass = modelClass)

    private fun <T : Any> createInstance(targetClass: KClass<T>): T {
        val constructor =
            targetClass.primaryConstructor
                ?: throw IllegalArgumentException("${targetClass.simpleName}의 주 생성자가 없습니다.")

        val dependencyClasses =
            constructor.parameters.map { param ->
                val dependencyClass =
                    param.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${param.name}은 클래스가 아닙니다.")

                getInstance(dependencyClass)
            }

        return constructor.call(*dependencyClasses.toTypedArray())
    }

    private fun getInstance(targetClass: KClass<*>): Any =
        instances.getOrPut(targetClass) {
            createInstance(targetClass)
        }
}
