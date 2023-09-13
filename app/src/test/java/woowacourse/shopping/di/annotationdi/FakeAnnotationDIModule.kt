package woowacourse.shopping.di.annotationdi

import com.di.berdi.Module

object FakeAnnotationDIModule : Module {
    fun provideFakeObj(): FakeObj = FakeObj
}
