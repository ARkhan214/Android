package com.emranhss.blooddonation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.emranhss.blooddonation.ui.theme.BloodDonationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloodDonationTheme {
                SplashScreen(
                    onTimeout = {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // ✅ close splash so user can’t go back
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(true) {
        visible = true
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(2000) // show splash for 2 sec
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = visible) {
            Text(
                text = "Blood Donation",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                )
            )
        }
    }
}
