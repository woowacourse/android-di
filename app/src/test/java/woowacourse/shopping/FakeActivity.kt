package woowacourse.shopping

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.woowacourse.di.DIViewModelFactory

class FakeActivity : AppCompatActivity() {
    val firstSuccessCaseViewModel by viewModels<FirstSuccessCaseViewModel> { DIViewModelFactory() }
    val firstFailureCaseViewModel by viewModels<FirstFailureCaseViewModel> { DIViewModelFactory() }
    val secondSuccessCaseViewModel by viewModels<SecondSuccessCaseViewModel> { DIViewModelFactory() }
    val thirdSuccessCaseViewModel by viewModels<ThirdSuccessCaseViewModel> { DIViewModelFactory() }
}
