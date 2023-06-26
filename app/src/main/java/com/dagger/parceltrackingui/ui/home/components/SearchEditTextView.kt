package com.dagger.parceltrackingui.ui.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchEditText(
    onSearchClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToDetail: (
        parcelId: String,
        parcelType: String,
        enableChat: Boolean
    ) -> Unit
) {
    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    var trackingNumber by rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    LaunchedEffect(key1 = isFocused) {
        if (isFocused) {
            delay(600)
            focusRequester.requestFocus()
        }
    }

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
                placeholder = { Text("Tracking number", color = Color.White) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                    disabledIndicatorColor = Color.Transparent,
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(corner = CornerSize(32.dp)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isFocused = true
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
                    value = trackingNumber,
                    onValueChange = {
                        trackingNumber = it
                    },
                    placeholder = { Text("Tracking number") },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        disabledIndicatorColor = Color.Transparent,
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(32.dp)),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            localFocusManager.clearFocus()
                            onNavigateToDetail.invoke(
                                trackingNumber.ifEmpty { "882734" },
                                "goods",
                                true
                            )
                        },
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .zIndex(0.9f)
                )
            }
            LargeIconButton(
                onClick = { onSearchClicked("test") },
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}