package woowacourse.fakeClasses

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.annotation.Qualifier
import woowacourse.shopping.ui.util.viewModel.viewModel

interface FieldDependency

class AFieldDependency : FieldDependency

@Qualifier(AFieldDependency::class)
annotation class AFieldDependencyQualifier

class FieldTestActivity : AppCompatActivity() {
    val viewModel: FieldTestViewModel by viewModel()
}

class FieldTestViewModel : ViewModel() {
    @Inject
    @AFieldDependencyQualifier
    lateinit var fieldWithInjectAnnotation: FieldDependency
    private lateinit var fieldWithoutInjectAnnotation: FieldDependency

    fun isFieldWithoutInjectAnnotationInitialized() = ::fieldWithoutInjectAnnotation.isInitialized
}
