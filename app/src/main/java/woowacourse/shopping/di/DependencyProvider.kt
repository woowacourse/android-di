package woowacourse.shopping.di

import kotlin.reflect.KClassifier

interface DependencyProvider {
    fun <T : Any> getInstance(key: KClassifier): T
}
