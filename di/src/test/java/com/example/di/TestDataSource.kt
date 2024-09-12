package com.example.di

import javax.inject.Singleton

interface TestDataSource

@Singleton
class SingletonDataSource : TestDataSource

class RemoteTestDataSource : TestDataSource

class LocalTestDataSource : TestDataSource
