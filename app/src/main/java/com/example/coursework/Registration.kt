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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.coursework.presentation.theme.CourseWorkTheme

private const val BASE_URL = "https://httpbin.org"
private const val GET_UUID = "$BASE_URL/uuid"

/*fun sinmple(){
    val client = HttpClient()

    GlobalScope.launch(Dispatchers.IO) {
        val data = client.get<String>(GET_UUID)
            // Log.i("$BASE_TAG Simple case ", data)
    }
}*/


class Registration : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current // Получаем текущий Context
            val intent1 = Intent(context, PreLOGIN::class.java)
            //val intent2 = Intent(context, Registration::class.java)
            //var text  by remember { mutableStateOf("") }
            val message1 = remember{mutableStateOf("")}
            val message2 = remember{mutableStateOf("")}
            val message3 = remember{mutableStateOf("")}
            val message4 = remember{mutableStateOf("")}

            CourseWorkTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column (modifier = Modifier.align(Alignment.TopCenter)) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Имя")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(
                            value = message3.value,
                            onValueChange = { newText -> message3.value = newText },
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Фамилия")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(
                            value = message4.value,
                            onValueChange = { newText -> message4.value = newText },
                            textStyle = TextStyle(fontSize = 28.sp),

                            )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Номер телефона")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(
                            value = message1.value,
                            onValueChange = { newText -> message1.value = newText },
                            textStyle = TextStyle(fontSize = 28.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Пароль")
                        TextField(
                            value = message2.value,
                            onValueChange = { newText -> message2.value = newText },
                            //visualTransformation = { filter(it) }
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    context.startActivity(intent1)
                                },
                            )
                            { Text("Войти") }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    val Data = Data.Builder()
                                        .putString("name", message3.value + message4.value)
                                        .putString("phone_number", message1.value)
                                        .putString("password", message2.value)
                                        .build()
                                    val myWorkRequest1 = OneTimeWorkRequestBuilder<RegPostWorker>()
                                        .setInputData(Data)
                                        .build()
                                    WorkManager.getInstance(context).enqueue(myWorkRequest1)
                                    Toast.makeText(context, "Данные сохранены", Toast.LENGTH_SHORT)
                                        .show()
                                    //context.startActivity(intent2)

                                }
                            )
                            { Text("Зарегистрироваться") }
                        }

                    }
    }
}


}}}