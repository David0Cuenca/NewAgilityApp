package com.example.newagilityapp.activites.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty

@Composable
fun Alert(
    isOpen:Boolean,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonsClick: () -> Unit
){
    val warning by rememberLottieComposition(LottieCompositionSpec.Asset("Warning.json"))
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            value = MaterialTheme.colorScheme.primary.toArgb(),
            keyPath = arrayOf("**")
        )
    )
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Column(Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(100.dp),
                        composition = warning,
                        dynamicProperties = dynamicProperties,
                        iterations = 1,
                        clipSpec = LottieClipSpec.Progress(0f, 1f),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                    Text(text = title)
                }
            },
            text = { Text(text = text) },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancelar")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonsClick,
                ) {
                    Text(text = "Aceptar")
                }
            },
        )
    }
}