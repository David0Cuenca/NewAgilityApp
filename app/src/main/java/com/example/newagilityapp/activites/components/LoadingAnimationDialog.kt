package com.example.newagilityapp.activites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun LoadingAnimationDialog(onDismiss: () -> Unit) {
    var showSuccessAnimation by remember { mutableStateOf(false) }
    val compositionLoading by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
    val compositionSuccess by rememberLottieComposition(LottieCompositionSpec.Asset("sucess.json"))
    LaunchedEffect(Unit) {
        delay(1000)  // Espera para mostrar la animación de carga
        showSuccessAnimation = true
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!showSuccessAnimation) {
                    LottieAnimation(compositionLoading, modifier = Modifier.size(100.dp))
                } else {
                    Column {
                        LottieAnimation(compositionSuccess, modifier = Modifier.size(100.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onDismiss) {
                            Text(text = "Continuar")
                        }
                    }

                }
            }
        }
}

