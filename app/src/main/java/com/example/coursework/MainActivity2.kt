package com.example.coursework

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.coursework.presentation.theme.CourseWorkTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

fun read ():String{

    val content: String = key().key()
    return content
}

class MainActivity2 : ComponentActivity() {
    //private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val API_Key = read()
        MapKitFactory.setApiKey(API_Key)
        MapKitFactory.initialize(this)


        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current // Получаем текущий Context
            val intent = Intent(context, MainActivity::class.java)
            CourseWorkTheme {
            Box(modifier = Modifier.fillMaxSize())
            {
                Button(modifier = Modifier.padding(50.dp),
                    onClick = {
                        context.startActivity(intent)
                    },
                )
                { Text("Вернуться")}

                Column(modifier = Modifier.fillMaxWidth().align(Alignment.Center)) {
                    Text("Контактная информация")
                    Text("номер телефона")
                    Text("+78005552222")
                }
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f)
                .align(Alignment.BottomEnd)
                )

            {
                YandexMapScreen()
            }}}



        }
    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()

    }

    override fun onStop() {

        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    CourseWorkTheme {
        Greeting2("Android")
    }
}

@Composable
fun YandexMapScreen() {
    val context = LocalContext.current
    val imageProvider = ImageProvider.fromResource(context, R.drawable.img)



    AndroidView(
        factory = { context ->
            MapView(context).apply {
                val map = mapWindow.map

                map.move(
                    CameraPosition(
                        Point(55.575001, 37.598303), // Москва
                        18.0f,  // zoom
                        0.0f,   // azimuth
                        0.0f    // tilt
                    )
                )
                val placemark = map.mapObjects.
                addPlacemark().apply { geometry = Point(55.575001, 37.598303)
                    setIcon(imageProvider, IconStyle().apply { scale = 0.3f })}
            }


        },

        modifier = Modifier.fillMaxSize()
    )

}