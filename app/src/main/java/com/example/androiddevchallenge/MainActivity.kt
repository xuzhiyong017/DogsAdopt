/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import com.example.androiddevchallenge.model.Dog
import com.example.androiddevchallenge.ui.theme.MyTheme

const val SELECT_POS = "selected_position"
const val SELECT_DOG = "selected_dog"

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Home")
                        },
                        backgroundColor = Color.Transparent,elevation = 0.dp
                    )
                }) {
                    ShowList(this,loading = viewModel.mLoading,dogs = viewModel.dogsList){ position: Int, dog: Dog ->
                        Intent(this, DogDetailActivity::class.java).apply {
                            putExtra(SELECT_POS, position)
                            putExtra(SELECT_DOG, dog)
                            startForResult.launch(this)
                        }
                    }
                }
            }
        }
        viewModel.fetchDogsList()
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val adopted = it.getBooleanExtra(ADOPTED, false)
                    val position = it.getIntExtra(SELECT_POS, 0)
                    if (adopted) {
                        viewModel.setDogAdopted(position)
                    }
                }
            }
        }
}

@Composable
fun ShowList(owner: LifecycleOwner,loading: LiveData<Boolean>, dogs: LiveData<List<Dog>>, onClick: (index: Int, dog: Dog) -> Unit) {
    val isloading by loading.observeAsState()
    val list by dogs.observeAsState()
    isloading?.let {
        if(it){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }else{
            list?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                ) {
                    LazyColumn(Modifier.fillMaxWidth()) {
                        itemsIndexed(it) { position, dog ->
                            DisplayDogItem(position, dog, onClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayDogItem(position: Int, dog: Dog, onClick: (position: Int, dog: Dog) -> Unit) {
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .requiredHeight(220.dp)
            .clickable { onClick(position, dog) }
    ) {
        CardItem(name = dog.name, avatar = dog.avatarName)
    }
}

@Composable
fun CardItem(name: String, avatar: String) {
    val imageIdentity = SimpleApp.context.resources.getIdentifier(
        avatar, "drawable",
        SimpleApp.context.packageName
    )
    val image: Painter = painterResource(imageIdentity)
    Image(
        painter = image,
        contentDescription = name,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Surface(
            color = Color.Shadow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}



val Color.Companion.Shadow: Color
    get() {
        return Color(0x99000000)
    }
