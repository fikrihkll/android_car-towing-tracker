package com.dagger.parceltrackingui.ui.parcel_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dagger.parceltrackingui.ui.components.Shimmer
import com.dagger.parceltrackingui.ui.home.CirclesWithLine
import com.dagger.parceltrackingui.ui.theme.ParcelTrackingUITheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelDetailScreen(
    parcelId: String,
    parcelType: String,
    enableChat: Boolean,
    onBackPressed: () -> Unit
) {
    val translateYAnimation = remember {
        Animatable(999f)
    }

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        translateYAnimation.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            )
        )
    }

    val persistentViewHeight = LocalConfiguration.current.screenHeightDp.dp/2
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Surface(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                
            }
            SmallFloatingActionButton(
                onClick = { onBackPressed.invoke() },
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier
                    .padding(PaddingValues(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = ""
                )
            }
            Surface(
                modifier = Modifier
                    .height(persistentViewHeight)
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
                    .offset(y = translateYAnimation.value.dp),
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(32.dp)
                    )
                    Text(
                        text = "Tracking",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(
                        modifier = Modifier
                            .height(32.dp)
                    )
                    TrackingActivityView()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TrackingActivityView() {
    var isValueLoaded by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        delay(4500)
        isValueLoaded = true
    }

    Row(
        modifier = Modifier
            .height(110.dp)
    ) {
        CirclesWithLine()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "From",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                AnimatedContent(targetState = isValueLoaded) {
                    if (it) {
                        Text(
                            "California",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Shimmer.Rectangle()
                    }
                }
            }
            Column {
                Text(
                    "To",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall
                )
                AnimatedContent(
                    isValueLoaded,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(550, delayMillis = 90)) +
                                scaleIn(initialScale = 0.92f, animationSpec = tween(320, delayMillis = 90)) with
                                fadeOut(animationSpec = tween(90))
                    }
                ) {
                    if (it) {
                        Text(
                            "Boston, CA",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Shimmer.Rectangle()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParcelTrackingUITheme {
        ParcelDetailScreen("", "", false, onBackPressed = {})
    }
}