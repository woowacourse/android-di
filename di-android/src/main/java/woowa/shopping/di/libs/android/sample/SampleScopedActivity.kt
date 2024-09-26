package woowa.shopping.di.libs.android.sample

import woowa.shopping.di.libs.android.ScopeActivity

internal class SampleScopedActivity : ScopeActivity() {
    val sampleRepository: SampleRepository by inject()
}
