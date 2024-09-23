package com.kmlibs.supplin.fixtures

import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.fixtures.android.FakeRepository1
import com.kmlibs.supplin.fixtures.android.repository.FakeRepository
import com.kmlibs.supplin.supplinInjection

class Root1 @Supply constructor(
    private val child1: Child1,
    private val child2: Child2,
)

class Root2 @Supply constructor(
    private val child3: Child3,
)

class Root3 @Supply constructor(
    private val child4: Child4,
)

class Root4 @Supply constructor() {
    @Supply
    private lateinit var child1: Child1

    @Supply
    private lateinit var child2: Child2
}

class Root5 {
    @Supply
    private lateinit var child1: Child1

    @Supply
    private lateinit var child2: Child2
}

class Child1 @Supply constructor(
    private val grandChild: GrandChild1,
)

class Child2 @Supply constructor()

class Child3 @Supply constructor(
    private val grandChild: GrandChild2
)

class Child4(
    @Supply
    private val grandChild: GrandChild1,
    @Supply
    private val grandChild2: GrandChild2,
)

class GrandChild1 @Supply constructor() {
    @Supply
    private lateinit var grandGrandChild: GreatGrandChild1
}

class GrandChild2 @Supply constructor(
    @FakeRepository1
    private val repository: FakeRepository,
)

class GreatGrandChild1 @Supply constructor()

/*
class Test1 {
    val root: Root1 by supplinInjection<Root1>()
}

class Test2 {
    val root: Root2 by supplinInjection<Root2>()
}

class Test3 {
    val root: Root3 by supplinInjection<Root3>()
}

class Test4 {
    val root: Root4 by supplinInjection<Root4>()
}

class Test5 {
    val root: Root5 by supplinInjection<Root5>()
}
*/
