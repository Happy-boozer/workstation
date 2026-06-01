package com.example.coursework

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.coursework.domain.model.Car
import com.example.coursework.presentation.cars.CarsState
import com.example.coursework.presentation.cars.CarsViewModel
import com.example.coursework.presentation.notifications.NotificationsState
import com.example.coursework.presentation.notifications.NotificationsViewModel
import com.example.coursework.presentation.theme.CourseWorkTheme

class MainActivity : ComponentActivity() {

    private val carsViewModel: CarsViewModel by viewModels {
        CarsViewModel.Factory((applicationContext as App).container.getCarsUseCase)
    }

    private val notificationsViewModel: NotificationsViewModel by viewModels {
        val container = (applicationContext as App).container
        NotificationsViewModel.Factory(container.getNotificationsUseCase, container.markNotificationReadUseCase)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                0
            )
        }

        MapKitFactory.setApiKey(key().key())
        MapKitFactory.initialize(this)
        enableEdgeToEdge()

        val login = (applicationContext as App).container.sessionManager.getLogin() ?: ""
        setContent {
            CourseWorkTheme {
                MainNavigation(login = login)
            }
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

    enum class Destination(val route: String, val label: String, val icon: ImageVector) {
        CARS("cars", "Автомобили", Icons.Default.Build),
        MAP("map", "Карта", Icons.Default.LocationOn),
        NOTIFICATIONS("notifications", "Уведомления", Icons.Default.Notifications),
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainNavigation(login: String) {
        var selectedTab by rememberSaveable { mutableIntStateOf(0) }
        val notifState by notificationsViewModel.state.collectAsState()
        val unreadCount = (notifState as? NotificationsState.Success)?.notifications?.count { !it.isRead } ?: 0

        Scaffold(
            bottomBar = {
                NavigationBar {
                    Destination.entries.forEachIndexed { index, dest ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = {
                                if (dest == Destination.NOTIFICATIONS && unreadCount > 0) {
                                    BadgedBox(badge = { Badge { Text(unreadCount.toString()) } }) {
                                        Icon(dest.icon, contentDescription = dest.label)
                                    }
                                } else {
                                    Icon(dest.icon, contentDescription = dest.label)
                                }
                            },
                            label = { Text(dest.label) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (Destination.entries[selectedTab]) {
                    Destination.CARS -> CarsTab(login)
                    Destination.MAP -> MapTab()
                    Destination.NOTIFICATIONS -> NotificationsTab(login)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CarsTab(login: String) {
        val state by carsViewModel.state.collectAsState()

        LaunchedEffect(login) {
            if (login.isNotBlank()) carsViewModel.loadCars(login)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Мои автомобили", fontWeight = FontWeight.Bold)
                            Text(
                                text = login,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    },
                    actions = {
                        androidx.compose.material3.IconButton(
                            onClick = { carsViewModel.loadCars(login) }
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Обновить",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { startActivity(Intent(this@MainActivity, ADDCAR::class.java)) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить автомобиль", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {
                when (val s = state) {
                    is CarsState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is CarsState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("😕", style = MaterialTheme.typography.displayMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(s.message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                        }
                    }
                    is CarsState.Success -> {
                        if (s.cars.isEmpty()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🚗", style = MaterialTheme.typography.displayLarge)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Нет автомобилей",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Нажмите + чтобы добавить первый",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                items(s.cars) { car -> CarCard(car) }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    @Composable
    fun CarCard(car: Car) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Chip(label = car.sign)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "VIN: ${car.vin}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    @Composable
    fun Chip(label: String) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }

    @Composable
    fun MapTab() {
        val imageProvider = ImageProvider.fromResource(
            androidx.compose.ui.platform.LocalContext.current, R.drawable.img
        )
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        mapWindow.map.move(
                            CameraPosition(Point(55.575001, 37.598303), 15.0f, 0.0f, 0.0f)
                        )
                        mapWindow.map.mapObjects.addPlacemark().apply {
                            geometry = Point(55.575001, 37.598303)
                            setIcon(imageProvider, IconStyle().apply { scale = 0.3f })
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            ContactCard(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NotificationsTab(login: String) {
        val state by notificationsViewModel.state.collectAsState()

        LaunchedEffect(login) {
            if (login.isNotBlank()) notificationsViewModel.load(login)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Уведомления", fontWeight = FontWeight.Bold) },
                    actions = {
                        androidx.compose.material3.IconButton(onClick = { notificationsViewModel.load(login) }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Обновить", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {
                when (val s = state) {
                    is NotificationsState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is NotificationsState.Error -> Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("😕", style = MaterialTheme.typography.displayMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(s.message, color = MaterialTheme.colorScheme.error)
                    }
                    is NotificationsState.Success -> {
                        val unread = s.notifications.filter { !it.isRead }
                        val read   = s.notifications.filter { it.isRead }
                        if (s.notifications.isEmpty()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🔔", style = MaterialTheme.typography.displayLarge)
                                Spacer(Modifier.height(16.dp))
                                Text("Уведомлений нет", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (unread.isNotEmpty()) {
                                    item {
                                        Text("Новые", style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(vertical = 4.dp))
                                    }
                                    items(unread) { notif ->
                                        NotificationCard(
                                            type = notif.type, title = notif.title,
                                            body = notif.body, time = notif.time,
                                            isRead = notif.isRead,
                                            onClick = { notificationsViewModel.markRead(notif.id, login) }
                                        )
                                    }
                                }
                                if (read.isNotEmpty()) {
                                    item {
                                        Spacer(Modifier.height(4.dp))
                                        HorizontalDivider()
                                        Spacer(Modifier.height(4.dp))
                                        Text("Ранее", style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(vertical = 4.dp))
                                    }
                                    items(read) { notif ->
                                        NotificationCard(
                                            type = notif.type, title = notif.title,
                                            body = notif.body, time = notif.time,
                                            isRead = notif.isRead, onClick = {}
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    @Composable
    fun NotificationCard(type: String, title: String, body: String, time: String, isRead: Boolean, onClick: () -> Unit = {}) {
        val (emoji, containerColor, iconTint) = when (type) {
            "ORDER"    -> Triple("🔧", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary)
            "REMINDER" -> Triple("⏰", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)
            else       -> Triple("🎁", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.secondary)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(if (isRead) 0.dp else 3.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isRead)
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, style = MaterialTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isRead) FontWeight.Normal else FontWeight.Bold,
                            color = if (isRead)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                        if (!isRead) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = body,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }

    @Composable
    fun ContactCard(modifier: Modifier = Modifier) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Контактная информация",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    }
                    Column {
                        Text("Телефон поддержки", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("+7 800 555-22-22", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(22.dp))
                    }
                    Column {
                        Text("Наш адрес", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Москва, МКАД 32й км вл 15", style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
