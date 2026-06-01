package com.example.coursework

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework.presentation.addcar.AddCarState
import com.example.coursework.presentation.addcar.AddCarViewModel
import com.example.coursework.presentation.theme.CourseWorkTheme

class ADDCAR : ComponentActivity() {

    private val viewModel: AddCarViewModel by viewModels {
        AddCarViewModel.Factory((applicationContext as App).container.addCarUseCase)
    }

    private val carBrands = listOf(
        "Audi", "BMW", "Chevrolet", "Ford", "Honda", "Hyundai",
        "Kia", "Land Rover", "Mazda", "Mercedes-Benz", "Mitsubishi",
        "Nissan", "Renault", "Skoda", "Subaru", "Toyota", "Volkswagen", "ВАЗ (LADA)"
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val carName = remember { mutableStateOf("") }
            val vin = remember { mutableStateOf("") }
            val plate = remember { mutableStateOf("") }
            var selectedBrand by remember { mutableStateOf("") }
            var brandExpanded by remember { mutableStateOf(false) }

            val state by viewModel.state.collectAsState()
            val login = (applicationContext as App).container.sessionManager.getLogin() ?: ""

            LaunchedEffect(state) {
                if (state is AddCarState.Success) {
                    startActivity(Intent(this@ADDCAR, MainActivity::class.java))
                    finish()
                }
            }

            CourseWorkTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Добавить автомобиль") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        FormSection(title = "Основная информация", icon = "🚗") {
                            OutlinedTextField(
                                value = carName.value,
                                onValueChange = { carName.value = it },
                                label = { Text("Название (например: Моя машина)") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(20.dp))
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            ExposedDropdownMenuBox(
                                expanded = brandExpanded,
                                onExpandedChange = { brandExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedBrand,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Марка автомобиля") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandExpanded) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = brandExpanded,
                                    onDismissRequest = { brandExpanded = false }
                                ) {
                                    carBrands.forEach { brand ->
                                        DropdownMenuItem(
                                            text = { Text(brand) },
                                            onClick = {
                                                selectedBrand = brand
                                                brandExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        FormSection(title = "Документы", icon = "📄") {
                            OutlinedTextField(
                                value = plate.value,
                                onValueChange = { plate.value = it.uppercase() },
                                label = { Text("Государственный номер") },
                                placeholder = { Text("А123БВ77") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = vin.value,
                                onValueChange = { vin.value = it.uppercase() },
                                label = { Text("VIN-номер") },
                                placeholder = { Text("17 символов") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }

                        if (state is AddCarState.Error) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = (state as AddCarState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.addCar(login, "${selectedBrand} ${carName.value}".trim(), plate.value, vin.value) },
                            enabled = state !is AddCarState.Loading,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (state is AddCarState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Зарегистрировать автомобиль", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSection(title: String, icon: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            androidx.compose.foundation.layout.Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(icon, style = MaterialTheme.typography.titleMedium)
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
            content()
        }
    }
}
