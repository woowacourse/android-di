package woowacourse.shopping.di

import androidx.compose.runtime.staticCompositionLocalOf
import woowacourse.di.KirbyDIContainer

val LocalDIContainer =
    staticCompositionLocalOf<KirbyDIContainer> {
        error("KirbyDIContainer가 제공되지 않았습니다")
    }
