package com.example.conper

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import coil.compose.AsyncImage
import com.example.conper.ui.theme.ConperTheme
import com.example.conper.ui.theme.viewModel
import java.util.Calendar

class MainActivity : ComponentActivity() {
    private  val viewModel by viewModels<viewModel>()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ),
            0
        )
        super.onCreate(savedInstanceState)
        val pro = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val dataDur = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR,-10)
        }.timeInMillis

        val sele = "${ MediaStore.Images.Media.DATE_TAKEN} >= ?"
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI ,
            pro,
            sele,
            arrayOf(dataDur.toString()),
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        )?.use {
            val idC = it.getColumnIndex( MediaStore.Images.Media._ID)
            val NameC = it.getColumnIndex( MediaStore.Images.Media.DISPLAY_NAME)
            val images = mutableListOf<ContentProviderDataItem>()
            while (it.moveToNext()){
                val id = it.getLong(idC)
                val dN =it.getString(NameC)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(ContentProviderDataItem(id , dN , uri ))
            }
            viewModel.udataeImg(images)


        }
        setContent {
            LazyColumn(Modifier.fillMaxSize()) {
                items(viewModel.imgState.value){ image->
                    Column(Modifier.fillMaxWidth()) {
                        AsyncImage(model = image.uri, contentDescription = "image")
                        Text(text= image.name)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConperTheme {
        Greeting("Android")
    }
}
data class ContentProviderDataItem(
    val id: Long,
    val name: String,
    val uri: Uri
)