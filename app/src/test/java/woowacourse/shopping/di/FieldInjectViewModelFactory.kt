package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.fixture.Inject
import woowacourse.shopping.fixture.TestAppContainer

class FieldInjectViewModelFactory(
    private val appContainer: TestAppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm = modelClass.getDeclaredConstructor().newInstance()

        modelClass.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true

                // 바인딩 없으면 예외
                val dependency = appContainer.resolve(field.type.kotlin)
                if (dependency != null) {
                    field.set(vm, dependency)
                } else {
                    throw IllegalArgumentException(
                        "No binding found for type ${field.type.kotlin.simpleName}",
                    )
                }
            }
        }

        return vm
    }
}
