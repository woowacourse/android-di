package com.medandro.di

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    override fun onCleared() {
        // 모든 DIContainer에서 이 ViewModel의 스코프 정리
        DIContainer.clearViewModelScopeGlobally(this)
        super.onCleared()
    }
}
