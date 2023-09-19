package woowacourse.shopping.di

import android.content.Context
import com.created.customdi.DiContainer
import com.created.customdi.annotation.ApplicationContext

object GuiltyModule {
    @ApplicationContext
    fun provideApplicationContext(): Context = DiContainer.context as Context
}
