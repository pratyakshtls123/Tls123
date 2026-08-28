package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CompanyEntity
import com.example.ui.components.CompanyLogoView
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicPink
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
import kotlin.random.Random

@Composable
fun MasterAccessCredentialsDialog(
    companies: List<CompanyEntity>,
    onDismiss: () -> Unit,
    onSaveCredentials: (companyId: String, newLoginId: String, newPassword: String) -> Unit,
    onEditCompanyProfile: (CompanyEntity) -> Unit,
    onAddNewCompany: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredCompanies = remember(companies, searchQuery) {
        if (searchQuery.isBlank()) companies
        else companies.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.loginId.contains(searchQuery, ignoreCase = true) ||
                    it.ownerName.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CosmicIndigo.copy(alpha = 0.25f))
                                .border(1.dp, CosmicIndigo, RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.VpnKey,
                                contentDescription = "Security",
                                tint = CosmicCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Company Access & Credentials Hub",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CosmicIndigo)
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "MASTER ONLY",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                            Text(
                                text = "Set, update, and manage Login IDs & Passwords for all tenant companies",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search & Add Company Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search by company name or ID...", color = TextDark, fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextDim,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = SpaceBlack,
                            unfocusedContainerColor = SpaceBlack,
                            focusedBorderColor = CosmicViolet,
                            unfocusedBorderColor = SpaceBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = onAddNewCompany,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicEmerald),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "New Company",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of Companies
                if (filteredCompanies.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .padding(vertical = 30.dp)
                    ) {
                        Text(
                            text = "No companies found. Add a new company to get started.",
                            color = TextDim,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                    ) {
                        items(filteredCompanies, key = { it.id }) { company ->
                            CompanyCredentialCard(
                                company = company,
                                onSave = { newLoginId, newPass ->
                                    onSaveCredentials(company.id, newLoginId, newPass)
                                    Toast.makeText(
                                        context,
                                        "Updated credentials for ${company.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onCopy = {
                                    val clipText = """
                                        🏢 Company: ${company.name}
                                        🆔 Login ID: ${company.loginId.ifEmpty { "Not Set" }}
                                        🔑 Access Password: ${company.accessPassword.ifEmpty { "Not Set" }}
                                        🌐 Portal: StarLink TLS123
                                    """.trimIndent()
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Company Credentials", clipText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copied credentials for ${company.name}!", Toast.LENGTH_SHORT).show()
                                },
                                onEditProfile = { onEditCompanyProfile(company) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Total Registered: ${companies.size} Companies",
                        fontSize = 11.sp,
                        color = TextDim
                    )
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Done", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanyCredentialCard(
    company: CompanyEntity,
    onSave: (newLoginId: String, newPassword: String) -> Unit,
    onCopy: () -> Unit,
    onEditProfile: () -> Unit
) {
    var loginId by remember(company.id, company.loginId) { mutableStateOf(company.loginId) }
    var password by remember(company.id, company.accessPassword) { mutableStateOf(company.accessPassword) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isDirty by remember(loginId, password, company.loginId, company.accessPassword) {
        mutableStateOf(loginId != company.loginId || password != company.accessPassword)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SpaceCardElevated)
            .border(
                1.dp,
                if (isDirty) CosmicViolet else SpaceBorder,
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        // Company Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                CompanyLogoView(
                    logo = company.logoUrl,
                    size = 36.dp,
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = company.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        maxLines = 1
                    )
                    if (company.ownerName.isNotBlank()) {
                        Text(
                            text = "Director: ${company.ownerName}",
                            fontSize = 10.sp,
                            color = TextDim,
                            maxLines = 1
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Copy button
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Credentials",
                        tint = CosmicCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Full Edit button
                IconButton(
                    onClick = onEditProfile,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = CosmicIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ID and Password Editor Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Login ID Field
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Login ID",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CosmicCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = loginId,
                    onValueChange = { loginId = it },
                    placeholder = { Text("e.g. COM0001", color = TextDark, fontSize = 11.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SpaceBlack,
                        unfocusedContainerColor = SpaceBlack,
                        focusedBorderColor = CosmicCyan,
                        unfocusedBorderColor = SpaceBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("edit_login_id_${company.id}")
                )
            }

            // Access Password Field
            Column(modifier = Modifier.weight(1.3f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Access Password",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CosmicViolet
                    )
                    // Suggest Password button
                    Text(
                        text = "⚡ Generate",
                        fontSize = 9.sp,
                        color = CosmicPink,
                        modifier = Modifier
                            .clickable {
                                val cleanName = company.name.split(" ").firstOrNull()?.replace("[^a-zA-Z]".toRegex(), "") ?: "Matrix"
                                password = "${cleanName}@${Random.nextInt(100, 999)}"
                            }
                            .padding(horizontal = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Set Password", color = TextDark, fontSize = 11.sp) },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle",
                                tint = TextDim,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SpaceBlack,
                        unfocusedContainerColor = SpaceBlack,
                        focusedBorderColor = CosmicViolet,
                        unfocusedBorderColor = SpaceBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("edit_password_${company.id}")
                )
            }
        }

        if (isDirty) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Unsaved credential changes",
                    fontSize = 10.sp,
                    color = CosmicAmber
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onSave(loginId.trim(), password.trim()) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicEmerald),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        tint = TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save Changes", fontSize = 11.sp, color = TextWhite)
                }
            }
        }
    }
}
