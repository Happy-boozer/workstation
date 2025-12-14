package com.example.coursework

import java.io.File
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.coursework.ui.theme.CourseWorkTheme
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

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
            Box(modifier = Modifier.fillMaxSize())
            {
            Box(modifier = Modifier.size(height = 250.dp, width = 500.dp)
                .align(Alignment.BottomEnd)
                )
                //contentAlignment = Alignment.CenterEnd)
            {
                YandexMapScreen()
            }}

            //YandexMapScreen()

        }
    }
    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()

    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()

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
    AndroidView(
        factory = { context ->
            MapView(context).apply {

                map.move(
                    CameraPosition(
                        Point(55.574823, 37.597943), // Москва
                        90.0f,  // zoom
                        0.0f,   // azimuth
                        0.0f    // tilt
                    )
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}