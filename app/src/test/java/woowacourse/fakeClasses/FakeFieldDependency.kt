package woowacourse.fakeClasses

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.mission.androiddi.util.viewModel.viewModel
import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.annotation.Qualifier

interface FieldDependency

class AFieldDependency : FieldDependency
class BFieldDependency : FieldDependency

@Qualifier(AFieldDependency::class)
annotation class AFieldDependencyQualifier

@Qualifier(BFieldDependency::class)
annotation class BFieldDependencyQualifier

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
