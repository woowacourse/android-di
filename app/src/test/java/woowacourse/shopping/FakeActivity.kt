package woowacourse.shopping

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class FakeActivity : AppCompatActivity() {
    val firstViewModel by viewModels<FirstSuccessCaseViewModel> { GlobalViewModelFactory() }
    val secondViewModel by viewModels<FirstFailureCaseViewModel> { GlobalViewModelFactory() }
    val thirdViewModel by viewModels<SecondSuccessCaseViewModel> { GlobalViewModelFactory() }
    val fourthViewModel by viewModels<ThirdSuccessCaseViewModel> { GlobalViewModelFactory() }
}
