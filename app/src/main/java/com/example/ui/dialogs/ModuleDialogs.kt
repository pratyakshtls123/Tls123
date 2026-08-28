package com.example.ui.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import com.example.ui.components.CosmicInputField
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

private fun todayDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

@Composable
fun TelecallingDialog(
    companyId: String,
    record: TelecallingRecord?,
    onDismiss: () -> Unit,
    onSave: (TelecallingRecord) -> Unit
) {
    var callDate by remember(record) { mutableStateOf(record?.callDate?.ifEmpty { todayDate() } ?: todayDate()) }
    var callTime by remember(record) { mutableStateOf(record?.callTime ?: "") }
    var prospectName by remember(record) { mutableStateOf(record?.prospectName ?: "") }
    var contactNumber by remember(record) { mutableStateOf(record?.contactNumber ?: "") }
    var address by remember(record) { mutableStateOf(record?.address ?: "") }
    var leadSource by remember(record) { mutableStateOf(record?.leadSource ?: "") }
    var capacityRequirement by remember(record) { mutableStateOf(record?.capacityRequirement ?: "") }
    var callStatus by remember(record) { mutableStateOf(record?.callStatus ?: "") } // Typed freely
    var remarks by remember(record) { mutableStateOf(record?.remarks ?: "") }
    var telecallerName by remember(record) { mutableStateOf(record?.telecallerName ?: "") }
    var telecallerCode by remember(record) { mutableStateOf(record?.telecallerCode ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Log Telecalling Lead" else "Edit Lead Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CosmicInputField(
                        label = "Prospect Name",
                        value = prospectName,
                        onValueChange = { prospectName = it; error = null },
                        isRequired = true,
                        placeholder = "e.g. Dr. Rajesh Sharma"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(
                            label = "Call Date",
                            value = callDate,
                            onValueChange = { callDate = it },
                            placeholder = "YYYY-MM-DD",
                            modifier = Modifier.weight(1f)
                        )
                        CosmicInputField(
                            label = "Call Time",
                            value = callTime,
                            onValueChange = { callTime = it },
                            placeholder = "e.g. 4:30 PM",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    CosmicInputField(
                        label = "Contact Number",
                        value = contactNumber,
                        onValueChange = { contactNumber = it },
                        placeholder = "+91 98765 43210"
                    )
                    CosmicInputField(
                        label = "Call Status (Type your custom status)",
                        value = callStatus,
                        onValueChange = { callStatus = it },
                        placeholder = "Type status e.g. Interested, Callback, Converted..."
                    )
                    CosmicInputField(
                        label = "Capacity Requirement",
                        value = capacityRequirement,
                        onValueChange = { capacityRequirement = it },
                        placeholder = "e.g. 5 kW Rooftop / 20 kW Commercial"
                    )
                    CosmicInputField(
                        label = "Lead Source",
                        value = leadSource,
                        onValueChange = { leadSource = it },
                        placeholder = "Referral, Meta ad, Walk-in, Franchisee..."
                    )
                    CosmicInputField(
                        label = "Address / Location",
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Prospect location"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(
                            label = "Telecaller Name",
                            value = telecallerName,
                            onValueChange = { telecallerName = it },
                            placeholder = "Staff name",
                            modifier = Modifier.weight(1f)
                        )
                        CosmicInputField(
                            label = "Telecaller Code",
                            value = telecallerCode,
                            onValueChange = { telecallerCode = it },
                            placeholder = "TC-101",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    CosmicInputField(
                        label = "Remarks & Call Summary",
                        value = remarks,
                        onValueChange = { remarks = it },
                        placeholder = "Key discussion points...",
                        isMultiline = true
                    )

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (prospectName.isBlank()) {
                                error = "Prospect name is required."
                            } else {
                                val item = record?.copy(
                                    callDate = callDate.trim(),
                                    callTime = callTime.trim(),
                                    prospectName = prospectName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    address = address.trim(),
                                    leadSource = leadSource.trim(),
                                    capacityRequirement = capacityRequirement.trim(),
                                    callStatus = callStatus.trim(),
                                    remarks = remarks.trim(),
                                    telecallerName = telecallerName.trim(),
                                    telecallerCode = telecallerCode.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: TelecallingRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    callDate = callDate.trim(),
                                    callTime = callTime.trim(),
                                    prospectName = prospectName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    address = address.trim(),
                                    leadSource = leadSource.trim(),
                                    capacityRequirement = capacityRequirement.trim(),
                                    callStatus = callStatus.trim(),
                                    remarks = remarks.trim(),
                                    telecallerName = telecallerName.trim(),
                                    telecallerCode = telecallerCode.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save Record")
                    }
                }
            }
        }
    }
}

@Composable
fun SalesDvrDialog(
    companyId: String,
    record: SalesDvrRecord?,
    onDismiss: () -> Unit,
    onSave: (SalesDvrRecord) -> Unit
) {
    var visitDate by remember(record) { mutableStateOf(record?.visitDate?.ifEmpty { todayDate() } ?: todayDate()) }
    var execName by remember(record) { mutableStateOf(record?.execName ?: "") }
    var execCode by remember(record) { mutableStateOf(record?.execCode ?: "") }
    var customerName by remember(record) { mutableStateOf(record?.customerName ?: "") }
    var contactNumber by remember(record) { mutableStateOf(record?.contactNumber ?: "") }
    var locationAddress by remember(record) { mutableStateOf(record?.locationAddress ?: "") }
    var remarks by remember(record) { mutableStateOf(record?.remarks ?: "") }
    var leadStatus by remember(record) { mutableStateOf(record?.leadStatus ?: "") } // Typed freely
    var nextFollowUp by remember(record) { mutableStateOf(record?.nextFollowUp ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Log Sales DVR Visit" else "Edit DVR Report",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CosmicInputField(
                        label = "Sales Executive Name",
                        value = execName,
                        onValueChange = { execName = it; error = null },
                        isRequired = true,
                        placeholder = "e.g. Rohit Mehra"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(
                            label = "Visit Date",
                            value = visitDate,
                            onValueChange = { visitDate = it },
                            placeholder = "YYYY-MM-DD",
                            modifier = Modifier.weight(1f)
                        )
                        CosmicInputField(
                            label = "Executive Code",
                            value = execCode,
                            onValueChange = { execCode = it },
                            placeholder = "SALES-01",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    CosmicInputField(
                        label = "Customer / Prospect Met",
                        value = customerName,
                        onValueChange = { customerName = it },
                        placeholder = "e.g. Sharma Cold Storage"
                    )
                    CosmicInputField(
                        label = "Contact Number",
                        value = contactNumber,
                        onValueChange = { contactNumber = it },
                        placeholder = "+91 98765 43210"
                    )
                    CosmicInputField(
                        label = "Visit Location Address",
                        value = locationAddress,
                        onValueChange = { locationAddress = it },
                        placeholder = "Site address"
                    )
                    CosmicInputField(
                        label = "Lead Status (Type your custom status)",
                        value = leadStatus,
                        onValueChange = { leadStatus = it },
                        placeholder = "Type status e.g. Hot, Warm, Cold, Site Survey Done..."
                    )
                    CosmicInputField(
                        label = "Next Follow-up Date",
                        value = nextFollowUp,
                        onValueChange = { nextFollowUp = it },
                        placeholder = "YYYY-MM-DD"
                    )
                    CosmicInputField(
                        label = "Discussion Remarks / Outcome",
                        value = remarks,
                        onValueChange = { remarks = it },
                        placeholder = "Meeting outcome, plant feasibility...",
                        isMultiline = true
                    )

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (execName.isBlank()) {
                                error = "Sales executive name is required."
                            } else {
                                val item = record?.copy(
                                    visitDate = visitDate.trim(),
                                    execName = execName.trim(),
                                    execCode = execCode.trim(),
                                    customerName = customerName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    locationAddress = locationAddress.trim(),
                                    remarks = remarks.trim(),
                                    leadStatus = leadStatus.trim(),
                                    nextFollowUp = nextFollowUp.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: SalesDvrRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    visitDate = visitDate.trim(),
                                    execName = execName.trim(),
                                    execCode = execCode.trim(),
                                    customerName = customerName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    locationAddress = locationAddress.trim(),
                                    remarks = remarks.trim(),
                                    leadStatus = leadStatus.trim(),
                                    nextFollowUp = nextFollowUp.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save DVR")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerDialog(
    companyId: String,
    record: CustomerRecord?,
    onDismiss: () -> Unit,
    onSave: (CustomerRecord) -> Unit
) {
    var customerName by remember(record) { mutableStateOf(record?.customerName ?: "") }
    var phone by remember(record) { mutableStateOf(record?.phone ?: "") }
    var email by remember(record) { mutableStateOf(record?.email ?: "") }
    var pan by remember(record) { mutableStateOf(record?.pan ?: "") }
    var aadhar by remember(record) { mutableStateOf(record?.aadhar ?: "") }
    var siteAddress by remember(record) { mutableStateOf(record?.siteAddress ?: "") }
    var electricityAccountNumber by remember(record) { mutableStateOf(record?.electricityAccountNumber ?: "") }
    var utilityProvider by remember(record) { mutableStateOf(record?.utilityProvider ?: "") }
    var applicationNumber by remember(record) { mutableStateOf(record?.applicationNumber ?: "") }
    var plantCapacity by remember(record) { mutableStateOf(if ((record?.plantCapacity ?: 0.0) > 0) record?.plantCapacity.toString() else "") }
    var solarBrand by remember(record) { mutableStateOf(record?.solarBrand ?: "") }
    var setupType by remember(record) { mutableStateOf(record?.setupType ?: "") }
    var installationCategory by remember(record) { mutableStateOf(record?.installationCategory ?: "") }
    var bankName by remember(record) { mutableStateOf(record?.bankName ?: "") }
    var branchName by remember(record) { mutableStateOf(record?.branchName ?: "") }
    var ifsc by remember(record) { mutableStateOf(record?.ifsc ?: "") }
    var accountNumber by remember(record) { mutableStateOf(record?.accountNumber ?: "") }
    var paymentMethod by remember(record) { mutableStateOf(record?.paymentMethod ?: "") }
    var registrationStatus by remember(record) { mutableStateOf(record?.registrationStatus ?: "") }
    var fileLoginDate by remember(record) { mutableStateOf(record?.fileLoginDate ?: "") }
    var paymentStatus by remember(record) { mutableStateOf(record?.paymentStatus ?: "") } // Typed freely
    var totalProjectCost by remember(record) { mutableStateOf(if ((record?.totalProjectCost ?: 0.0) > 0) record?.totalProjectCost.toString() else "") }
    var totalReceived by remember(record) { mutableStateOf(if ((record?.totalReceived ?: 0.0) > 0) record?.totalReceived.toString() else "") }
    var pendingReason by remember(record) { mutableStateOf(record?.pendingReason ?: "") }
    var leadOwnerName by remember(record) { mutableStateOf(record?.leadOwnerName ?: "") }
    var leadOwnerCode by remember(record) { mutableStateOf(record?.leadOwnerCode ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Add Customer Project File" else "Edit Customer Record",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("1. Customer & Site Profile", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CosmicViolet)
                    CosmicInputField(
                        label = "Customer Name",
                        value = customerName,
                        onValueChange = { customerName = it; error = null },
                        isRequired = true,
                        placeholder = "e.g. Kavita Rathi"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Phone", value = phone, onValueChange = { phone = it }, placeholder = "+91...", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Email", value = email, onValueChange = { email = it }, placeholder = "email@...", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Site Installation Address", value = siteAddress, onValueChange = { siteAddress = it }, placeholder = "Full Address", isMultiline = true)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "PAN Number", value = pan, onValueChange = { pan = it }, placeholder = "ABCDE1234F", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Aadhar No.", value = aadhar, onValueChange = { aadhar = it }, placeholder = "1234 5678 9012", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Electricity A/c No", value = electricityAccountNumber, onValueChange = { electricityAccountNumber = it }, placeholder = "CA / K No.", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Utility / Discom Provider", value = utilityProvider, onValueChange = { utilityProvider = it }, placeholder = "DHBVN / TPDDL", modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("2. Solar Plant Specifications", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CosmicViolet)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Capacity (kW)", value = plantCapacity, onValueChange = { plantCapacity = it }, isNumber = true, placeholder = "e.g. 5", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Setup Type", value = setupType, onValueChange = { setupType = it }, placeholder = "On-Grid / Off-Grid", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Installation Category", value = installationCategory, onValueChange = { installationCategory = it }, placeholder = "Residential / Commercial", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Solar Brand (Panel/Inverter)", value = solarBrand, onValueChange = { solarBrand = it }, placeholder = "Tata / Adani / Growatt", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Portal Application No", value = applicationNumber, onValueChange = { applicationNumber = it }, placeholder = "SL-2026-...")

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("3. Financial & Payment Matrix", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CosmicViolet)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Total Project Cost (₹)", value = totalProjectCost, onValueChange = { totalProjectCost = it }, isNumber = true, placeholder = "e.g. 350000", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Amount Received (₹)", value = totalReceived, onValueChange = { totalReceived = it }, isNumber = true, placeholder = "e.g. 150000", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(
                        label = "Payment Status (Type your custom status)",
                        value = paymentStatus,
                        onValueChange = { paymentStatus = it },
                        placeholder = "Type status e.g. Fully Paid, Partially Paid, Subsidy Pending..."
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Payment Method", value = paymentMethod, onValueChange = { paymentMethod = it }, placeholder = "Finance / Cheque / Online", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "File Login Date", value = fileLoginDate, onValueChange = { fileLoginDate = it }, placeholder = "YYYY-MM-DD", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Pending Status Reason / Remarks", value = pendingReason, onValueChange = { pendingReason = it }, placeholder = "Reason if balance is pending")

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("4. Bank & Lead Ownership", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CosmicViolet)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Bank Name", value = bankName, onValueChange = { bankName = it }, placeholder = "SBI / HDFC / PNB", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Branch & IFSC", value = branchName, onValueChange = { branchName = it }, placeholder = "Branch / IFSC", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Lead Owner Name", value = leadOwnerName, onValueChange = { leadOwnerName = it }, placeholder = "Sales Rep Name", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Owner Code", value = leadOwnerCode, onValueChange = { leadOwnerCode = it }, placeholder = "Code", modifier = Modifier.weight(1f))
                    }

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (customerName.isBlank()) {
                                error = "Customer name is required."
                            } else {
                                val item = record?.copy(
                                    customerName = customerName.trim(),
                                    phone = phone.trim(),
                                    email = email.trim(),
                                    pan = pan.trim(),
                                    aadhar = aadhar.trim(),
                                    siteAddress = siteAddress.trim(),
                                    electricityAccountNumber = electricityAccountNumber.trim(),
                                    utilityProvider = utilityProvider.trim(),
                                    applicationNumber = applicationNumber.trim(),
                                    plantCapacity = plantCapacity.toDoubleOrNull() ?: 0.0,
                                    solarBrand = solarBrand.trim(),
                                    setupType = setupType.trim(),
                                    installationCategory = installationCategory.trim(),
                                    bankName = bankName.trim(),
                                    branchName = branchName.trim(),
                                    ifsc = ifsc.trim(),
                                    accountNumber = accountNumber.trim(),
                                    paymentMethod = paymentMethod.trim(),
                                    registrationStatus = registrationStatus.trim(),
                                    fileLoginDate = fileLoginDate.trim(),
                                    paymentStatus = paymentStatus.trim(),
                                    totalProjectCost = totalProjectCost.toDoubleOrNull() ?: 0.0,
                                    totalReceived = totalReceived.toDoubleOrNull() ?: 0.0,
                                    pendingReason = pendingReason.trim(),
                                    leadOwnerName = leadOwnerName.trim(),
                                    leadOwnerCode = leadOwnerCode.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: CustomerRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    customerName = customerName.trim(),
                                    phone = phone.trim(),
                                    email = email.trim(),
                                    pan = pan.trim(),
                                    aadhar = aadhar.trim(),
                                    siteAddress = siteAddress.trim(),
                                    electricityAccountNumber = electricityAccountNumber.trim(),
                                    utilityProvider = utilityProvider.trim(),
                                    applicationNumber = applicationNumber.trim(),
                                    plantCapacity = plantCapacity.toDoubleOrNull() ?: 0.0,
                                    solarBrand = solarBrand.trim(),
                                    setupType = setupType.trim(),
                                    installationCategory = installationCategory.trim(),
                                    bankName = bankName.trim(),
                                    branchName = branchName.trim(),
                                    ifsc = ifsc.trim(),
                                    accountNumber = accountNumber.trim(),
                                    paymentMethod = paymentMethod.trim(),
                                    registrationStatus = registrationStatus.trim(),
                                    fileLoginDate = fileLoginDate.trim(),
                                    paymentStatus = paymentStatus.trim(),
                                    totalProjectCost = totalProjectCost.toDoubleOrNull() ?: 0.0,
                                    totalReceived = totalReceived.toDoubleOrNull() ?: 0.0,
                                    pendingReason = pendingReason.trim(),
                                    leadOwnerName = leadOwnerName.trim(),
                                    leadOwnerCode = leadOwnerCode.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save Customer")
                    }
                }
            }
        }
    }
}

@Composable
fun BankPendencyDialog(
    companyId: String,
    record: BankPendencyRecord?,
    onDismiss: () -> Unit,
    onSave: (BankPendencyRecord) -> Unit
) {
    var customerName by remember(record) { mutableStateOf(record?.customerName ?: "") }
    var contactNumber by remember(record) { mutableStateOf(record?.contactNumber ?: "") }
    var applicationNumber by remember(record) { mutableStateOf(record?.applicationNumber ?: "") }
    var bankName by remember(record) { mutableStateOf(record?.bankName ?: "") }
    var branchName by remember(record) { mutableStateOf(record?.branchName ?: "") }
    var ifsc by remember(record) { mutableStateOf(record?.ifsc ?: "") }
    var handlerName by remember(record) { mutableStateOf(record?.handlerName ?: "") }
    var handlerCode by remember(record) { mutableStateOf(record?.handlerCode ?: "") }
    var sanctionStatus by remember(record) { mutableStateOf(record?.sanctionStatus ?: "") } // Typed freely
    var pendencyRemarks by remember(record) { mutableStateOf(record?.pendencyRemarks ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Log Bank Pendency Case" else "Edit Bank Pendency",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CosmicInputField(
                        label = "Customer Name",
                        value = customerName,
                        onValueChange = { customerName = it; error = null },
                        isRequired = true,
                        placeholder = "e.g. Kavita Rathi"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Contact Number", value = contactNumber, onValueChange = { contactNumber = it }, placeholder = "+91...", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Application Number", value = applicationNumber, onValueChange = { applicationNumber = it }, placeholder = "App / File No.", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Bank Name", value = bankName, onValueChange = { bankName = it }, placeholder = "SBI / PNB / Canara", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Branch Name", value = branchName, onValueChange = { branchName = it }, placeholder = "Branch / IFSC", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Internal Handler Name", value = handlerName, onValueChange = { handlerName = it }, placeholder = "Executive Name", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Handler Code", value = handlerCode, onValueChange = { handlerCode = it }, placeholder = "BNK-01", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(
                        label = "Sanction Status (Type your custom status)",
                        value = sanctionStatus,
                        onValueChange = { sanctionStatus = it },
                        placeholder = "Type status e.g. Under Review, Approved, Query Raised..."
                    )
                    CosmicInputField(
                        label = "Pendency Remarks / Delay Reason",
                        value = pendencyRemarks,
                        onValueChange = { pendencyRemarks = it },
                        placeholder = "Documents pending, survey done...",
                        isMultiline = true
                    )

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (customerName.isBlank()) {
                                error = "Customer name is required."
                            } else {
                                val item = record?.copy(
                                    customerName = customerName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    applicationNumber = applicationNumber.trim(),
                                    bankName = bankName.trim(),
                                    branchName = branchName.trim(),
                                    ifsc = ifsc.trim(),
                                    handlerName = handlerName.trim(),
                                    handlerCode = handlerCode.trim(),
                                    sanctionStatus = sanctionStatus.trim(),
                                    pendencyRemarks = pendencyRemarks.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: BankPendencyRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    customerName = customerName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    applicationNumber = applicationNumber.trim(),
                                    bankName = bankName.trim(),
                                    branchName = branchName.trim(),
                                    ifsc = ifsc.trim(),
                                    handlerName = handlerName.trim(),
                                    handlerCode = handlerCode.trim(),
                                    sanctionStatus = sanctionStatus.trim(),
                                    pendencyRemarks = pendencyRemarks.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save Case")
                    }
                }
            }
        }
    }
}

@Composable
fun BankingDvrDialog(
    companyId: String,
    record: BankingDvrRecord?,
    onDismiss: () -> Unit,
    onSave: (BankingDvrRecord) -> Unit
) {
    var visitDate by remember(record) { mutableStateOf(record?.visitDate?.ifEmpty { todayDate() } ?: todayDate()) }
    var execName by remember(record) { mutableStateOf(record?.execName ?: "") }
    var execCode by remember(record) { mutableStateOf(record?.execCode ?: "") }
    var bankName by remember(record) { mutableStateOf(record?.bankName ?: "") }
    var branchName by remember(record) { mutableStateOf(record?.branchName ?: "") }
    var customerFileName by remember(record) { mutableStateOf(record?.customerFileName ?: "") }
    var applicationNumber by remember(record) { mutableStateOf(record?.applicationNumber ?: "") }
    var bankOfficial by remember(record) { mutableStateOf(record?.bankOfficial ?: "") }
    var remarks by remember(record) { mutableStateOf(record?.remarks ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Log Banking DVR Visit" else "Edit Banking DVR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CosmicInputField(
                        label = "Banking Executive Name",
                        value = execName,
                        onValueChange = { execName = it; error = null },
                        isRequired = true,
                        placeholder = "e.g. Suresh Gupta"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Visit Date", value = visitDate, onValueChange = { visitDate = it }, placeholder = "YYYY-MM-DD", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Executive Code", value = execCode, onValueChange = { execCode = it }, placeholder = "BNK-02", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Bank Name", value = bankName, onValueChange = { bankName = it }, placeholder = "e.g. PNB / SBI", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Branch Name", value = branchName, onValueChange = { branchName = it }, placeholder = "Sector 18", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Associated Customer File", value = customerFileName, onValueChange = { customerFileName = it }, placeholder = "Customer / Plant file name")
                    CosmicInputField(label = "Application / File No.", value = applicationNumber, onValueChange = { applicationNumber = it }, placeholder = "PNB-SOL-9901")
                    CosmicInputField(label = "Bank Official Met (Name & Role)", value = bankOfficial, onValueChange = { bankOfficial = it }, placeholder = "e.g. Mr. Verma (Chief Loan Officer)")
                    CosmicInputField(label = "Meeting Remarks / Outcome", value = remarks, onValueChange = { remarks = it }, placeholder = "Discussion outcome...", isMultiline = true)

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (execName.isBlank()) {
                                error = "Banking executive name is required."
                            } else {
                                val item = record?.copy(
                                    visitDate = visitDate.trim(),
                                    execName = execName.trim(),
                                    execCode = execCode.trim(),
                                    bankName = bankName.trim(),
                                    branchName = branchName.trim(),
                                    customerFileName = customerFileName.trim(),
                                    applicationNumber = applicationNumber.trim(),
                                    bankOfficial = bankOfficial.trim(),
                                    remarks = remarks.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: BankingDvrRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    visitDate = visitDate.trim(),
                                    execName = execName.trim(),
                                    execCode = execCode.trim(),
                                    bankName = bankName.trim(),
                                    branchName = branchName.trim(),
                                    customerFileName = customerFileName.trim(),
                                    applicationNumber = applicationNumber.trim(),
                                    bankOfficial = bankOfficial.trim(),
                                    remarks = remarks.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save DVR")
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeDialog(
    companyId: String,
    record: EmployeeRecord?,
    onDismiss: () -> Unit,
    onSave: (EmployeeRecord) -> Unit
) {
    var employeeCode by remember(record) { mutableStateOf(record?.employeeCode ?: "") }
    var fullName by remember(record) { mutableStateOf(record?.fullName ?: "") }
    var contactNumber by remember(record) { mutableStateOf(record?.contactNumber ?: "") }
    var email by remember(record) { mutableStateOf(record?.email ?: "") }
    var address by remember(record) { mutableStateOf(record?.address ?: "") }
    var doj by remember(record) { mutableStateOf(record?.doj ?: "") }
    var designation by remember(record) { mutableStateOf(record?.designation ?: "") }
    var bankAccountDetails by remember(record) { mutableStateOf(record?.bankAccountDetails ?: "") }
    var emergencyContact by remember(record) { mutableStateOf(record?.emergencyContact ?: "") }
    var agreementNote by remember(record) { mutableStateOf(record?.agreementNote ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Register New Employee" else "Edit Employee Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Employee Code", value = employeeCode, onValueChange = { employeeCode = it }, isRequired = true, placeholder = "TLS-E-001", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Full Name", value = fullName, onValueChange = { fullName = it; error = null }, isRequired = true, placeholder = "Full Name", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Designation / Role", value = designation, onValueChange = { designation = it }, placeholder = "Sales / Engineer", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Date of Joining", value = doj, onValueChange = { doj = it }, placeholder = "YYYY-MM-DD", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Contact Phone", value = contactNumber, onValueChange = { contactNumber = it }, placeholder = "+91...", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Email Address", value = email, onValueChange = { email = it }, placeholder = "email@...", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Residential Address", value = address, onValueChange = { address = it }, placeholder = "Address", isMultiline = true)
                    CosmicInputField(label = "Bank Account (Payroll)", value = bankAccountDetails, onValueChange = { bankAccountDetails = it }, placeholder = "Bank Name, A/c No, IFSC")
                    CosmicInputField(label = "Emergency Contact", value = emergencyContact, onValueChange = { emergencyContact = it }, placeholder = "Name & Phone")
                    CosmicInputField(label = "Agreement / Contract Reference", value = agreementNote, onValueChange = { agreementNote = it }, placeholder = "Contract / NDA reference note")

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (fullName.isBlank()) {
                                error = "Employee full name is required."
                            } else {
                                val item = record?.copy(
                                    employeeCode = employeeCode.trim(),
                                    fullName = fullName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    email = email.trim(),
                                    address = address.trim(),
                                    doj = doj.trim(),
                                    designation = designation.trim(),
                                    bankAccountDetails = bankAccountDetails.trim(),
                                    emergencyContact = emergencyContact.trim(),
                                    agreementNote = agreementNote.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: EmployeeRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    employeeCode = employeeCode.trim(),
                                    fullName = fullName.trim(),
                                    contactNumber = contactNumber.trim(),
                                    email = email.trim(),
                                    address = address.trim(),
                                    doj = doj.trim(),
                                    designation = designation.trim(),
                                    bankAccountDetails = bankAccountDetails.trim(),
                                    emergencyContact = emergencyContact.trim(),
                                    agreementNote = agreementNote.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save Employee")
                    }
                }
            }
        }
    }
}

@Composable
fun FranchiseeDialog(
    companyId: String,
    record: FranchiseeRecord?,
    onDismiss: () -> Unit,
    onSave: (FranchiseeRecord) -> Unit
) {
    var franchiseCode by remember(record) { mutableStateOf(record?.franchiseCode ?: "") }
    var franchiseName by remember(record) { mutableStateOf(record?.franchiseName ?: "") }
    var ownerName by remember(record) { mutableStateOf(record?.ownerName ?: "") }
    var contactPhone by remember(record) { mutableStateOf(record?.contactPhone ?: "") }
    var email by remember(record) { mutableStateOf(record?.email ?: "") }
    var officeAddress by remember(record) { mutableStateOf(record?.officeAddress ?: "") }
    var bankAccountNumber by remember(record) { mutableStateOf(record?.bankAccountNumber ?: "") }
    var bankName by remember(record) { mutableStateOf(record?.bankName ?: "") }
    var ifsc by remember(record) { mutableStateOf(record?.ifsc ?: "") }
    var agreementNote by remember(record) { mutableStateOf(record?.agreementNote ?: "") }

    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SpaceCard,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(1.dp, SpaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (record == null) "Register New Franchisee" else "Edit Franchisee Partner",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Franchise Code", value = franchiseCode, onValueChange = { franchiseCode = it }, isRequired = true, placeholder = "FRAN-01", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Franchise Name", value = franchiseName, onValueChange = { franchiseName = it; error = null }, isRequired = true, placeholder = "Branch / Hub Name", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Owner / Partner Name", value = ownerName, onValueChange = { ownerName = it }, placeholder = "Partner Name", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Contact Phone", value = contactPhone, onValueChange = { contactPhone = it }, placeholder = "+91...", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Official Email", value = email, onValueChange = { email = it }, placeholder = "partner@...")
                    CosmicInputField(label = "Registered Office Address", value = officeAddress, onValueChange = { officeAddress = it }, placeholder = "Full Address", isMultiline = true)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        CosmicInputField(label = "Bank Name", value = bankName, onValueChange = { bankName = it }, placeholder = "Bank Name", modifier = Modifier.weight(1f))
                        CosmicInputField(label = "Account / IFSC", value = bankAccountNumber, onValueChange = { bankAccountNumber = it }, placeholder = "A/c & IFSC", modifier = Modifier.weight(1f))
                    }
                    CosmicInputField(label = "Franchise Agreement Reference", value = agreementNote, onValueChange = { agreementNote = it }, placeholder = "Terms, tenure, note...")

                    if (error != null) {
                        Text(text = error ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (franchiseName.isBlank()) {
                                error = "Franchise name is required."
                            } else {
                                val item = record?.copy(
                                    franchiseCode = franchiseCode.trim(),
                                    franchiseName = franchiseName.trim(),
                                    ownerName = ownerName.trim(),
                                    contactPhone = contactPhone.trim(),
                                    email = email.trim(),
                                    officeAddress = officeAddress.trim(),
                                    bankAccountNumber = bankAccountNumber.trim(),
                                    bankName = bankName.trim(),
                                    ifsc = ifsc.trim(),
                                    agreementNote = agreementNote.trim(),
                                    updatedAt = System.currentTimeMillis()
                                ) ?: FranchiseeRecord(
                                    id = UUID.randomUUID().toString(),
                                    companyId = companyId,
                                    franchiseCode = franchiseCode.trim(),
                                    franchiseName = franchiseName.trim(),
                                    ownerName = ownerName.trim(),
                                    contactPhone = contactPhone.trim(),
                                    email = email.trim(),
                                    officeAddress = officeAddress.trim(),
                                    bankAccountNumber = bankAccountNumber.trim(),
                                    bankName = bankName.trim(),
                                    ifsc = ifsc.trim(),
                                    agreementNote = agreementNote.trim()
                                )
                                onSave(item)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CosmicViolet)
                    ) {
                        Text("Save Franchisee")
                    }
                }
            }
        }
    }
}
