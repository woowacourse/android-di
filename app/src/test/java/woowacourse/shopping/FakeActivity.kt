package woowacourse.shopping

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class FakeActivity : AppCompatActivity() {
    val firstSuccessCaseViewModel by viewModels<FirstSuccessCaseViewModel> { GlobalViewModelFactory() }
    val firstFailureCaseViewModel by viewModels<FirstFailureCaseViewModel> { GlobalViewModelFactory() }
    val secondSuccessCaseViewModel by viewModels<SecondSuccessCaseViewModel> { GlobalViewModelFactory() }
    val thirdSuccessCaseViewModel by viewModels<ThirdSuccessCaseViewModel> { GlobalViewModelFactory() }
}
