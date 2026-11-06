package com.fingerspell.tutor.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fingerspell.tutor.R
import com.fingerspell.tutor.core.di.ServiceLocator
import com.fingerspell.tutor.core.theme.Blush
import com.fingerspell.tutor.core.theme.Hairline
import com.fingerspell.tutor.core.theme.SplashOverlay
import com.fingerspell.tutor.core.theme.Teal
import com.fingerspell.tutor.core.theme.skinTone
import com.fingerspell.tutor.core.ui.AppBackdrop
import com.fingerspell.tutor.core.ui.HandshapeCanvas
import com.fingerspell.tutor.domain.model.NeutralHandshape

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
fun SplashScreen() {
    val transition = rememberInfiniteTransition(label = "splash")
    val wave by transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
    )
    val spin by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    AppBackdrop(overlay = SplashOverlay) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(spin)
        ) {
            val radius = size.minDimension * 0.34f
            val focus = Offset(size.width / 2f, size.height * 0.38f)
            listOf(0.72f, 1f, 1.28f).forEachIndexed { index, factor ->
                drawArc(
                    color = Teal.copy(alpha = 0.06f + index * 0.01f),
                    startAngle = 25f + index * 42f,
                    sweepAngle = 190f,
                    useCenter = false,
                    topLeft = Offset(focus.x - radius * factor, focus.y - radius * factor),
                    size = Size(radius * factor * 2f, radius * factor * 2f),
                    style = Stroke(width = 2.5f)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(pulse)
                    .background(Color(0x1AFFFFFF), CircleShape)
                    .border(1.dp, Color(0x33F7D6CC), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(140.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            HandshapeCanvas(
                spec = NeutralHandshape,
                size = 96.dp,
                tone = skinTone(1),
                description = stringResource(R.string.cd_waving_hand),
                rotationDeg = wave
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.splash_title_one),
                color = Blush,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.splash_title_two),
                color = Hairline,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 8.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                color = Hairline.copy(alpha = 0.72f),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(34.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .width(148.dp)
                    .height(4.dp),
                color = Teal,
                trackColor = Color(0x33FFF4F0)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                color = Hairline.copy(alpha = 0.6f),
                fontSize = 10.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 3.sp
            )
        }
    }
}
