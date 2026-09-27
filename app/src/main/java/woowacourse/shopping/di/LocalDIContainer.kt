package woowacourse.shopping.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalDIContainer =
    staticCompositionLocalOf<KirbyDIContainer> {
        error("KirbyDIContainer가 제공되지 않았습니다")
    }
