package woowacourse.shopping.di.module

import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ViewModelModule

class DefaultViewModelModule(activityRetainedModule: ActivityRetainedModule) :
    ViewModelModule(activityRetainedModule)
