package woowacourse.shopping

class DependencyCycleException(
    message: String,
) : RuntimeException(message)

class NoProviderException(
    message: String,
) : RuntimeException(message)
