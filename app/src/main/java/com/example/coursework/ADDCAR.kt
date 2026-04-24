package com.example.coursework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.coursework.presentation.theme.CourseWorkTheme

class ADDCAR : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val name = remember { mutableStateOf("") }
            val VIN = remember { mutableStateOf("") }
            val plate = remember { mutableStateOf("") }

            var filteredSuggestions by remember { mutableStateOf(emptyList<String>()) }
            var showSuggestions by remember { mutableStateOf(false) }

            val context = LocalContext.current
            val user_login = context.openFileInput("config.txt").use{String(it.readBytes())}
            val intent1 = Intent(context, MainActivity::class.java)

            CourseWorkTheme {

                Box(modifier = Modifier.Companion.fillMaxSize()) {
                    Column(modifier = Modifier.Companion.align(Alignment.Companion.TopCenter)) {
                        Spacer(modifier = Modifier.Companion.height(30.dp))
                        //--
                        Text("Имя")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            value = name.value,
                            onValueChange = { newText -> name.value = newText },
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.Companion.height(30.dp))
                        //--
                        Text("Госномер")

                        TextField(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            value = plate.value,
                            onValueChange = { newText -> plate.value = newText },
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.Companion.height(30.dp))
                        //--
                        Text("VIN - номер")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            value = VIN.value,
                            onValueChange = { newText -> VIN.value = newText },
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.Companion.height(30.dp))
                        //--
                        Text("Марка автомобиля")
                        //Text(message.value, fontSize = 28.sp)

                        AutocompleteTextField() {}


//                        TextField(value = message1.value,
//                            onValueChange = {newText -> message1.value = newText},
//                            textStyle = TextStyle(fontSize = 28.sp),
//                            //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
//                        )


                        //AutocompleteTextField(){}


                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.Companion.height(30.dp))
                        Button(
                            onClick = {
                                //занести данные в бд
                                val Data = Data.Builder()
                                    .putString("login", user_login)
                                    .putString("VIN", VIN.value)
                                    .putString("name", name.value)
                                    .putString("plate", plate.value)
                                    .build()
                                val request = OneTimeWorkRequestBuilder<SendCarWorker>()
                                    .setInputData(Data)
                                    .build()
                                val workManager = WorkManager.getInstance(context)
                                workManager.enqueue(request)

                                Log.d("WORKER_TEST", "Enqueue worker")

                                WorkManager.getInstance(context).enqueue(request)

                                Toast.makeText(context, "Данные сохранены", Toast.LENGTH_SHORT)
                                    .show()
                                context.startActivity(intent1)
                            },
                        )
                        { Text("Зарегистрировать автомобиль") }
                    }
                        Spacer(modifier = Modifier.Companion.height(30.dp))
                        Button(
                            onClick = {
                                context.startActivity(intent1)
                            },
                        )
                        { Text("Вернуться") }


                    }
                }
            }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable

    fun AutocompleteTextField(
        suggestions: List<String> = listOf("Ford", "Kia", "Chevrolet", "Nissan", "Land Rover",
            "Toyota", "Dodge", "Cadilac", "Subaru", "Mitsubishi", "Hundai"),
        onItemSelected: (String) -> Unit
    ) {
        var query by remember { mutableStateOf("") }
        var filteredSuggestions by remember { mutableStateOf(emptyList<String>()) }
        var showSuggestions by remember { mutableStateOf(false) }

        Column {
            // Используем OutlinedTextField для лучшего вида
            OutlinedTextField(
                value = query,
                onValueChange = { newQuery ->
                    query = newQuery
                    filteredSuggestions = suggestions.filter {
                        it.contains(newQuery, ignoreCase = true)
                    }
                    showSuggestions = newQuery.isNotEmpty() && filteredSuggestions.isNotEmpty()
                },
                label = { Text("Введите текст и выберите вариант") },
                modifier = Modifier.Companion.fillMaxWidth(),
                singleLine = true
            )

            if (showSuggestions) {
                // Используем Surface вместо Card
                Surface(
                    modifier = Modifier.Companion
                        .fillMaxWidth().background(Color.Companion.Blue)
                        .heightIn(max = 200.dp),
                    //elevation = 4.dp,
                    //shape = RoundedCornerShape(4.dp)
                ) {
                    Column(
                        modifier = Modifier.Companion.verticalScroll(rememberScrollState())
                    ) {
                        filteredSuggestions.forEachIndexed { index, suggestion ->
                            Box(
                                modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple() // Явное указание ripple
                                    ) {
                                        query = suggestion
                                        onItemSelected(suggestion)
                                        showSuggestions = false
                                    }
                            ) {
                                Text(
                                    text = suggestion,
                                    modifier = Modifier.Companion
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }

                            if (index < filteredSuggestions.lastIndex) {
                                Divider(
                                    thickness = 0.5.dp,
                                    color = Color.Companion.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}