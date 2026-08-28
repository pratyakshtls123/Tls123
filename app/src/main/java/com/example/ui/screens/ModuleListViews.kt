package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicRose
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.TextWhite
import com.example.util.FormatUtils

private fun copyToClipboard(context: Context, text: String, label: String = "Copied") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Matrix Info", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
}

private fun launchDialer(context: Context, phone: String) {
    if (phone.isNotBlank()) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.trim()}"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot launch dialer", Toast.LENGTH_SHORT).show()
        }
    }
}

// -------------------------------------------------------------
// STAGE 01: Telecalling List
// -------------------------------------------------------------
@Composable
fun TelecallingListView(
    records: List<TelecallingRecord>,
    onEdit: (TelecallingRecord) -> Unit,
    onDelete: (TelecallingRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (records.isEmpty()) {
        EmptyListState("No telecalling leads recorded yet.", "Click '+ Add Record' to log the first call.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
                    .testTag("telecalling_item_${item.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.prospectName.ifEmpty { "Unnamed Prospect" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            if (item.callDate.isNotBlank() || item.callTime.isNotBlank()) {
                                Text(
                                    text = "${item.callDate} ${item.callTime}".trim(),
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                        StatusBadge(status = item.callStatus)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (item.contactNumber.isNotBlank() || item.leadSource.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceCardElevated)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            if (item.contactNumber.isNotBlank()) {
                                Text(
                                    text = item.contactNumber,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CosmicCyan,
                                    modifier = Modifier.clickable { launchDialer(context, item.contactNumber) }
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (item.leadSource.isNotBlank()) {
                                Text(
                                    text = "Source: ${item.leadSource}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    if (item.capacityRequirement.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            Text("Requirement: ", fontSize = 11.sp, color = TextMuted)
                            Text(item.capacityRequirement, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextLight)
                        }
                    }

                    if (item.address.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.address, fontSize = 11.sp, color = TextDim, maxLines = 1)
                    }

                    if (item.remarks.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "“${item.remarks}”",
                            fontSize = 12.sp,
                            color = TextMuted,
                            maxLines = 3
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Footer with Telecaller Attribution and Actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (item.telecallerName.isNotBlank()) "Caller: ${item.telecallerName} (${item.telecallerCode})" else "",
                            fontSize = 10.sp,
                            color = TextDim
                        )
                        Row {
                            if (item.contactNumber.isNotBlank()) {
                                IconButton(
                                    onClick = { launchDialer(context, item.contactNumber) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = { copyToClipboard(context, item.contactNumber, "Phone") },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextDim, modifier = Modifier.size(16.dp))
                                }
                            }
                            IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 02: Sales DVR List
// -------------------------------------------------------------
@Composable
fun SalesDvrListView(
    records: List<SalesDvrRecord>,
    onEdit: (SalesDvrRecord) -> Unit,
    onDelete: (SalesDvrRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (records.isEmpty()) {
        EmptyListState("No sales DVR logs found.", "Field visits and meeting reports will appear here.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
                    .testTag("sales_dvr_item_${item.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.customerName.ifEmpty { "Prospect Visit" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Visit Date: ${item.visitDate} | Exec: ${item.execName} (${item.execCode})",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                        StatusBadge(status = item.leadStatus)
                    }

                    if (item.locationAddress.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "📍 ${item.locationAddress}", fontSize = 11.sp, color = TextLight)
                    }

                    if (item.nextFollowUp.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Next Follow-up: ${item.nextFollowUp}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CosmicAmber)
                    }

                    if (item.remarks.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "“${item.remarks}”", fontSize = 12.sp, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (item.contactNumber.isNotBlank()) {
                            Text(
                                text = item.contactNumber,
                                fontSize = 12.sp,
                                color = CosmicCyan,
                                modifier = Modifier.clickable { launchDialer(context, item.contactNumber) }
                            )
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }
                        Row {
                            if (item.contactNumber.isNotBlank()) {
                                IconButton(onClick = { launchDialer(context, item.contactNumber) }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                                }
                            }
                            IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 03: Customer Records List
// -------------------------------------------------------------
@Composable
fun CustomerListView(
    records: List<CustomerRecord>,
    onEdit: (CustomerRecord) -> Unit,
    onDelete: (CustomerRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (records.isEmpty()) {
        EmptyListState("No customer records found.", "Add full technical and financial lifecycle files here.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            val pendingAmt = item.totalProjectCost - item.totalReceived

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
                    .testTag("customer_item_${item.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.customerName.ifEmpty { "Customer Project" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "App No: ${item.applicationNumber.ifEmpty { "—" }} | ${item.plantCapacity} kW ${item.setupType}",
                                fontSize = 11.sp,
                                color = CosmicCyan
                            )
                        }
                        StatusBadge(status = item.paymentStatus)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Financial Card Block
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SpaceCardElevated)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Cost", fontSize = 10.sp, color = TextMuted)
                            Text(FormatUtils.formatCurrency(item.totalProjectCost), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Column {
                            Text("Received", fontSize = 10.sp, color = TextMuted)
                            Text(FormatUtils.formatCurrency(item.totalReceived), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CosmicEmerald)
                        }
                        Column {
                            Text("Balance Due", fontSize = 10.sp, color = TextMuted)
                            Text(FormatUtils.formatCurrency(pendingAmt), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (pendingAmt > 0) CosmicAmber else CosmicEmerald)
                        }
                    }

                    if (item.siteAddress.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "📍 ${item.siteAddress}", fontSize = 11.sp, color = TextDim, maxLines = 1)
                    }

                    if (item.pan.isNotBlank() || item.aadhar.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.pan.isNotBlank()) {
                                Text(
                                    text = "PAN: ${item.pan}",
                                    fontSize = 11.sp,
                                    color = CosmicIndigo,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (item.aadhar.isNotBlank()) {
                                    Text(text = "  •  ", fontSize = 11.sp, color = TextDark)
                                }
                            }
                            if (item.aadhar.isNotBlank()) {
                                Text(
                                    text = "Aadhar: ${item.aadhar}",
                                    fontSize = 11.sp,
                                    color = CosmicCyan,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    if (item.solarBrand.isNotBlank() || item.bankName.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Brand: ${item.solarBrand.ifEmpty { "—" }} | Bank: ${item.bankName.ifEmpty { "—" }}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Footer actions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (item.leadOwnerName.isNotBlank()) "Owner: ${item.leadOwnerName}" else "",
                            fontSize = 10.sp,
                            color = TextDim
                        )
                        Row {
                            if (item.phone.isNotBlank()) {
                                IconButton(onClick = { launchDialer(context, item.phone) }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                                }
                            }
                            IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 04: Bank Pendency List
// -------------------------------------------------------------
@Composable
fun BankPendencyListView(
    records: List<BankPendencyRecord>,
    onEdit: (BankPendencyRecord) -> Unit,
    onDelete: (BankPendencyRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (records.isEmpty()) {
        EmptyListState("No bank pendency cases.", "Bank loan sanction applications will appear here.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
                    .testTag("bank_pendency_item_${item.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.customerName.ifEmpty { "Bank Case" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "${item.bankName} - ${item.branchName}".trim(),
                                fontSize = 11.sp,
                                color = CosmicIndigo
                            )
                        }
                        StatusBadge(status = item.sanctionStatus)
                    }

                    if (item.applicationNumber.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "App Ref: ${item.applicationNumber}", fontSize = 11.sp, color = CosmicCyan)
                    }

                    if (item.pendencyRemarks.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "“${item.pendencyRemarks}”", fontSize = 12.sp, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (item.handlerName.isNotBlank()) "Handler: ${item.handlerName} (${item.handlerCode})" else "",
                            fontSize = 10.sp,
                            color = TextDim
                        )
                        Row {
                            if (item.contactNumber.isNotBlank()) {
                                IconButton(onClick = { launchDialer(context, item.contactNumber) }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                                }
                            }
                            IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 05: Banking DVR List
// -------------------------------------------------------------
@Composable
fun BankingDvrListView(
    records: List<BankingDvrRecord>,
    onEdit: (BankingDvrRecord) -> Unit,
    onDelete: (BankingDvrRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    if (records.isEmpty()) {
        EmptyListState("No banking DVR reports logged.", "Daily liaison visits to bank branches will appear here.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${item.bankName} - ${item.branchName}".trim(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Visit Date: ${item.visitDate} | Exec: ${item.execName} (${item.execCode})",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    if (item.customerFileName.isNotBlank() || item.applicationNumber.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Customer File: ${item.customerFileName} (${item.applicationNumber})",
                            fontSize = 11.sp,
                            color = CosmicCyan
                        )
                    }

                    if (item.bankOfficial.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Official Met: ${item.bankOfficial}", fontSize = 11.sp, color = TextLight)
                    }

                    if (item.remarks.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "“${item.remarks}”", fontSize = 12.sp, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 06: Employee Records List
// -------------------------------------------------------------
@Composable
fun EmployeeListView(
    records: List<EmployeeRecord>,
    onEdit: (EmployeeRecord) -> Unit,
    onDelete: (EmployeeRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (records.isEmpty()) {
        EmptyListState("No employees recorded.", "Register your team members and staff here.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.fullName.ifEmpty { "Employee" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "${item.designation.ifEmpty { "Staff" }} | Code: ${item.employeeCode}",
                                fontSize = 11.sp,
                                color = CosmicViolet
                            )
                        }
                        if (item.doj.isNotBlank()) {
                            Text(text = "DOJ: ${item.doj}", fontSize = 10.sp, color = TextDim)
                        }
                    }

                    if (item.contactNumber.isNotBlank() || item.email.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceCardElevated)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = item.contactNumber, fontSize = 11.sp, color = CosmicCyan)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = item.email, fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    if (item.address.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.address, fontSize = 11.sp, color = TextDim, maxLines = 1)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (item.emergencyContact.isNotBlank()) {
                            Text(text = "Emg: ${item.emergencyContact}", fontSize = 10.sp, color = TextDim)
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }
                        Row {
                            if (item.contactNumber.isNotBlank()) {
                                IconButton(onClick = { launchDialer(context, item.contactNumber) }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Default.Call, contentDescription = "Call", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                                }
                            }
                            IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 07: Franchisee Records List
// -------------------------------------------------------------
@Composable
fun FranchiseeListView(
    records: List<FranchiseeRecord>,
    onEdit: (FranchiseeRecord) -> Unit,
    onDelete: (FranchiseeRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (records.isEmpty()) {
        EmptyListState("No franchisee partners registered.", "Add authorized branches and regional hub agreements.")
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(records, key = { it.id }) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SpaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.franchiseName.ifEmpty { "Franchisee Branch" },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Code: ${item.franchiseCode} | Owner: ${item.ownerName.ifEmpty { "—" }}",
                                fontSize = 11.sp,
                                color = CosmicViolet
                            )
                        }
                    }

                    if (item.contactPhone.isNotBlank() || item.email.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceCardElevated)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = item.contactPhone, fontSize = 11.sp, color = CosmicCyan)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = item.email, fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    if (item.officeAddress.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "📍 ${item.officeAddress}", fontSize = 11.sp, color = TextDim, maxLines = 1)
                    }

                    if (item.agreementNote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Agreement: ${item.agreementNote}", fontSize = 11.sp, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (item.contactPhone.isNotBlank()) {
                            IconButton(onClick = { launchDialer(context, item.contactPhone) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                            }
                        }
                        IconButton(onClick = { onEdit(item) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { onDelete(item) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRose, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyListState(title: String, subtitle: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, fontSize = 12.sp, color = TextMuted)
        }
    }
}
