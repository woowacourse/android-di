package woowacourse.shopping.sangoondi.fake

import woowacourse.shopping.sangoondi.annotation.Qualifier
import woowacourse.shopping.sangoondi.annotation.Singleton

interface Qualify
class QualifiedClass1 : Qualify
class QualifiedClass2 : Qualify
class QualifiedClass3 : Qualify
class Normal
class Normal2(normal: Normal)
class Single
class RealSingle(single: Single)

@Qualifier
annotation class TestQualifier1

@Qualifier
annotation class TestQualifier2

@Qualifier
annotation class TestQualifier3

object TestModule {
    fun provideNormal(): Normal = Normal()

    fun provideNormal2(normal: Normal): Normal2 = Normal2(normal)

    @Singleton
    fun provideSingle(normal: Normal): Single = Single()

    @TestQualifier1
    fun provideQualifiedClass1(): Qualify = QualifiedClass1()

    @TestQualifier2
    fun provideQualifiedClass2(): Qualify = QualifiedClass2()

    @Singleton
    @TestQualifier3
    fun provideQualifiedClass3(): Qualify = QualifiedClass3()

    fun provideRealSingleton(single: Single): RealSingle = RealSingle(single)
}
