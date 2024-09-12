package woowa.shopping.di.libs.android

import android.content.Context
import woowa.shopping.di.libs.container.ContainersDSL

// TODO 추후 android 용 di 모듈로 분리
fun ContainersDSL.androidContext(androidContext: Context) {
    container {
        single<Context> { androidContext }
    }
}
