package com.example.seogi.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

inline fun <reified T : ViewModel> AppCompatActivity.viewModel() = viewModels<T> { ViewModelFactory(DiApplication.diContainer) }
