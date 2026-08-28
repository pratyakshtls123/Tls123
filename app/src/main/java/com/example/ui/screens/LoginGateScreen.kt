package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicPink
import com.example.ui.theme.CosmicRose
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.AuthResult
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LoginGateScreen(
    onAuthenticate: (loginId: String, password: String) -> AuthResult,
    modifier: Modifier = Modifier
) {
    // Tab: 0 = Company Portal, 1 = Master Admin
    var selectedTab by remember { mutableIntStateOf(0) }

    var companyIdInput by remember { mutableStateOf("") }
    var companyPasswordInput by remember { mutableStateOf("") }

    var masterPasswordInput by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val shakeOffset = remember { Animatable(0f) }

    fun triggerShake() {
        coroutineScope.launch {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    -20f at 50
                    20f at 100
                    -15f at 150
                    15f at 200
                    -8f at 250
                    8f at 300
                    0f at 400
                }
            )
        }
    }

    fun submitLogin() {
        errorMessage = null
        val result = if (selectedTab == 0) {
            if (companyPasswordInput.isBlank()) {
                errorMessage = "Please enter company password."
                triggerShake()
                return
            }
            onAuthenticate(companyIdInput.trim(), companyPasswordInput.trim())
        } else {
            if (masterPasswordInput.isBlank()) {
                errorMessage = "Please enter master admin password."
                triggerShake()
                return
            }
            onAuthenticate("", masterPasswordInput.trim())
        }

        when (result) {
            is AuthResult.Success -> {
                successMessage = result.message
            }
            is AuthResult.Failure -> {
                errorMessage = result.message
                triggerShake()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack),
        contentAlignment = Alignment.Center
    ) {
        // Ambient Cosmic Lights
        Box(
            modifier = Modifier
                .offset(x = 120.dp, y = (-180).dp)
                .size(320.dp)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(listOf(CosmicViolet.copy(alpha = 0.25f), Color.Transparent)),
                    CircleShape
                )
        )
        Box(
            modifier = Modifier
                .offset(x = (-120).dp, y = 180.dp)
                .size(320.dp)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(listOf(CosmicPink.copy(alpha = 0.15f), Color.Transparent)),
                    CircleShape
                )
        )

        // Login Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                .clip(RoundedCornerShape(24.dp))
                .background(SpaceCard)
                .border(1.dp, SpaceBorder, RoundedCornerShape(24.dp))
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // App Branding Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(CosmicViolet, CosmicIndigo)))
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Matrix Brand",
                    tint = TextWhite,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "StarLink TLS123",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                letterSpacing = 0.5.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(CosmicCyan, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "UNIVERSE MATRIX PORTAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CosmicIndigo,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Role Selector Tab Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SpaceCardElevated)
                    .border(1.dp, SpaceBorder, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                // Company Portal Tab
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(9.dp))
                        .background(if (selectedTab == 0) CosmicViolet else Color.Transparent)
                        .clickable {
                            selectedTab = 0
                            errorMessage = null
                        }
                        .padding(vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Company",
                            tint = if (selectedTab == 0) TextWhite else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Company Login",
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 0) TextWhite else TextMuted
                        )
                    }
                }

                // Master Admin Tab
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(9.dp))
                        .background(if (selectedTab == 1) CosmicIndigo else Color.Transparent)
                        .clickable {
                            selectedTab = 1
                            errorMessage = null
                        }
                        .padding(vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Master",
                            tint = if (selectedTab == 1) TextWhite else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Master Admin",
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 1) TextWhite else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (selectedTab == 0) {
                // Company Portal Inputs
                Text(
                    text = "Enter your designated Company Login ID and Password to access your company's operations.",
                    fontSize = 12.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Company ID Field
                OutlinedTextField(
                    value = companyIdInput,
                    onValueChange = {
                        companyIdInput = it
                        errorMessage = null
                    },
                    placeholder = {
                        Text("Company Login ID (e.g. COM0001)", color = TextDark, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Badge,
                            contentDescription = "Badge",
                            tint = CosmicCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SpaceBlack,
                        unfocusedContainerColor = SpaceBlack,
                        focusedBorderColor = CosmicViolet,
                        unfocusedBorderColor = SpaceBorder,
                        cursorColor = CosmicViolet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("company_login_id_field")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Company Password Field
                OutlinedTextField(
                    value = companyPasswordInput,
                    onValueChange = {
                        companyPasswordInput = it
                        errorMessage = null
                    },
                    placeholder = {
                        Text("Company Password", color = TextDark, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = TextDim,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                                tint = CosmicIndigo,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { submitLogin() }),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SpaceBlack,
                        unfocusedContainerColor = SpaceBlack,
                        focusedBorderColor = if (errorMessage != null) CosmicRose else CosmicViolet,
                        unfocusedBorderColor = if (errorMessage != null) CosmicRose else SpaceBorder,
                        cursorColor = CosmicViolet
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("company_password_field")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick test credentials chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceCardElevated)
                        .clickable {
                            companyIdInput = "COM0001"
                            companyPasswordInput = "Neelanjali@123"
                            errorMessage = null
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Hint",
                        tint = CosmicCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Demo: COM0001 / Neelanjali@123 (Tap to autofill)",
                        fontSize = 11.sp,
                        color = CosmicCyan
                    )
                }

            } else {
                // Master Admin Inputs
                Text(
                    text = "Master Administrator access allows full multi-company management, cross-tenant switching, and profile registration.",
                    fontSize = 12.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Master Password Field
                OutlinedTextField(
                    value = masterPasswordInput,
                    onValueChange = {
                        masterPasswordInput = it
                        errorMessage = null
                    },
                    placeholder = {
                        Text("Master Admin Password", color = TextDark, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = CosmicIndigo,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                                tint = CosmicIndigo,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { submitLogin() }),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SpaceBlack,
                        unfocusedContainerColor = SpaceBlack,
                        focusedBorderColor = if (errorMessage != null) CosmicRose else CosmicIndigo,
                        unfocusedBorderColor = if (errorMessage != null) CosmicRose else SpaceBorder,
                        cursorColor = CosmicIndigo
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("master_password_field")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Master hint chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceCardElevated)
                        .clickable {
                            masterPasswordInput = "Hansooyoung70010"
                            errorMessage = null
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Hint",
                        tint = CosmicIndigo,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Master Pass: Hansooyoung70010 (Tap to autofill)",
                        fontSize = 11.sp,
                        color = CosmicIndigo
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage ?: "",
                    color = CosmicRose,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = { submitLogin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == 0) CosmicViolet else CosmicIndigo
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_submit_button")
            ) {
                Text(
                    text = if (selectedTab == 0) "Access Company Portal" else "Unlock Master Matrix",
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Isolated tenant sandboxing. Sensitive data protected.",
                fontSize = 10.sp,
                color = TextDark,
                textAlign = TextAlign.Center
            )
        }
    }
}
