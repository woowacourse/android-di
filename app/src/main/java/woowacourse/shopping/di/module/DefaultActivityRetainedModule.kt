package woowacourse.shopping.di.module

import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule

class DefaultActivityRetainedModule(applicationModule: ApplicationModule) :
    ActivityRetainedModule(applicationModule)
