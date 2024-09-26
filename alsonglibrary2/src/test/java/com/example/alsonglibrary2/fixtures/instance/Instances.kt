package com.example.alsonglibrary2.fixtures.instance

import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.dao.FakeDao
import com.example.alsonglibrary2.fixtures.repository.DefaultFakeRepository1
import com.example.alsonglibrary2.fixtures.repository.DefaultFakeRepository2
import com.example.alsonglibrary2.fixtures.repository.DefaultFakeRepository3

val fakeDao = FakeDao()

val defaultFakeRepository1 = DefaultFakeRepository1()

val defaultFakeRepository2 = DefaultFakeRepository2()

val defaultFakeRepository3 by lazy { AutoDIManager.createAutoDIInstance<DefaultFakeRepository3>() }
