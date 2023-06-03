package com.dagger.parceltrackingui.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Shimmer {

    @Composable
    fun Rectangle(
        height: Dp = 14.dp,
        width: Dp = 80.dp,
        fillMaxWidth: Boolean = false,
        cornerSize: CornerSize = CornerSize(8.dp)
    ) {
        val infiniteAnimation = rememberInfiniteTransition()

        val alpha by infiniteAnimation.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    0.7f at 500
                    0.9f at 800
                },
                repeatMode = RepeatMode.Reverse,
            )
        )

        Box(
            modifier = Modifier
                .height(height)
                .alpha(alpha = alpha)
                .clip(RoundedCornerShape(corner = cornerSize))
                .background(Color.Gray)
                .run {
                    if (fillMaxWidth) {
                        this.fillMaxWidth()
                    } else {
                        this.width(width)
                    }
                }
        )
    }

    @Composable
    fun Circle(size: Dp) {
        val infiniteAnimation = rememberInfiniteTransition()

        val alpha by infiniteAnimation.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    0.7f at 500
                    0.9f at 800
                },
                repeatMode = RepeatMode.Reverse,
            )
        )

        Box(
            modifier = Modifier
                .height(size)
                .width(size)
                .alpha(alpha = alpha)
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }



}