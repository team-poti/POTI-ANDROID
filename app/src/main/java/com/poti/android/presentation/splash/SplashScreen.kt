package com.poti.android.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.poti.android.R
import com.poti.android.core.designsystem.theme.PotiTheme

@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
) {
    SplashScreen(modifier = modifier)
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PotiTheme.colors.poti600),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(126.dp),
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    PotiTheme {
        SplashScreen()
    }
}
