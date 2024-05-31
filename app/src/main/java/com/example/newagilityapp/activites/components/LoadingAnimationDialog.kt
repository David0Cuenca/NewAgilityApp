package com.example.newagilityapp.activites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LoadingAnimationDialog(
    isLoading: Boolean,
    isSuccess: Boolean?,
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    if (isLoading || isSuccess != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    when {
                        isLoading -> {
                            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
                            LottieAnimation(
                                composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        isSuccess == true -> {
                            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
                            LottieAnimation(
                                composition,
                                iterations = 1,
                                modifier = Modifier
                                    .size(150.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        isSuccess == false -> {
                            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("error.json"))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                LottieAnimation(
                                    composition,
                                    iterations = 1,
                                    modifier = Modifier.size(150.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = errorMessage ?: "Unknown error", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}
