package woowacourse.shopping.di.module

import android.content.Context
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule

class DefaultActivityModule(
    activityContext: Context,
    activityRetainedModule: ActivityRetainedModule,
) : ActivityModule(activityContext, activityRetainedModule)
