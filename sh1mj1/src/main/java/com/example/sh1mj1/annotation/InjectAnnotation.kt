package com.example.sh1mj1.annotation

@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val value: String, val generate: Boolean = false)

@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope
