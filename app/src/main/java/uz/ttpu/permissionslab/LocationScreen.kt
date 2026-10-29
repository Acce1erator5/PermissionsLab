package uz.ttpu.permissionslab

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun LocationScreen() {
    val context = LocalContext.current
    var preciseGranted by remember { mutableStateOf(false) }

    var approxGranted by remember { mutableStateOf(false) }

    fun updateStates(ctx: Context) {
        preciseGranted = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        approxGranted = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        updateStates(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        preciseGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: preciseGranted
        approxGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: approxGranted
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val statusText = when {
            preciseGranted -> "Precise location granted"
            approxGranted -> "Approximate location granted"
            else -> "No location permission"
        }
        Text("Location Status: $statusText")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            launcher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }) {
            Text("Request Location")
        }
    }
}
