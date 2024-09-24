package woowa.shopping.di.libs.android

import android.content.Context
import woowa.shopping.di.libs.container.ContainersDSL

fun ContainersDSL.androidContext(androidContext: Context) {
    container {
        single<Context> { androidContext }
    }
}
