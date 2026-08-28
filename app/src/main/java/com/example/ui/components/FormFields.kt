package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.util.FormatUtils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CosmicInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isNumber: Boolean = false,
    isMultiline: Boolean = false,
    isRequired: Boolean = false,
    suggestedChips: List<String> = emptyList(),
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )
            if (isRequired) {
                Text(
                    text = " *",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicViolet
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = placeholder.ifEmpty { "Type $label..." }, color = TextDark, fontSize = 13.sp)
            },
            singleLine = !isMultiline,
            minLines = if (isMultiline) 3 else 1,
            maxLines = if (isMultiline) 5 else 1,
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextLightWrapper,
                focusedContainerColor = SpaceCard,
                unfocusedContainerColor = SpaceCard,
                focusedBorderColor = CosmicViolet,
                unfocusedBorderColor = SpaceBorder,
                cursorColor = CosmicViolet
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )

        // If chips provided, render quick suggestion tags that fill the text field when tapped
        if (suggestedChips.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                suggestedChips.forEach { suggestion ->
                    val color = FormatUtils.getDynamicStatusColor(suggestion)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (value.equals(suggestion, ignoreCase = true)) color.copy(alpha = 0.25f) else SpaceCardElevated)
                            .border(1.dp, if (value.equals(suggestion, ignoreCase = true)) color else SpaceBorder, RoundedCornerShape(6.dp))
                            .clickable { onValueChange(suggestion) }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = suggestion,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (value.equals(suggestion, ignoreCase = true)) color else TextDim
                        )
                    }
                }
            }
        }
    }
}

private val TextLightWrapper = Color(0xFFE2E8F0)
