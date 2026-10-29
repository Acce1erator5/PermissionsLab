package uz.ttpu.permissionslab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenTabs()
                }
            }
        }
    }
}

@Composable
fun MainScreenTabs() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Network", "Camera", "Overlay", "Location")

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            when (selectedTab) {
                0 -> NetworkScreen()
                1 -> CameraScreen()
                2 -> OverlayScreen()
                3 -> LocationScreen()

            }
        }
    }
}
