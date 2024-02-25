package com.example.neuzapp.presentation.news_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp

@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    value: String,
    onInputValueChange: (String) -> Unit,
    onClosedIconClicked: () -> Unit,
    onSearchedIconClicked: () -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onInputValueChange,
        singleLine = true,
        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = Color.White.copy(alpha = 0.7f)
            )
        },
        placeholder = {
            Text(text = "Search...", color = Color.White.copy(alpha = 0.7f))
        },
        trailingIcon = {
            IconButton(onClick = {
                // what we wanna do is that when we click this icon then whatever in
                // the field is just disappear.  So

                // if value is not empty means there is some value so we are clearing this value
                if (value.isNotEmpty()) onInputValueChange("")
                else onClosedIconClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Icon",
                    tint = Color.White
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchedIconClicked() }
        ),

        // this used for changing the color of SearchAppBar
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = Color.White,
            focusedIndicatorColor = Color.White
        )
    )
}