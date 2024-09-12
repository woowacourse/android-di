package com.example.sh1mj1

@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val value: String)
