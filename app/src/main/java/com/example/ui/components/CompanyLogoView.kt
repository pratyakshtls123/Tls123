package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.TextWhite
import java.io.File

@Composable
fun CompanyLogoView(
    logo: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    shape: Shape = CircleShape
) {
    val trimmed = logo.trim()
    val isImageUri = trimmed.startsWith("http://") ||
            trimmed.startsWith("https://") ||
            trimmed.startsWith("content://") ||
            trimmed.startsWith("file://") ||
            (trimmed.startsWith("/") && (trimmed.endsWith(".png") || trimmed.endsWith(".jpg") || trimmed.endsWith(".jpeg") || trimmed.endsWith(".webp") || trimmed.contains("logos")))

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(SpaceCardElevated)
            .border(1.dp, SpaceBorder, shape),
        contentAlignment = Alignment.Center
    ) {
        if (isImageUri) {
            val model = if (trimmed.startsWith("/")) File(trimmed) else trimmed
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model)
                    .crossfade(true)
                    .build(),
                contentDescription = "Company Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (trimmed.isNotEmpty()) {
            Text(
                text = trimmed.take(2),
                fontSize = (size.value * 0.55).sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        } else {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = "Company",
                tint = CosmicViolet,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}
