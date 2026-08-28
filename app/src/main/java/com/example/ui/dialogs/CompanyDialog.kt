package com.example.ui.dialogs

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CompanyEntity
import com.example.ui.components.CompanyLogoView
import com.example.ui.components.CosmicInputField
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicRose
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.io.File
import java.util.UUID

private fun saveLogoToLocalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val logosDir = File(context.filesDir, "company_logos").apply { mkdirs() }
        val targetFile = File(logosDir, "logo_${System.currentTimeMillis()}.png")
        targetFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        targetFile.absolutePath
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyDialog(
    company: CompanyEntity?,
    onDismiss: () -> Unit,
    onSave: (CompanyEntity) -> Unit
) {
    val context = LocalContext.current

    var name by remember(company) { mutableStateOf(company?.name ?: "") }
    var logoUrl by remember(company) { mutableStateOf(company?.logoUrl ?: "") }
    var ownerName by remember(company) { mutableStateOf(company?.ownerName ?: "") }
    var email by remember(company) { mutableStateOf(company?.email ?: "") }
    var phone by remember(company) { mutableStateOf(company?.phone ?: "") }
    var address by remember(company) { mutableStateOf(company?.address ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    // Direct Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val savedPath = saveLogoToLocalStorage(context, uri)
            if (savedPath != null) {
                logoUrl = savedPath
                Toast.makeText(context, "Logo uploaded successfully!", Toast.LENGTH_SHORT).show()
            } else {
                // Fallback to direct URI string if copy failed
                logoUrl = uri.toString()
            }
        }
    }

    val presetEmojis = listOf("⚡", "☀️", "🏢", "🔋", "🌐", "🚀", "🪐", "💎")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
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
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Company",
                            tint = CosmicViolet
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (company == null) "Register New Company" else "Edit Company Profile",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Direct Company Logo Upload Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SpaceCardElevated)
                            .border(1.dp, CosmicViolet.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Company Logo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicViolet
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Logo Preview
                            CompanyLogoView(
                                logo = logoUrl,
                                size = 56.dp,
                                shape = RoundedCornerShape(14.dp)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                // Upload Button
                                Button(
                                    onClick = { imagePickerLauncher.launch("image/*") },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp)
                                        .testTag("upload_logo_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Upload Image",
                                        tint = TextWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (logoUrl.isBlank()) "Upload Image" else "Change Image",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextWhite
                                    )
                                }

                                if (logoUrl.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { logoUrl = "" }
                                            .padding(vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove Logo",
                                            tint = CosmicRose,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Remove Logo",
                                            fontSize = 11.sp,
                                            color = CosmicRose
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Emoji Selectors as an alternative
                        Text(
                            text = "Or choose a quick icon badge:",
                            fontSize = 10.sp,
                            color = TextDim
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            presetEmojis.forEach { emoji ->
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (logoUrl == emoji) CosmicViolet.copy(alpha = 0.3f) else SpaceCard)
                                        .border(
                                            1.dp,
                                            if (logoUrl == emoji) CosmicViolet else SpaceBorder,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { logoUrl = emoji }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 16.sp)
                                }
                            }
                        }
                    }

                    CosmicInputField(
                        label = "Company Name",
                        value = name,
                        onValueChange = { name = it; error = null },
                        placeholder = "e.g. Neelanjali Solar Matrix Pvt Ltd",
                        isRequired = true,
                        testTag = "company_name_input"
                    )

                    CosmicInputField(
                        label = "Owner / Director Name",
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        placeholder = "e.g. Pratyaksh Hans",
                        testTag = "company_owner_input"
                    )

                    CosmicInputField(
                        label = "Contact Phone",
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "+91 98765 43210",
                        testTag = "company_phone_input"
                    )

                    CosmicInputField(
                        label = "Official Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "contact@company.com",
                        testTag = "company_email_input"
                    )

                    CosmicInputField(
                        label = "Registered Office Address",
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Office / Plant Address",
                        isMultiline = true,
                        testTag = "company_address_input"
                    )

                    if (error != null) {
                        Text(
                            text = error ?: "",
                            color = Color(0xFFEF4444),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                error = "Company name is required."
                            } else {
                                val updated = company?.copy(
                                    name = name.trim(),
                                    logoUrl = logoUrl.trim(),
                                    ownerName = ownerName.trim(),
                                    email = email.trim(),
                                    phone = phone.trim(),
                                    address = address.trim()
                                ) ?: CompanyEntity(
                                    id = UUID.randomUUID().toString(),
                                    name = name.trim(),
                                    logoUrl = logoUrl.trim(),
                                    ownerName = ownerName.trim(),
                                    email = email.trim(),
                                    phone = phone.trim(),
                                    address = address.trim()
                                )
                                onSave(updated)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet),
                        modifier = Modifier.testTag("save_company_button")
                    ) {
                        Text("Save Company")
                    }
                }
            }
        }
    }
}
