package com.example.coursework

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.coursework.presentation.theme.CourseWorkTheme

class PreLOGIN : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val phone = remember{mutableStateOf("")}
            val lifecycleOwner = LocalLifecycleOwner.current
            val context = LocalContext.current // Получаем текущий Context
            val intent1 = Intent(context, MainActivity::class.java)
            val intent2 = Intent(context, Registration::class.java)
            //var text  by remember { mutableStateOf("") }
            val login = remember{mutableStateOf("")}
            val password = remember{mutableStateOf("")}

            CourseWorkTheme {

                Box(modifier = Modifier.fillMaxSize()) {
                    Column (modifier = Modifier.align(Alignment.TopCenter)){
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Номер телефона")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(value = login.value, onValueChange = {newText -> login.value = newText},
                            textStyle = TextStyle(fontSize = 28.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Пароль")
                        TextField(value = password.value, onValueChange = {newText -> password.value = newText},
                            visualTransformation = { filter(it) }
                        )

                    Row (modifier = Modifier.fillMaxWidth()){
                        Button(
                            onClick = {
                                if (password.value == "admin" &&  login.value == "1"){

                                }
                                else{
                                    val Data = Data.Builder()
                                        .putString("login", login.value)
                                        .putString("password", password.value)
                                        .build()
                                    val request = OneTimeWorkRequestBuilder<AuthWorkerSend>()
                                        .setInputData(Data)
                                        .build()
                                    val workManager = WorkManager.getInstance(context)
                                    workManager.enqueue(request)

                                    Log.d("WORKER_TEST", "Enqueue worker")

                                    WorkManager.getInstance(context).enqueue(request)

                                    workManager.getWorkInfoByIdLiveData(request.id)
                                        .observe(lifecycleOwner) { workInfo ->
                                            if (workInfo != null && workInfo.state.isFinished) {

                                                val response = workInfo.outputData.getString("response")

                                                Log.d("RESULT", response ?: "null")

                                                if (response == "ok") {
                                                    context.startActivity(intent1)
                                                } else {
                                                    Log.d("ERROR", "Неверные данные или ошибка")
                                                }
                                            }
                                        }

                                    //getting()
                                //context.startActivity(intent1)
                                }
                            },
                        )
                        { Text("Войти")}
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {

                                context.startActivity(intent2)
                            },
                        )
                        { Text("Зарегистрироваться")}}

                    }
                }
            }
        }
    }
}

fun filter(text: AnnotatedString): TransformedText{
    return TransformedText(
        AnnotatedString("*".repeat(text.text.length)),
        /**
         * [OffsetMapping.Identity] is a predefined [OffsetMapping] that can be used for the
         * transformation that does not change the character count.
         */
        OffsetMapping.Identity,
    )
}