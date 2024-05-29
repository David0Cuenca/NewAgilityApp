package com.example.newagilityapp.activites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun LoadingAnimationDialog(
    isLoading: Boolean,
    isSuccess: Boolean?,
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    if (isLoading || isSuccess != null) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    DotLottieAnimation(
                        source = DotLottieSource.Asset("loading.json"),
                        autoplay = true,
                        loop = true,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (isSuccess == true) {
                    DotLottieAnimation(
                        source = DotLottieSource.Asset("success.json"),
                        autoplay = true,
                        loop = false,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (isSuccess == false) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        DotLottieAnimation(
                            source = DotLottieSource.Asset("error.json"),
                            autoplay = true,
                            loop = false
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage ?: "Unknown error", color = Color.Red)
                    }
                }
            }
        }
    }
}
