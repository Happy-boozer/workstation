package com.example.coursework

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework.ui.theme.CourseWorkTheme

class PreLOGIN : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //val phone = remember{mutableStateOf("")}
            val context = LocalContext.current // Получаем текущий Context
            val intent1 = Intent(context, MainActivity::class.java)
            val intent2 = Intent(context, MainActivity::class.java)
            //var text  by remember { mutableStateOf("") }
            val message1 = remember{mutableStateOf("")}
            val message2 = remember{mutableStateOf("")}

            CourseWorkTheme {

                Box(modifier = Modifier.fillMaxSize()) {
                    Column (modifier = Modifier.align(Alignment.TopCenter)){
                        Spacer(modifier = Modifier.height(30.dp))
                        Text("Номер телефона")
                        //Text(message.value, fontSize = 28.sp)
                        TextField(value = message1.value, onValueChange = {newText -> message1.value = newText},
                            textStyle = TextStyle(fontSize = 28.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Пароль")
                        TextField(value = message2.value, onValueChange = {newText -> message2.value = newText},
                            visualTransformation = { filter(it) }
                        )
                    }
                    Row (modifier = Modifier.align(Alignment.Center)){
                        Button(
                            onClick = {
                                context.startActivity(intent1)
                            },
                        )
                        { Text("Войти")}
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                context.startActivity(intent2)
                            },
                        )
                        { Text("Зарегистрироваться")}

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

/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}*/

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CourseWorkTheme {
        Greeting("Android")
    }
}*/