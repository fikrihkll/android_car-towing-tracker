package com.dagger.parceltrackingui.ui.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dagger.parceltrackingui.ui.components.Shimmer
import com.dagger.parceltrackingui.ui.home.CirclesWithLine
import com.dagger.parceltrackingui.ui.theme.cGreen
import kotlinx.coroutines.launch

@Composable
fun DeliveryCardSection(
    isLoading: Boolean,
    listData: List<Pair<String, String>>,
    onTabSelected: (String) -> Unit
) {
    var selectedTab by rememberSaveable {
        mutableStateOf("Current")
    }
    Column {
        HomeParcelListTabBar(
            selectedTab,
            listOf("Current", "History", "All"),
            onTabClick = {
                selectedTab = it
                onTabSelected.invoke(it)
            }
        )
        DeliveringPackageTrackingCardList(
            listData = listData,
            isLoading = isLoading
        )
    }
}

@Composable
fun HomeParcelListTabBar(
    selectedTab: String,
    tabs: List<String>,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(tabs) {
            ParcelListTab(selectedTab = selectedTab, tab = it, onTabClick = onTabClick)
        }
    }
}

@Composable
fun DeliveringPackageTrackingCardList(listData: List<Pair<String, String>>, isLoading: Boolean) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (isLoading) {
            items(count = 5) {
                DeliveringPackageTrackingCard(isLoading = true, data = Pair("", ""), index = 0)
            }
        }
       itemsIndexed(listData) { index, data ->
           DeliveringPackageTrackingCard(isLoading = false, data = data, index = index)
       }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DeliveringPackageTrackingCard(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    data: Pair<String, String>,
    index: Int,
) {
    val translateXAnimation = remember {
        Animatable(150f)
    }
    var translateXState by rememberSaveable {
        mutableStateOf(150f)
    }
    val translateXTarget = 0f

    val alphaAnimation = remember {
        Animatable(0f)
    }
    var alphaState by rememberSaveable {
        mutableStateOf(0f)
    }
    val alphaTarget = 1f

    LaunchedEffect(key1 = Unit) {
        launch {
            translateXAnimation.animateTo(
                translateXTarget,
                animationSpec = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 400
                )
            )
            translateXState = translateXTarget
        }
        launch {
            alphaAnimation.animateTo(
                1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 400
                )
            )
            alphaState = alphaTarget
        }
    }

    val cardWidth = LocalConfiguration.current.screenWidthDp - 32
    Surface(
        modifier = modifier
            .width(cardWidth.dp)
            .padding(horizontal = 16.dp)
            .run {
                if (isLoading) {
                    if (alphaState == alphaTarget)
                        this.alpha(alphaState)
                    else
                        this.alpha(alphaAnimation.value)
                } else {
                    this.alpha(alphaTarget)
                }
            }
            .run {
                if (isLoading) {
                    if (translateXState == translateXTarget)
                        this.offset(x = translateXState.dp, y = 0.dp)
                    else
                        this.offset(x = translateXAnimation.value.dp, y = 0.dp)
                } else {
                    this.offset(x = translateXTarget.dp, y = 0.dp)
                }
            },
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(size = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "On The Way",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "$76",
                    style = MaterialTheme.typography.titleSmall,
                    color = cGreen
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                        AnimatedContent(targetState = !isLoading) {
                            if (it) {
                                Text(
                                    data.first,
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
                            !isLoading,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(550, delayMillis = 90)) +
                                        scaleIn(initialScale = 0.92f, animationSpec = tween(320, delayMillis = 90)) with
                                        fadeOut(animationSpec = tween(90))
                            }
                        ) {
                            if (it) {
                                Text(
                                    data.second,
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
    }
}

@Composable
fun ParcelListTab(
    onTabClick: (String) -> Unit,
    selectedTab: String,
    tab: String,
) {
    val scaleTransition = updateTransition(
        selectedTab,
        label = "Tab Indicator"
    )
    val scale by scaleTransition.animateFloat(
        label = "scale animation",
        targetValueByState = { value ->
            if (value == tab) {
                1.2f
            } else {
                1f
            }
        }
    )
    val padding by scaleTransition.animateDp(
        label = "extra padding",
        targetValueByState = { value ->
            if (value == tab) {
                12.dp
            } else {
                0.dp
            }
        }
    )

    val isSelected = selectedTab == tab

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                enabled = true,
                role = Role.Tab,
                onClick = {
                    onTabClick(tab)
                }
            )
            .padding(horizontal = padding)
            .scale(scale)
    ) {
        Text(
            tab,
            color = if (isSelected) Color.Black else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
                .padding(2.dp)
        )
    }
}