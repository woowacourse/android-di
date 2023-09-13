package woowacourse.fakeClasses

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.woowacourse.bunadi.annotation.Qualifier
import woowacourse.shopping.ui.util.viewModel.viewModel

@Qualifier(AConstructorDependency::class)
annotation class AConstructorDependencyQualifier

interface ConstructorDependency

class AConstructorDependency : ConstructorDependency

class ConstructorTestActivity : AppCompatActivity() {
    val viewModel: ConstructorTestViewModel by viewModel()
}

class ConstructorTestViewModel(
    @AConstructorDependencyQualifier
    val constructorDependency: ConstructorDependency,
) : ViewModel()
