package uz.ttpu.permissionslab


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    var permissionState by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA))
    }

    var shouldShowRationale by remember {
        mutableStateOf(activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA) } ?: false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionState = if (isGranted) PackageManager.PERMISSION_GRANTED else PackageManager.PERMISSION_DENIED
        shouldShowRationale = activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA) } ?: false
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (Build.VERSION.SDK_INT < 23) {
            Text("API < 23: Permission granted automatically")
        } else {
            when {
                permissionState == PackageManager.PERMISSION_GRANTED -> {
                    Text("Camera Status: Permission Granted")
                }
                shouldShowRationale -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Camera permission is recommended for features.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                            Text("Retry Request")
                        }
                    }
                }
                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Camera access required.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            // Если это не первый запуск и rationale false -> дважды отклонено
                            if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA) } == false &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                context.startActivity(intent)
                            } else {
                                launcher.launch(Manifest.permission.CAMERA)
                            }
                        }) {
                            Text("Grant Permission / Open Settings")
                        }
                    }
                }
            }
        }
    }
}
