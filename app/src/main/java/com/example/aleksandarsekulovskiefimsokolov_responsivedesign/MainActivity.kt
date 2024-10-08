package com.example.aleksandarsekulovskiefimsokolov_responsivedesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aleksandarsekulovskiefimsokolov_responsivedesign.ui.theme.AleksandarSekulovskiEfimSokolovResponsiveDesignTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AleksandarSekulovskiEfimSokolovResponsiveDesignTheme {
                var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }
                val windowInfo = calculateCurrentWindowInfo()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (!windowInfo.isHorizontal) { // Menu responds to isHorizontal
                            NavigationMenu(
                                isHorizontal = windowInfo.isHorizontal,
                                currentScreen = currentScreen,
                                onMenuItemClick = { screen ->
                                    currentScreen = screen
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    MainContent(
                        windowInfo = windowInfo,
                        currentScreen = currentScreen,
                        onScreenChange = { screen ->
                            currentScreen = screen
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

enum class Screen {
    HOME, PROFILE, SETTINGS
}

data class WindowInfo(
    val isWideScreen: Boolean,
    val isHorizontal: Boolean
)

@Composable
fun calculateCurrentWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val isHorizontal = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val isWideScreen = configuration.screenWidthDp >= 600
    return WindowInfo(
        isWideScreen = isWideScreen,
        isHorizontal = isHorizontal
    )
}

@Composable
fun MainContent(
    windowInfo: WindowInfo,
    currentScreen: Screen,
    onScreenChange: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    if (windowInfo.isHorizontal) {
        Row(modifier = modifier.fillMaxSize()) {
            NavigationMenu(
                isHorizontal = windowInfo.isHorizontal,
                currentScreen = currentScreen,
                onMenuItemClick = onScreenChange,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
            )
            Divider(color = Color.Gray, modifier = Modifier.width(1.dp))
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                when (currentScreen) {
                    Screen.HOME -> {
                        HomeContent(isWideScreen = windowInfo.isWideScreen)
                    }
                    Screen.PROFILE -> {
                        ProfileContent(isWideScreen = windowInfo.isWideScreen, maxWidth)
                    }
                    Screen.SETTINGS -> {
                        SettingsContent(isWideScreen = windowInfo.isWideScreen)
                    }
                }
            }
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                when (currentScreen) {
                    Screen.HOME -> {
                        HomeContent(isWideScreen = windowInfo.isWideScreen)
                    }
                    Screen.PROFILE -> {
                        ProfileContent(isWideScreen = windowInfo.isWideScreen, maxWidth)
                    }
                    Screen.SETTINGS -> {
                        SettingsContent(isWideScreen = windowInfo.isWideScreen)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContent(isWideScreen: Boolean, modifier: Modifier = Modifier) {
    var selectedItem by rememberSaveable { mutableStateOf<String?>(null) }
    val items = (1..50).map { "Item $it" }

    if (isWideScreen) {
        Row(modifier = modifier.fillMaxSize()) {
            ItemList(
                items = items,
                onItemSelected = { selectedItem = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Divider(color = Color.Gray, modifier = Modifier.width(1.dp))
            ItemDetails(
                item = selectedItem,
                isWideScreen = isWideScreen,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            if (selectedItem == null) {
                ItemList(
                    items = items,
                    onItemSelected = { selectedItem = it },
                    modifier = Modifier.weight(1f)
                )
            } else {
                ItemDetails(
                    item = selectedItem,
                    isWideScreen = isWideScreen,
                    onBack = { selectedItem = null },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ProfileContent(isWideScreen: Boolean, maxWidth: Dp, modifier: Modifier = Modifier) {
    if (isWideScreen) {
        Box(modifier = modifier.fillMaxSize()) {
            val halfWidth = maxWidth / 2
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .width(halfWidth)
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Profile Info",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Name: John Doe")
                    Text("Email: john.doe@example.com")
                }
                Divider(color = Color.Gray, modifier = Modifier.width(1.dp))
                Column(
                    modifier = Modifier
                        .width(halfWidth)
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Additional Details",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Address: 123 Main Street")
                    Text("Phone: +1 234 567 890")
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Name: John Doe")
            Text("Email: john.doe@example.com")
            Text("Address: 123 Main Street")
            Text("Phone: +1 234 567 890")
        }
    }
}

@Composable
fun SettingsContent(isWideScreen: Boolean, modifier: Modifier = Modifier) {
    var selectedSetting by rememberSaveable { mutableStateOf<String?>(null) }
    val settingsItems = listOf("Account", "Notifications", "Privacy", "Security", "About")

    if (isWideScreen) {
        Row(modifier = modifier.fillMaxSize()) {
            SettingsList(
                settingsItems = settingsItems,
                onSettingSelected = { selectedSetting = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Divider(color = Color.Gray, modifier = Modifier.width(1.dp))
            SettingsDetails(
                setting = selectedSetting,
                isWideScreen = isWideScreen,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            if (selectedSetting == null) {
                SettingsList(
                    settingsItems = settingsItems,
                    onSettingSelected = { selectedSetting = it },
                    modifier = Modifier.weight(1f)
                )
            } else {
                SettingsDetails(
                    setting = selectedSetting,
                    isWideScreen = isWideScreen,
                    onBack = { selectedSetting = null },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SettingsList(
    settingsItems: List<String>,
    onSettingSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(settingsItems.size) { index ->
            val item = settingsItems[index]
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSettingSelected(item) }
                    .padding(vertical = 8.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun SettingsDetails(
    setting: String?,
    isWideScreen: Boolean,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (setting != null) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            if (!isWideScreen) {
                Button(onClick = onBack) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Text(
                text = setting,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Details for $setting settings.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        Text(
            text = "Select a setting to see details.",
            modifier = modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ItemList(items: List<String>, onItemSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items.size) { index ->
            val item = items[index]
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemSelected(item) }
                    .padding(16.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun ItemDetails(
    item: String?,
    isWideScreen: Boolean,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (item != null) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            if (!isWideScreen) {
                Button(onClick = onBack) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Text(
                text = "Details of $item",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        Text(
            text = "No item selected",
            modifier = modifier.padding(16.dp)
        )
    }
}

@Composable
fun NavigationMenu(
    isHorizontal: Boolean,
    currentScreen: Screen,
    onMenuItemClick: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        Screen.HOME to "Home",
        Screen.PROFILE to "Profile",
        Screen.SETTINGS to "Settings"
    )

    if (isHorizontal) {
        Column(
            modifier = modifier
                .width(150.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            menuItems.forEach { (screen, title) ->
                val isSelected = screen == currentScreen
                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
                val textColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary

                Text(
                    text = title,
                    color = textColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .clickable { onMenuItemClick(screen) }
                        .padding(16.dp)
                )
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            menuItems.forEach { (screen, title) ->
                val isSelected = screen == currentScreen
                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
                val textColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary

                Text(
                    text = title,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(backgroundColor)
                        .clickable { onMenuItemClick(screen) }
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AleksandarSekulovskiEfimSokolovResponsiveDesignTheme {
        val windowInfo = WindowInfo(isWideScreen = true, isHorizontal = false)
        MainContent(
            windowInfo = windowInfo,
            currentScreen = Screen.HOME,
            onScreenChange = {}
        )
    }
}
