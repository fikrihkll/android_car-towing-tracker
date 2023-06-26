package com.dagger.parceltrackingui.ui.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dagger.parceltrackingui.R
import kotlinx.coroutines.launch

@Composable
fun BigAppBar(
    modifier: Modifier = Modifier,
    onSearchClicked: (String) -> Unit,
    onNavigateToDetail: (
        parcelId: String,
        parcelType: String,
        enableChat: Boolean
    ) -> Unit
) {
    val translationYAnimation = remember {
        Animatable(-250f)
    }
    var translationYState by rememberSaveable {
        mutableStateOf(-250f)
    }
    val translationYTarget = 0f

    val alphaAnimation = remember {
        Animatable(0f)
    }
    var alphaState by rememberSaveable {
        mutableStateOf(0f)
    }
    val alphaTarget = 1f

    LaunchedEffect(Unit) {
        launch {
            translationYAnimation.animateTo(
                targetValue = translationYTarget,
                animationSpec = tween(
                    durationMillis = 1300,
                    easing = FastOutSlowInEasing
                )
            )
            translationYState = translationYTarget
        }
        launch {
            alphaAnimation.animateTo(
                targetValue = alphaTarget,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                )
            )
            alphaState = alphaTarget
        }
    }

    Surface(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 32.dp,
            bottomStart = 32.dp
        ),
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .run {
                    if (translationYState == translationYTarget)
                        this.offset(x = 0.dp, y = translationYState.dp)
                    else
                        this.offset(x = 0.dp, y = translationYAnimation.value.dp)
                }
                .run {
                    if (alphaState == alphaTarget)
                        this.alpha(alphaTarget)
                    else
                        this.alpha(alphaAnimation.value)

                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "sidebar button"
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://avatars.githubusercontent.com/u/57880863?v=4")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(
                        id = R.drawable.ic_launcher_background
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .wrapContentSize(),
                    contentDescription = "avatar image",
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Welcome, Miguel",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                "Find your package",
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchEditText(
                onSearchClicked = onSearchClicked,
                onNavigateToDetail = onNavigateToDetail
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}