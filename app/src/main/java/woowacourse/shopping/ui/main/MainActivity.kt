package woowacourse.shopping.ui.main

import android.os.Bundle
import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.scope.ActivityScope
import com.mission.androiddi.util.viewModel.viewModel
import com.woowacourse.bunadi.annotation.Inject
import woowacourse.shopping.databinding.ActivityMainBinding

class MainActivity : InjectableActivity() {
    override val activityClazz = MainActivity::class
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModel()

    @Inject
    @ActivityScope
    private lateinit var childShareResource: ChildShareResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBinding()

        // Activity와 Fragment의 의존성 공유를 확인하기 위한 코드
        childShareResource.print("MainActivity", this)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }
}
