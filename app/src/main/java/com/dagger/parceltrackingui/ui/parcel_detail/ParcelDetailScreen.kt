package com.dagger.parceltrackingui.ui.parcel_detail

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.splineBasedDecay
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.verticalDrag
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dagger.parceltrackingui.R
import com.dagger.parceltrackingui.ui.components.Shimmer
import com.dagger.parceltrackingui.ui.home.CirclesWithLine
import com.dagger.parceltrackingui.ui.theme.ParcelTrackingUITheme
import com.dagger.parceltrackingui.ui.theme.cAccent
import com.dagger.parceltrackingui.ui.theme.cGray
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ParcelDetailScreen(
    parcelId: String,
    parcelType: String,
    enableChat: Boolean,
    onBackPressed: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val translateYAnimation = remember {
        Animatable(0f)
    }

    val minimumBottomSheetHeight = (LocalConfiguration.current.screenHeightDp) / 2
    LaunchedEffect(key1 = Unit) {
        delay(2000)
        translateYAnimation.animateTo(
            targetValue = minimumBottomSheetHeight.toFloat(),
            animationSpec = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            )
        )
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = translateYAnimation.value.dp,
        sheetShape = if (scaffoldState.bottomSheetState.isExpanded) RectangleShape else RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(16.dp)
            ) {

                item {
                    Spacer(
                        modifier = Modifier
                            .height(16.dp)
                    )
                    Text(
                        text = "Tracking #US $parcelId",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(
                        modifier = Modifier
                            .height(32.dp)
                    )
                    TrackingActivityView()
                    Spacer(modifier = Modifier.height(32.dp))
                    ChatView()
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    Text("Log Tracking", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(16.dp))
                }

                items(List(29) { "$it Jan 2022 - Courier is on its way" } ) {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }

            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MapsView(
                    modifier = Modifier
                        .fillMaxSize()
                )
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
            }
        }
    )
}
@Composable
fun MapsView(
    modifier: Modifier = Modifier
) {
    val requiredPermissionList =  listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var isAllPermissionGranted by rememberSaveable {
        mutableStateOf(false)
    }
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            isAllPermissionGranted = true
        } else {
            // Show dialog
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(37.417924, -122.080706), 14f)
    }

    LaunchedEffect(Unit) {
        launcherMultiplePermissions.launch(requiredPermissionList.toTypedArray())
    }

    PermissionRequired(
        isPermissionGranted = isAllPermissionGranted,
        permissionNotGrantedContent = { Surface(color = Color.LightGray,modifier = modifier) { Text("Permission is not granted") } },
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = modifier,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(compassEnabled = true)
        ) {
            GoogleMarkers()
            Polyline(
                points = listOf(
                    LatLng(37.422917, -122.077981),
                    LatLng(37.420608, -122.078053),
                    LatLng(37.421041, -122.086738),
                )
            )
        }
    }
}

@Composable
fun GoogleMarkers() {
    Marker(
        state = rememberMarkerState(position = LatLng(37.422917, -122.077981)),
        title = "Marker1",
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    )
    Marker(
        state = rememberMarkerState(position = LatLng(37.420608, -122.078053)),
        title = "Marker2",
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
    )
    Marker(
        state = rememberMarkerState(position = LatLng(37.421041, -122.086738),),
        title = "Marker3",
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
    )
}

@Composable
fun PermissionRequired(
    isPermissionGranted: Boolean,
    permissionNotGrantedContent: @Composable () -> Unit,
    permissionGrantedContent: @Composable () -> Unit,
) {
    if (isPermissionGranted)
        permissionGrantedContent()
    else
        permissionNotGrantedContent()
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

@Composable
fun ChatView(modifier: Modifier = Modifier) {
    Column {
        Surface(
            shape = RoundedCornerShape(corner = CornerSize(32.dp)),
            color = MaterialTheme.colorScheme.onBackground,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
//                Image(painter = painterResource(id = R.drawable.round_chat_24), contentDescription = "Chat Icon")
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
                        .size(48.dp)
                        .wrapContentSize(),
                    contentDescription = "avatar image",
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row {
                        Text(
                            "Jacelyn Montgomery",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                    Row {
                        Text(
                            "Delivery CS",
                            style = MaterialTheme.typography.labelSmall,
                            color = cGray,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                ChatButton()
            }
        }
    }
}

@Composable
fun ChatButton() {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
            .clickable(
                onClick = { },
                enabled = true,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 32.dp)
            ),
    ) {
        Icon(painterResource(id = R.drawable.round_chat_24), contentDescription = "", modifier = Modifier.wrapContentSize(), tint = MaterialTheme.colorScheme.onBackground)
    }
}

private fun Modifier.swipeToExpand(
    screenHeight: Dp,
    onExpanded: () -> Unit
): Modifier = composed {
    // This Animatable stores the vertical offset for the element.
    val offsetY = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation.
                offsetY.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    verticalDrag(pointerId) { change ->
                        // Record the position after offset
                        val verticalDragOffset = offsetY.value + change.positionChange().y
                        launch {
                            // Overwrite the Animatable value while the element is dragged.
                            offsetY.snapTo(verticalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().y
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetY = decay.calculateTargetValue(offsetY.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetY.updateBounds(
                    lowerBound = -screenHeight.toPx(),
                    upperBound = 0f
                )
                launch {
                    if (targetOffsetY.absoluteValue <= screenHeight.toPx()) {
                        // Not enough velocity; Slide back to the default position.
                        offsetY.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the top.
                        offsetY.animateTo(targetValue = -screenHeight.toPx(), initialVelocity = velocity)
                        // The element is fully expanded.
                        onExpanded()
                    }
                }
            }
        }
    }
        // Apply the vertical offset to the element.
        .offset { IntOffset(0, offsetY.value.roundToInt()) }
}
