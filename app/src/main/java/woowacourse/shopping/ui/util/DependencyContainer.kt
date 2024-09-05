package woowacourse.shopping.ui.util

import kotlin.reflect.KClassifier

interface DependencyContainer {
    fun <T : Any> getInstance(kClassifier: KClassifier): T
}
