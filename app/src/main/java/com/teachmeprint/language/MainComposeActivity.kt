package com.teachmeprint.language

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.teachmeprint.designsystem.theme.TeachMePrintTheme
import com.teachmeprint.language.swipeable_permission.SwipePermissionRoute

class MainComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TeachMePrintTheme {
                SwipePermissionRoute()
            }
        }
    }

    /* @Composable
     fun HomeScreen() {
         val context = LocalContext.current
         val allPermissions = rememberMultiplePermissionsState(PERMISSIONS)

         var showDialog by remember {
             mutableStateOf(false)
         }

         Scaffold(
             modifier = Modifier.systemBarsPadding(),
             topBar = {
                 CenterAlignedTopAppBar(
                     title = {
                         Text(
                             stringResource(id = R.string.app_name)
                         )
                     },
                     actions = {
                         IconButton(onClick = { }) {
                             Icon(Icons.Rounded.Settings, contentDescription = null)
                         }
                     }
                 )
             },
             floatingActionButtonPosition = FabPosition.Center,
             floatingActionButton = {
                 HomeToggleScreenCaptureButton(allPermissions.allPermissionsGranted) {
                     if (allPermissions.shouldShowRationale) {
                         showDialog = !showDialog
                         return@HomeToggleScreenCaptureButton
                     }
                     allPermissions.launchMultiplePermissionRequest()
                 }
             },
             content = { innerPadding ->
                 Box(
                     modifier = Modifier
                         .fillMaxSize()
                         .padding(innerPadding)
                 ) {
                     if (showDialog) {
                         HomeReadAndWritePermissionExplainer(
                             context = context,
                             onDismiss = {
                                 showDialog = !showDialog
                             }
                         )
                     }
                 }
             }
         )
     }*/
}