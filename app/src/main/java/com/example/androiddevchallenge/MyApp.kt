package com.example.androiddevchallenge

import android.app.Application
import android.content.Context

/**
 * @author: xuzhiyong
 * @date: 2021/3/2  下午4:21
 * @Email: 18971269648@163.com
 * @description:
 */
class SimpleApp : Application() {

    companion object{
        lateinit var context: Context
    }


    override fun onCreate() {
        super.onCreate()
        context = this
    }
}