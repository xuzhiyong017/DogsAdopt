package com.example.androiddevchallenge

import android.util.Log
import androidx.lifecycle.*
import com.example.androiddevchallenge.data.Repository
import com.example.androiddevchallenge.model.Dog
import com.example.androiddevchallenge.model.doFailure
import com.example.androiddevchallenge.model.doSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * @author: xuzhiyong
 * @date: 2021/3/2  下午4:15
 * @Email: 18971269648@163.com
 * @description:
 */

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel(private val repository: Repository) : ViewModel() {

    private var _Loading = MutableLiveData<Boolean>(false)
    var mLoading: LiveData<Boolean> = _Loading

    private val _dogs = MutableLiveData<List<Dog>>()

    val dogsList: LiveData<List<Dog>> = _dogs

    private val _failure = MutableLiveData<String>()
    val failure = _failure

    fun fetchDogsList() = viewModelScope.launch {
        repository.getDogsList().onStart {
            _Loading.postValue(true)
        }.catch {
            Log.d("Dogs",it.message+"")
            Log.d("Dogs",it.cause?.message+"")

            _Loading.postValue(false)
        }.onCompletion {
            _Loading.postValue(false)
        }.collectLatest {

            it.doFailure {
                _failure.value =it?.message ?: "failure"
            }
            it.doSuccess {
                _dogs.postValue(it)
            }
        }
    };

    fun setDogAdopted(position: Int) {
        _dogs.value?.get(position)?.adopted = true
    }


}