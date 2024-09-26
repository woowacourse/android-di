package woowa.shopping.di.libs.android.sample

internal class SampleRepository(private val sampleService: SampleService) {
    fun sample(): String {
        return sampleService.sample()
    }
}
