package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions


enum class SearchBarVariant {
    DEFAULT,
    FOCUSED,
    WITH_TEXT
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search_hint_default),
    variant: SearchBarVariant = SearchBarVariant.DEFAULT,
    onValueChange: (String) -> Unit = {},
    onClear: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    val borderWidth = when (variant) {
        SearchBarVariant.DEFAULT -> Dimensions.Tiny
        SearchBarVariant.FOCUSED, SearchBarVariant.WITH_TEXT -> Dimensions.Small
    }

    val borderColor = when (variant) {
        SearchBarVariant.DEFAULT -> MaterialTheme.colorScheme.secondary
        SearchBarVariant.FOCUSED, SearchBarVariant.WITH_TEXT -> MaterialTheme.colorScheme.primary
    }

    BasicTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onValueChange(newText)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(Dimensions.HeightBar)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(Dimensions.CornerRadiusSmall)
            )
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(Dimensions.CornerRadiusSmall)
            ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(
                    start = Dimensions.PaddingMedium,
                    top = if (variant == SearchBarVariant.WITH_TEXT) Dimensions.PaddingSmallMed else Dimensions.PaddingMedium,
                    end = Dimensions.PaddingMedium,
                    bottom = if (variant == SearchBarVariant.WITH_TEXT) Dimensions.PaddingSmallMed else Dimensions.PaddingMedium
                ),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacerSmall, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.content_description_search_icon),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(Dimensions.IconSizeMedium)
                )
                
                if (text.isEmpty() && variant == SearchBarVariant.DEFAULT) {
                    Text(
                        text = hint,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    innerTextField()
                }
                
                if (variant == SearchBarVariant.WITH_TEXT) {
                    IconButton(
                        onClick = {
                            text = ""
                            onClear()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = stringResource(R.string.content_description_clear_text),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(Dimensions.IconSizeMedium)
                        )
                    }
                }
            }
        }
    )
} 