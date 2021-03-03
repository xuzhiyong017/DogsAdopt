package com.example.androiddevchallenge.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author: xuzhiyong
 * @date: 2021/3/2  下午4:06
 * @Email: 18971269648@163.com
 */
@Parcelize
class Dog(
    val name: String,
    val avatarName: String,
    val desc: String,
    var adopted: Boolean
) : Parcelable {
}