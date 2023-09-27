package woowacourse.shopping.fake.repository

import android.content.Context
import woowacourse.shopping.annotation.ApplicationLifecycle

class FakeCartProductDao(context: Context)

fun createFakeCartProductDao(@ApplicationLifecycle context: Context): FakeCartProductDao =
    FakeCartProductDao(context)
