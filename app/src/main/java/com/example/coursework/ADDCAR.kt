package com.example.coursework

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.ui.theme.CourseWorkTheme

class ADDCAR : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val message3 = remember{mutableStateOf("")}
            val message4 = remember{mutableStateOf("")}
            val message1 = remember{mutableStateOf("")}
            val context = LocalContext.current
            val intent1 = Intent(context, MainActivity::class.java)

            CourseWorkTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column (modifier = Modifier.align(Alignment.TopCenter)){
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Имя")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(value = message3.value, onValueChange = {newText -> message3.value = newText},
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Госномер")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(value = message4.value, onValueChange = {newText -> message4.value = newText},
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Марка автомобиля")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(value = message1.value, onValueChange = {newText -> message1.value = newText},
                            textStyle = TextStyle(fontSize = 28.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )

                }
                    Row (modifier = Modifier.align(Alignment.Center)){
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {
                                Toast.makeText(context, "Данные сохранены", Toast.LENGTH_SHORT).show()
                                context.startActivity(intent1)
                            },
                        )
                        { Text("Зарегистрировать автомобиль")}


                    }

            }
        }
    }
}
}

