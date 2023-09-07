package woowacourse.shopping.di.util

fun throwNotExistDependency(clazz: Class<*>) {
    throw IllegalArgumentException("[ERROR] 주입할 의존성이 존재하지 않습니다. parameterType: $clazz")
}
