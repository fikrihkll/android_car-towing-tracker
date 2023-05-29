package com.dagger.parceltrackingui.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dagger.parceltrackingui.R
import com.dagger.parceltrackingui.entity.ServiceEntity
import com.dagger.parceltrackingui.ui.home.components.LargeIconButton
import com.dagger.parceltrackingui.ui.home.components.TOP_LEVEL_DESTINATIONS
import com.dagger.parceltrackingui.ui.theme.ParcelTrackingUITheme
import com.dagger.parceltrackingui.ui.theme.cBrownLightPastel
import com.dagger.parceltrackingui.ui.theme.cGreen
import com.dagger.parceltrackingui.ui.theme.cLightGrayPastel
import com.dagger.parceltrackingui.ui.theme.cMintGreenPastel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by rememberSaveable {
        mutableStateOf("Current")
    }
    var selectedDestination by rememberSaveable {
        mutableStateOf(
            TOP_LEVEL_DESTINATIONS.first().route
        )
    }

    Scaffold(
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)

            ) {
                item {
                    BigAppBar(
                        onSearchClicked = {

                        }
                    )
                }
                item {
                    Column {
                        HomeParcelListTabBar(
                            selectedTab,
                            listOf("Current", "History", "All"),
                            onTabClick = {
                                selectedTab = it
                            }
                        )
                        DeliveringPackageTrackingCard()
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
                                    icon = "🫱🏻",
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

@Composable
fun BigAppBar(modifier: Modifier = Modifier, onSearchClicked: (String) -> Unit,) {
    val translationYAnimation = remember {
        Animatable(-250f)
    }
    val alphaAnimation = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        launch {
            translationYAnimation.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1300,
                    easing = FastOutSlowInEasing
                )
            )
        }
        launch {
            alphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                )
            )
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
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .offset(x = 0.dp, y = translationYAnimation.value.dp)
                .alpha(alphaAnimation.value)
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
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchEditText(onSearchClicked = onSearchClicked)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SearchEditText(
    onSearchClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val xOffset by animateDpAsState(
        targetValue = if (isFocused) 0.dp else 9999.dp
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(CornerSize(32.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = "",
                onValueChange = {},
                enabled = false,
                placeholder = { Text("Tracking number") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.Gray
                ),
                shape = RoundedCornerShape(corner = CornerSize(32.dp)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isFocused = !isFocused
                    }
            )
            AnimatedVisibility(
                modifier = Modifier,
                visible = isFocused,
                enter = slideInHorizontally(
                    initialOffsetX = { 9999 },
                    animationSpec = tween(durationMillis = 500)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { 0 },
                    animationSpec = tween(durationMillis = 500)
                ),
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Tracking number") },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(32.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(0.9f)
                        .clickable {
                            isFocused = !isFocused
                        }
                )
            }
            LargeIconButton(
                onClick = { onSearchClicked("test") },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }
    }
}

@Composable
fun DeliveringPackageTrackingCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
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
                        Text(
                            "California",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Column {
                        Text(
                            "To",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            "Boston, CA",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CirclesWithLine() {
    val primaryColor: Color = MaterialTheme.colorScheme.primary
    val secondaryColor: Color = MaterialTheme.colorScheme.secondary
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
                color = secondaryColor,
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
    val listColor = mutableListOf(cBrownLightPastel, cLightGrayPastel, cMintGreenPastel)
    servicesList.forEach { entity ->
        entity.color = listColor.first()
        listColor.removeFirst()
    }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(servicesList) { entity ->
            ServiceCard(entity = entity)
        }
    }
}

@Composable
fun ServiceCard(
    modifier: Modifier = Modifier,
    entity: ServiceEntity
) {
    Surface(
        modifier = modifier,
        color = cBrownLightPastel,
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

@Preview
@Composable
fun SearchBarPreview() {
    SearchEditText(onSearchClicked = {})
}

//@Preview(showBackground = true,)
@Composable
fun HomeParcelListPreview() {
    Column {
        HomeParcelListTabBar(
            "All",
            listOf("All", "Sent Packages", "Delivering"),
            onTabClick = {

            }
        )
    }
}

//@Preview()
@Composable
fun HomeScreenPreview() {
    ParcelTrackingUITheme {
        HomeScreen()
    }
}