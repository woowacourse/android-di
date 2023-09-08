package woowacourse.shopping.di.annotationdi

import woowacourse.shopping.di.Module

object FakeAnnotationDIModule : Module {
    fun provideFakeObj(): FakeObj = FakeObj
}
