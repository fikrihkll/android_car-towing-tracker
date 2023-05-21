package com.dagger.parceltrackingui.ui.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.TextFieldDecorationBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dagger.parceltrackingui.R
import com.dagger.parceltrackingui.ui.home.components.LargeIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by rememberSaveable {
        mutableStateOf("All")
    }

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)

        ) {
            item {
                BigAppBar(
                    onSearchClicked = {
                        selectedTab = if (selectedTab == "All") "Sent Packages" else "All"
                    }
                )
            }
            item {
                Column {
                    HomeParcelListTabBar(
                        selectedTab,
                        listOf("All", "Sent Packages", "Delivering"),
                        onTabClick = {
                            selectedTab = it
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeParcelListTabBar(
    selectedTab: String,
    tabs: List<String>,
    onTabClick: (String) -> Unit,
) {
    LazyRow(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchEditText(
    onSearchClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(CornerSize(32.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Your parcel ID") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(corner = CornerSize(32.dp)),
                modifier = Modifier
                    .weight(0.5f)
            )
            LargeIconButton(
                onClick = { onSearchClicked("test") },
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

@Preview(showBackground = true,)
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
//@Composable
//fun HomeScreenPreview() {
//    ParcelTrackingUITheme {
//        HomeScreen()
//    }
//}