package com.example.androiddevchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.data.Repository

/**
 * @author: xuzhiyong
 * @date: 2021/3/2  下午5:00
 * @Email: 18971269648@163.com
 * @description:
 */
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(Repository()) as T
    }
}