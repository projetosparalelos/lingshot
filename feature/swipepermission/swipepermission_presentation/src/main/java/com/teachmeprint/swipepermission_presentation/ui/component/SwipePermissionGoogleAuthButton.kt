package com.teachmeprint.swipepermission_presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teachmeprint.swipepermission_presentation.R

@Composable
fun SwipePermissionGoogleAuthButton(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onSignIn,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_google),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Sign in with Google")
    }
}

@Preview(showBackground = true)
@Composable
fun SwipePermissionGoogleAuthButtonPreview() {
    SwipePermissionGoogleAuthButton {}
}