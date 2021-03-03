package com.example.androiddevchallenge.data

import com.example.androiddevchallenge.SimpleApp
import com.example.androiddevchallenge.model.Dog
import com.example.androiddevchallenge.model.NetResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author: xuzhiyong
 * @date: 2021/3/2  下午4:17
 * @Email: 18971269648@163.com
 * @description:
 */
class Repository {

    suspend fun getDogsList(): Flow<NetResult<ArrayList<Dog>>>{
        return flow {
            var dogsList = ArrayList<Dog>()
            try {
                val assetsManager = SimpleApp.context.assets
                val inputReader = InputStreamReader(assetsManager.open("dogs.json"))
                val jsonString = BufferedReader(inputReader).readText()
                val typeOf = object : TypeToken<List<Dog>>() {}.type
                dogsList = Gson().fromJson(jsonString, typeOf)
                emit(NetResult.Success(dogsList))
            } catch (e: IOException) {
                emit(NetResult.Failure(e.cause))
            }
        }.flowOn(Dispatchers.IO)
    }
}