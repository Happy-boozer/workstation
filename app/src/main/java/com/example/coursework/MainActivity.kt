package com.example.coursework

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.renderscript.Matrix3f
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coursework.ui.theme.CourseWorkTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            CourseWorkTheme {

                NAVBBAR()
                //val context = LocalContext.current // Получаем текущий Context
                //val intent = Intent(context, MainActivity2::class.java)

            }

        }
    }


    @Composable
    fun SongsScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        val intent1 = Intent(context, ADDCAR::class.java)
        Box(
            modifier = Modifier.fillMaxSize(),
            //contentAlignment = Alignment.C
        ) {
            Column (modifier = Modifier.align(Alignment.TopEnd)){
            //Spacer(modifier = Modifier.height(50.dp))
            Button(modifier = Modifier.padding(40.dp),
                onClick = {
                    context.startActivity(intent1)
                }
            ){ Text("Добавить автомобиль")}
        }
        }
            //context.startActivity(intent)
    }

    @Composable
    fun AlbumScreen(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        val intent = Intent(context, MainActivity2::class.java)
        context.startActivity(intent)
    }

    @Composable
    fun PlaylistScreen(modifier: Modifier = Modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Playlist Screen")
        }
    }

    enum class Destination(
        val route: String,
        val label: String,
        val icon: ImageVector,
        val contentDescription: String
    ) {
        SONGS("cars", "Cars", Icons.Default.Info, "Cars"),
        ALBUM("info", "Info", Icons.Default.Build, "Info"),
        PLAYLISTS("playlist", "Playlist", Icons.Default.Create, "Playlist")
    }

    @Composable
    fun AppNavHost(
        navController: NavHostController,
        startDestination: Destination,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController,
            startDestination = startDestination.route
        ) {
            Destination.entries.forEach { destination ->
                composable(destination.route) {
                    when (destination) {
                        Destination.SONGS -> SongsScreen()
                        Destination.ALBUM -> AlbumScreen()
                        Destination.PLAYLISTS -> PlaylistScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun NAVBBAR(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        val startDestination = Destination.SONGS
        var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
        val _context = LocalContext.current

        Scaffold(
            modifier = modifier,
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    Destination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedDestination == index,
                            onClick = {
                                navController.navigate(route = destination.route)
                                selectedDestination = index
                                //val context = _context // Получаем текущий Context


                            },
                            {
                                Icon(
                                    destination.icon,
                                    contentDescription = destination.contentDescription
                                )

                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        ) { contentPadding ->
            AppNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
        }


    }
}