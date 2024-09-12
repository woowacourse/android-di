package olive.di.fixture

import olive.di.DIModule
import olive.di.annotation.Inject
import olive.di.annotation.Qualifier

@Qualifier
annotation class Child1

@Qualifier
annotation class Child2

@Child1
class ChildImpl1 : Parent

@Child2
class ChildImpl2 : Parent

abstract class QualifierTestModule : DIModule {
    @Child1
    abstract fun bindChild1(child1: ChildImpl1): Parent

    @Child2
    abstract fun bindChild2(child2: ChildImpl2): Parent
}

class QualifierTest1 {
    @Child1
    @Inject
    lateinit var parent: Parent
}

class QualifierTest2 {
    @Child2
    @Inject
    lateinit var parent: Parent
}
