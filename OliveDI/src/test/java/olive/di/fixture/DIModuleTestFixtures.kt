package olive.di.fixture

import olive.di.DIModule

interface Parent

class Child : Parent

abstract class AbstractTestModule : DIModule {
    abstract fun bindChild(child: Child): Parent
}

class TestModule : DIModule {
    fun bindChild(): Child = Child()
}
