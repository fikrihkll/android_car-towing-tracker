package com.dagger.parceltrackingui.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dagger.parceltrackingui.R
import com.dagger.parceltrackingui.entity.ServiceEntity
import com.dagger.parceltrackingui.ui.components.Shimmer
import com.dagger.parceltrackingui.ui.home.components.BigAppBar
import com.dagger.parceltrackingui.ui.home.components.DeliveryCardSection
import com.dagger.parceltrackingui.ui.home.components.LargeIconButton
import com.dagger.parceltrackingui.ui.home.components.TOP_LEVEL_DESTINATIONS
import com.dagger.parceltrackingui.ui.home.viewmodels.HomeDeliveryListState
import com.dagger.parceltrackingui.ui.home.viewmodels.HomeDeliveryListViewModel
import com.dagger.parceltrackingui.ui.theme.ParcelTrackingUITheme
import com.dagger.parceltrackingui.ui.theme.cAccent
import com.dagger.parceltrackingui.ui.theme.cBrownLightPastel
import com.dagger.parceltrackingui.ui.theme.cMintGreenPastel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (
        parcelId: String,
        parcelType: String,
        enableChat: Boolean
    ) -> Unit,
    viewModel: HomeDeliveryListViewModel = viewModel()
) {

    val homeDeliveryListState by viewModel.homeDeliveryListDataState.collectAsState()
    var selectedDestination by rememberSaveable {
        mutableStateOf(
            TOP_LEVEL_DESTINATIONS.first().route
        )
    }
    LaunchedEffect(key1 = Unit) {
        if (homeDeliveryListState is HomeDeliveryListState.HomeDeliveryInitial) {
            viewModel.loadHomeDeliveryList("Current")
        }
    }

    Scaffold(
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding),
            ) {
                item {
                    BigAppBar(
                        onSearchClicked = {

                        },
                        onNavigateToDetail = onNavigateToDetail
                    )
                }
                item {
                    DeliveryCardSection(
                        isLoading = homeDeliveryListState is HomeDeliveryListState.HomeDeliveryLoading,
                        listData = (homeDeliveryListState as? HomeDeliveryListState.HomeDeliveryLoaded)?.listData
                            ?: emptyList()
                    ) {
                        viewModel.loadHomeDeliveryList(it)
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(8.dp)
                        )
                        Text(
                            text = "Services",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                        ServicesList(
                            servicesList = listOf(
                                ServiceEntity(
                                    icon = "📦",
                                    name = "Cost Estimation",
                                    color = cBrownLightPastel
                                ),
                                ServiceEntity(
                                    icon = "✍️",
                                    name = "Sign The Contract",
                                    color = cBrownLightPastel
                                ),
                                ServiceEntity(
                                    icon = "✈️",
                                    name = "Deliver",
                                    color = cBrownLightPastel
                                )
                            )
                        )
                    }
                }
            }
        }, bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,

            ) {
                TOP_LEVEL_DESTINATIONS.forEach { parcelDestination ->
                    NavigationBarItem(
                        selected = selectedDestination == parcelDestination.route,
                        onClick = { selectedDestination = parcelDestination.route },
                        icon = {
                            Icon(
                                imageVector = parcelDestination.selectedIcon,
                                contentDescription = parcelDestination.iconText
                            )
                        }
                    )
                }
            }
        },
    )
}

@Composable
fun CirclesWithLine() {
    val primaryColor: Color = MaterialTheme.colorScheme.primary
    val tertiaryColor: Color = cAccent
    val textHeight = convertDpToPx(dp = 24.dp)
    val elementHeight = 120.dp
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(elementHeight)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val radius = 20f
            val circle1Center = Offset(size.width / 2, radius + 14f)
            val circle2Center = Offset(size.width / 2, (size.height - (radius/2) - textHeight))

            // Draw the line connecting the circles
            val lineWidth = 8f
            val gapWidth = 4f
            val lineStartY = circle1Center.y
            val lineEndY = circle2Center.y
            val lineAmount = ((lineEndY - lineStartY)/(lineWidth + gapWidth)).toInt()

            for (i in 0 until lineAmount) {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(circle1Center.x, lineStartY + (i * (lineWidth + gapWidth))),
                    end = Offset(circle2Center.x, lineStartY + (i * (lineWidth + gapWidth) + lineWidth)),
                    strokeWidth = 5f
                )
                drawLine(
                    color = Color.Transparent,
                    start = Offset(circle1Center.x, lineStartY + (i * (lineWidth + gapWidth) + lineWidth)),
                    end = Offset(circle2Center.x, lineStartY + (i * (lineWidth + gapWidth) + lineWidth + gapWidth)),
                    strokeWidth = 5f
                )
            }

            // Draw the first circle
            drawCircle(
                color = primaryColor,
                radius = radius,
                center = circle1Center
            )

            // Draw the second circle
            drawCircle(
                color = tertiaryColor,
                radius = radius,
                center = circle2Center
            )
        }
    }
}

@Composable
fun convertDpToPx(dp: Dp): Float {
    val density = LocalDensity.current.density
    return (dp * density).value
}

@Composable
fun ServicesList(
    servicesList: List<ServiceEntity>
) {
    val listColor = mutableListOf(cBrownLightPastel, MaterialTheme.colorScheme.primaryContainer, cMintGreenPastel)
    servicesList.forEach { entity ->
        entity.color = listColor.first()
        listColor.removeFirst()
    }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(servicesList) { index, entity ->
            ServiceCard(entity = entity, index = index)
        }
    }
}

@Composable
fun ServiceCard(
    modifier: Modifier = Modifier,
    entity: ServiceEntity,
    index: Int
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
        delay(600)
        launch {
            translateXAnimation.animateTo(
                translateXTarget,
                animationSpec = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 200
                )
            )
            translateXState = translateXTarget
        }
        launch {
            alphaAnimation.animateTo(
                alphaTarget,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 200
                )
            )
            alphaState = alphaTarget
        }
    }

    Surface(
        modifier = modifier
            .run {
                if (translateXState == translateXTarget)
                    this.offset(x = translateXTarget.dp)
                else
                    this.offset(x = translateXAnimation.value.dp)
            }
            .run {
                if (alphaState == alphaTarget)
                    this.alpha(alphaState)
                else
                    this.alpha(alphaAnimation.value)
            },
        color = entity.color,
        shape = RoundedCornerShape(size = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(entity.icon)
            Text(
                text = entity.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

//@Preview()
@Composable
fun HomeScreenPreview() {
    ParcelTrackingUITheme {
        HomeScreen(onNavigateToDetail = { parcelId, parcelType, enableChat ->  })
    }
}