package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CompanyEntity
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import com.example.ui.components.CompanyLogoView
import com.example.ui.dialogs.BankPendencyDialog
import com.example.ui.dialogs.BankingDvrDialog
import com.example.ui.dialogs.CompanyDialog
import com.example.ui.dialogs.CustomerDialog
import com.example.ui.dialogs.EmployeeDialog
import com.example.ui.dialogs.FranchiseeDialog
import com.example.ui.dialogs.LogoutConfirmationDialog
import com.example.ui.dialogs.MasterAccessCredentialsDialog
import com.example.ui.dialogs.PrintExportDialog
import com.example.ui.dialogs.SalesDvrDialog
import com.example.ui.dialogs.TelecallingDialog
import com.example.ui.theme.CosmicAmber
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
import com.example.ui.viewmodel.AuthRole
import com.example.ui.viewmodel.MatrixModule
import com.example.ui.viewmodel.MatrixViewModel
import com.example.util.FormatUtils
import com.example.util.PrintAndExcelUtils

@Composable
fun MainMatrixScreen(
    viewModel: MatrixViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val currentRole by viewModel.currentRole.collectAsState()
    val isMasterAdmin = currentRole is AuthRole.MasterAdmin
    val companyTenant = currentRole as? AuthRole.CompanyTenant

    val companies by viewModel.allCompanies.collectAsState()
    val selectedCompanyId by viewModel.selectedCompanyId.collectAsState()
    val selectedModule by viewModel.selectedModule.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Auto-select company
    val currentCompany = companies.find { it.id == selectedCompanyId } ?: companies.firstOrNull()
    if (selectedCompanyId == null && currentCompany != null) {
        viewModel.selectCompany(currentCompany.id)
    }

    val telecallingRecords by viewModel.telecallingRecords.collectAsState()
    val salesDvrRecords by viewModel.salesDvrRecords.collectAsState()
    val customerRecords by viewModel.customerRecords.collectAsState()
    val bankPendencyRecords by viewModel.bankPendencyRecords.collectAsState()
    val bankingDvrRecords by viewModel.bankingDvrRecords.collectAsState()
    val employeeRecords by viewModel.employeeRecords.collectAsState()
    val franchiseeRecords by viewModel.franchiseeRecords.collectAsState()

    // Filtering
    val filteredTelecalling = telecallingRecords.filter {
        searchQuery.isBlank() ||
                it.prospectName.contains(searchQuery, ignoreCase = true) ||
                it.contactNumber.contains(searchQuery, ignoreCase = true) ||
                it.callStatus.contains(searchQuery, ignoreCase = true) ||
                it.telecallerName.contains(searchQuery, ignoreCase = true)
    }

    val filteredSalesDvr = salesDvrRecords.filter {
        searchQuery.isBlank() ||
                it.customerName.contains(searchQuery, ignoreCase = true) ||
                it.execName.contains(searchQuery, ignoreCase = true) ||
                it.leadStatus.contains(searchQuery, ignoreCase = true)
    }

    val filteredCustomers = customerRecords.filter {
        searchQuery.isBlank() ||
                it.customerName.contains(searchQuery, ignoreCase = true) ||
                it.phone.contains(searchQuery, ignoreCase = true) ||
                it.pan.contains(searchQuery, ignoreCase = true) ||
                it.aadhar.contains(searchQuery, ignoreCase = true) ||
                it.paymentStatus.contains(searchQuery, ignoreCase = true) ||
                it.solarBrand.contains(searchQuery, ignoreCase = true) ||
                it.applicationNumber.contains(searchQuery, ignoreCase = true)
    }

    val filteredBankPendency = bankPendencyRecords.filter {
        searchQuery.isBlank() ||
                it.customerName.contains(searchQuery, ignoreCase = true) ||
                it.bankName.contains(searchQuery, ignoreCase = true) ||
                it.sanctionStatus.contains(searchQuery, ignoreCase = true)
    }

    val filteredBankingDvr = bankingDvrRecords.filter {
        searchQuery.isBlank() ||
                it.bankName.contains(searchQuery, ignoreCase = true) ||
                it.execName.contains(searchQuery, ignoreCase = true) ||
                it.customerFileName.contains(searchQuery, ignoreCase = true)
    }

    val filteredEmployees = employeeRecords.filter {
        searchQuery.isBlank() ||
                it.fullName.contains(searchQuery, ignoreCase = true) ||
                it.employeeCode.contains(searchQuery, ignoreCase = true) ||
                it.designation.contains(searchQuery, ignoreCase = true)
    }

    val filteredFranchisees = franchiseeRecords.filter {
        searchQuery.isBlank() ||
                it.franchiseName.contains(searchQuery, ignoreCase = true) ||
                it.franchiseCode.contains(searchQuery, ignoreCase = true) ||
                it.ownerName.contains(searchQuery, ignoreCase = true)
    }

    // Modal / Dialog States
    var showCompanyDropdown by remember { mutableStateOf(false) }
    var companyToEdit by remember { mutableStateOf<CompanyEntity?>(null) }
    var showCompanyModal by remember { mutableStateOf(false) }
    var showMasterCredentialsDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmationDialog by remember { mutableStateOf(false) }

    var telecallingToEdit by remember { mutableStateOf<TelecallingRecord?>(null) }
    var showTelecallingDialog by remember { mutableStateOf(false) }

    var salesDvrToEdit by remember { mutableStateOf<SalesDvrRecord?>(null) }
    var showSalesDvrDialog by remember { mutableStateOf(false) }

    var customerToEdit by remember { mutableStateOf<CustomerRecord?>(null) }
    var showCustomerDialog by remember { mutableStateOf(false) }

    var bankPendencyToEdit by remember { mutableStateOf<BankPendencyRecord?>(null) }
    var showBankPendencyDialog by remember { mutableStateOf(false) }

    var bankingDvrToEdit by remember { mutableStateOf<BankingDvrRecord?>(null) }
    var showBankingDvrDialog by remember { mutableStateOf(false) }

    var employeeToEdit by remember { mutableStateOf<EmployeeRecord?>(null) }
    var showEmployeeDialog by remember { mutableStateOf(false) }

    var franchiseeToEdit by remember { mutableStateOf<FranchiseeRecord?>(null) }
    var showFranchiseeDialog by remember { mutableStateOf(false) }

    var printExportTargetModule by remember { mutableStateOf<MatrixModule?>(null) }

    val activeCompanyId = currentCompany?.id ?: "comp_default"

    Scaffold(
        containerColor = SpaceBlack,
        floatingActionButton = {
            if (selectedModule != MatrixModule.OVERVIEW) {
                FloatingActionButton(
                    onClick = {
                        when (selectedModule) {
                            MatrixModule.TELECALLING -> {
                                telecallingToEdit = null
                                showTelecallingDialog = true
                            }
                            MatrixModule.SALES_DVR -> {
                                salesDvrToEdit = null
                                showSalesDvrDialog = true
                            }
                            MatrixModule.CUSTOMERS -> {
                                customerToEdit = null
                                showCustomerDialog = true
                            }
                            MatrixModule.BANK_PENDENCY -> {
                                bankPendencyToEdit = null
                                showBankPendencyDialog = true
                            }
                            MatrixModule.BANKING_DVR -> {
                                bankingDvrToEdit = null
                                showBankingDvrDialog = true
                            }
                            MatrixModule.EMPLOYEES -> {
                                employeeToEdit = null
                                showEmployeeDialog = true
                            }
                            MatrixModule.FRANCHISEES -> {
                                franchiseeToEdit = null
                                showFranchiseeDialog = true
                            }
                            else -> {}
                        }
                    },
                    containerColor = CosmicViolet,
                    contentColor = TextWhite,
                    modifier = Modifier.testTag("fab_add_record")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Record")
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Top Matrix Header Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpaceCard)
                    .border(1.dp, SpaceBorder)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Brand
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CosmicViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "StarLink",
                            tint = TextWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "StarLink TLS123",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "UNIVERSE MATRIX",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicIndigo,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Company Selector Pill & Lock Action
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isMasterAdmin) {
                        // Master Admin Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CosmicIndigo.copy(alpha = 0.25f))
                                .border(1.dp, CosmicIndigo, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "👑 ADMIN",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicIndigo
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    } else if (companyTenant != null) {
                        // Company Tenant Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CosmicViolet.copy(alpha = 0.25f))
                                .border(1.dp, CosmicViolet, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "🏢 ${companyTenant.loginId.ifEmpty { "PORTAL" }}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicViolet
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(SpaceCardElevated)
                                .border(1.dp, SpaceBorder, RoundedCornerShape(100.dp))
                                .clickable {
                                    if (isMasterAdmin) {
                                        showCompanyDropdown = true
                                    }
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("company_selector_pill")
                        ) {
                            CompanyLogoView(
                                logo = currentCompany?.logoUrl ?: "⚡",
                                size = 20.dp,
                                shape = CircleShape
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentCompany?.name ?: "Select Company",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextLight,
                                maxLines = 1
                            )
                            if (isMasterAdmin) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = "More",
                                    tint = TextDim,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        if (isMasterAdmin) {
                            DropdownMenu(
                                expanded = showCompanyDropdown,
                                onDismissRequest = { showCompanyDropdown = false },
                                modifier = Modifier
                                    .background(SpaceCard)
                                    .border(1.dp, SpaceBorder)
                            ) {
                                companies.forEach { comp ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                CompanyLogoView(
                                                    logo = comp.logoUrl.ifEmpty { "⚡" },
                                                    size = 22.dp,
                                                    shape = CircleShape
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        text = comp.name,
                                                        color = if (comp.id == currentCompany?.id) CosmicViolet else TextWhite,
                                                        fontWeight = if (comp.id == currentCompany?.id) FontWeight.Bold else FontWeight.Normal,
                                                        fontSize = 13.sp
                                                    )
                                                    if (comp.loginId.isNotEmpty()) {
                                                        Text(
                                                            text = "ID: ${comp.loginId}",
                                                            color = TextDim,
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        onClick = {
                                            viewModel.selectCompany(comp.id)
                                            showCompanyDropdown = false
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.VpnKey, contentDescription = "Security", tint = CosmicCyan, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Manage Company IDs & Passwords", color = CosmicCyan, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                    },
                                    onClick = {
                                        showCompanyDropdown = false
                                        showMasterCredentialsDialog = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicIndigo, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Edit Active Company", color = CosmicIndigo, fontSize = 13.sp)
                                        }
                                    },
                                    onClick = {
                                        showCompanyDropdown = false
                                        companyToEdit = currentCompany
                                        showCompanyModal = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Add, contentDescription = "Add", tint = CosmicEmerald, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("+ Register New Company", color = CosmicEmerald, fontSize = 13.sp)
                                        }
                                    },
                                    onClick = {
                                        showCompanyDropdown = false
                                        companyToEdit = null
                                        showCompanyModal = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = CosmicRose, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Log Out Admin", color = CosmicRose, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                    },
                                    onClick = {
                                        showCompanyDropdown = false
                                        showLogoutConfirmationDialog = true
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Explicit Logout Button for Both Master & User Logins
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CosmicRose.copy(alpha = 0.15f))
                            .border(1.dp, CosmicRose.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable { showLogoutConfirmationDialog = true }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("btn_logout_topbar")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Log Out",
                            tint = CosmicRose,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Logout",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicRose
                        )
                    }
                }
            }

            // 2. Stage Navigation Horizontal Scroll Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpaceBlack)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MatrixModule.values().forEach { module ->
                    val isSelected = selectedModule == module
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) CosmicViolet else SpaceCard)
                            .border(1.dp, if (isSelected) CosmicViolet else SpaceBorder, RoundedCornerShape(10.dp))
                            .clickable { viewModel.selectModule(module) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .testTag("nav_tab_${module.name}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = module.stage,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) TextWhite else CosmicIndigo
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = module.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) TextWhite else TextLight
                            )
                        }
                    }
                }
            }

            // 3. Search Bar & Export CSV Action (Shown in module views)
            if (selectedModule != MatrixModule.OVERVIEW) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search records...", color = TextDark, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = TextDim, modifier = Modifier.size(18.dp)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextDim, modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = SpaceCard,
                            unfocusedContainerColor = SpaceCard,
                            focusedBorderColor = CosmicViolet,
                            unfocusedBorderColor = SpaceBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Print / Excel Action Button for active category
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CosmicCyan.copy(alpha = 0.15f))
                            .border(1.dp, CosmicCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .clickable { printExportTargetModule = selectedModule }
                            .padding(horizontal = 12.dp)
                            .testTag("btn_print_excel_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print / Export Excel",
                            tint = CosmicCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Print / Excel",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicCyan
                        )
                    }
                }
            }

            // 4. Main Body Content Routed by Active Module
            Box(modifier = Modifier.weight(1f)) {
                when (selectedModule) {
                    MatrixModule.OVERVIEW -> {
                        OverviewDashboard(
                            telecallingList = telecallingRecords,
                            salesDvrList = salesDvrRecords,
                            customerList = customerRecords,
                            bankPendencyList = bankPendencyRecords,
                            bankingDvrList = bankingDvrRecords,
                            employeeList = employeeRecords,
                            franchiseeList = franchiseeRecords,
                            onNavigateModule = { viewModel.selectModule(it) },
                            isMasterAdmin = isMasterAdmin,
                            currentRole = currentRole,
                            companies = companies,
                            onOpenCompanyAccessManager = { showMasterCredentialsDialog = true },
                            onLogout = { showLogoutConfirmationDialog = true },
                            onPrintSummary = { printExportTargetModule = MatrixModule.OVERVIEW }
                        )
                    }
                    MatrixModule.TELECALLING -> {
                        TelecallingListView(
                            records = filteredTelecalling,
                            onEdit = {
                                telecallingToEdit = it
                                showTelecallingDialog = true
                            },
                            onDelete = { viewModel.deleteTelecalling(it) }
                        )
                    }
                    MatrixModule.SALES_DVR -> {
                        SalesDvrListView(
                            records = filteredSalesDvr,
                            onEdit = {
                                salesDvrToEdit = it
                                showSalesDvrDialog = true
                            },
                            onDelete = { viewModel.deleteSalesDvr(it) }
                        )
                    }
                    MatrixModule.CUSTOMERS -> {
                        CustomerListView(
                            records = filteredCustomers,
                            onEdit = {
                                customerToEdit = it
                                showCustomerDialog = true
                            },
                            onDelete = { viewModel.deleteCustomer(it) }
                        )
                    }
                    MatrixModule.BANK_PENDENCY -> {
                        BankPendencyListView(
                            records = filteredBankPendency,
                            onEdit = {
                                bankPendencyToEdit = it
                                showBankPendencyDialog = true
                            },
                            onDelete = { viewModel.deleteBankPendency(it) }
                        )
                    }
                    MatrixModule.BANKING_DVR -> {
                        BankingDvrListView(
                            records = filteredBankingDvr,
                            onEdit = {
                                bankingDvrToEdit = it
                                showBankingDvrDialog = true
                            },
                            onDelete = { viewModel.deleteBankingDvr(it) }
                        )
                    }
                    MatrixModule.EMPLOYEES -> {
                        EmployeeListView(
                            records = filteredEmployees,
                            onEdit = {
                                employeeToEdit = it
                                showEmployeeDialog = true
                            },
                            onDelete = { viewModel.deleteEmployee(it) }
                        )
                    }
                    MatrixModule.FRANCHISEES -> {
                        FranchiseeListView(
                            records = filteredFranchisees,
                            onEdit = {
                                franchiseeToEdit = it
                                showFranchiseeDialog = true
                            },
                            onDelete = { viewModel.deleteFranchisee(it) }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (showCompanyModal) {
        CompanyDialog(
            company = companyToEdit,
            onDismiss = { showCompanyModal = false },
            onSave = {
                viewModel.saveCompany(it)
                showCompanyModal = false
            }
        )
    }

    if (showTelecallingDialog) {
        TelecallingDialog(
            companyId = activeCompanyId,
            record = telecallingToEdit,
            onDismiss = { showTelecallingDialog = false },
            onSave = {
                viewModel.saveTelecalling(it)
                showTelecallingDialog = false
            }
        )
    }

    if (showSalesDvrDialog) {
        SalesDvrDialog(
            companyId = activeCompanyId,
            record = salesDvrToEdit,
            onDismiss = { showSalesDvrDialog = false },
            onSave = {
                viewModel.saveSalesDvr(it)
                showSalesDvrDialog = false
            }
        )
    }

    if (showCustomerDialog) {
        CustomerDialog(
            companyId = activeCompanyId,
            record = customerToEdit,
            onDismiss = { showCustomerDialog = false },
            onSave = {
                viewModel.saveCustomer(it)
                showCustomerDialog = false
            }
        )
    }

    if (showBankPendencyDialog) {
        BankPendencyDialog(
            companyId = activeCompanyId,
            record = bankPendencyToEdit,
            onDismiss = { showBankPendencyDialog = false },
            onSave = {
                viewModel.saveBankPendency(it)
                showBankPendencyDialog = false
            }
        )
    }

    if (showBankingDvrDialog) {
        BankingDvrDialog(
            companyId = activeCompanyId,
            record = bankingDvrToEdit,
            onDismiss = { showBankingDvrDialog = false },
            onSave = {
                viewModel.saveBankingDvr(it)
                showBankingDvrDialog = false
            }
        )
    }

    if (showEmployeeDialog) {
        EmployeeDialog(
            companyId = activeCompanyId,
            record = employeeToEdit,
            onDismiss = { showEmployeeDialog = false },
            onSave = {
                viewModel.saveEmployee(it)
                showEmployeeDialog = false
            }
        )
    }

    if (showFranchiseeDialog) {
        FranchiseeDialog(
            companyId = activeCompanyId,
            record = franchiseeToEdit,
            onDismiss = { showFranchiseeDialog = false },
            onSave = {
                viewModel.saveFranchisee(it)
                showFranchiseeDialog = false
            }
        )
    }

    if (showMasterCredentialsDialog) {
        MasterAccessCredentialsDialog(
            companies = companies,
            onDismiss = { showMasterCredentialsDialog = false },
            onSaveCredentials = { compId, newLoginId, newPass ->
                viewModel.updateCompanyCredentials(compId, newLoginId, newPass)
            },
            onEditCompanyProfile = { comp ->
                showMasterCredentialsDialog = false
                companyToEdit = comp
                showCompanyModal = true
            },
            onAddNewCompany = {
                showMasterCredentialsDialog = false
                companyToEdit = null
                showCompanyModal = true
            }
        )
    }

    if (showLogoutConfirmationDialog) {
        LogoutConfirmationDialog(
            currentRole = currentRole,
            onDismiss = { showLogoutConfirmationDialog = false },
            onConfirmLogout = {
                showLogoutConfirmationDialog = false
                viewModel.lockMatrix()
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    printExportTargetModule?.let { targetModule ->
        val compName = currentCompany?.name ?: "StarLink TLS123"
        val headers: List<String>
        val rows: List<List<String>>
        val count: Int
        val summaryMetrics: Map<String, String>

        when (targetModule) {
            MatrixModule.OVERVIEW -> {
                val pair = PrintAndExcelUtils.getCustomerData(customerRecords)
                headers = pair.first
                rows = pair.second
                count = customerRecords.size
                summaryMetrics = mapOf(
                    "Total Capacity Installed" to FormatUtils.formatKw(customerRecords.sumOf { it.plantCapacity }),
                    "Total Installations" to "${customerRecords.size} Units / Sites",
                    "Total Money Made" to FormatUtils.formatCurrency(customerRecords.sumOf { it.totalReceived }),
                    "Total Pending Balance" to FormatUtils.formatCurrency(customerRecords.sumOf { it.totalProjectCost - it.totalReceived })
                )
            }
            MatrixModule.TELECALLING -> {
                val pair = PrintAndExcelUtils.getTelecallingData(filteredTelecalling)
                headers = pair.first
                rows = pair.second
                count = filteredTelecalling.size
                summaryMetrics = emptyMap()
            }
            MatrixModule.SALES_DVR -> {
                val pair = PrintAndExcelUtils.getSalesDvrData(filteredSalesDvr)
                headers = pair.first
                rows = pair.second
                count = filteredSalesDvr.size
                summaryMetrics = emptyMap()
            }
            MatrixModule.CUSTOMERS -> {
                val pair = PrintAndExcelUtils.getCustomerData(filteredCustomers)
                headers = pair.first
                rows = pair.second
                count = filteredCustomers.size
                summaryMetrics = mapOf(
                    "Total kW Installed" to FormatUtils.formatKw(filteredCustomers.sumOf { it.plantCapacity }),
                    "Total Installations" to "${filteredCustomers.size} Sites",
                    "Total Money Made" to FormatUtils.formatCurrency(filteredCustomers.sumOf { it.totalReceived }),
                    "Pending Balance" to FormatUtils.formatCurrency(filteredCustomers.sumOf { it.totalProjectCost - it.totalReceived })
                )
            }
            MatrixModule.BANK_PENDENCY -> {
                val pair = PrintAndExcelUtils.getBankPendencyData(filteredBankPendency)
                headers = pair.first
                rows = pair.second
                count = filteredBankPendency.size
                summaryMetrics = emptyMap()
            }
            MatrixModule.BANKING_DVR -> {
                val pair = PrintAndExcelUtils.getBankingDvrData(filteredBankingDvr)
                headers = pair.first
                rows = pair.second
                count = filteredBankingDvr.size
                summaryMetrics = emptyMap()
            }
            MatrixModule.EMPLOYEES -> {
                val pair = PrintAndExcelUtils.getEmployeeData(filteredEmployees)
                headers = pair.first
                rows = pair.second
                count = filteredEmployees.size
                summaryMetrics = emptyMap()
            }
            MatrixModule.FRANCHISEES -> {
                val pair = PrintAndExcelUtils.getFranchiseeData(filteredFranchisees)
                headers = pair.first
                rows = pair.second
                count = filteredFranchisees.size
                summaryMetrics = emptyMap()
            }
        }

        PrintExportDialog(
            categoryTitle = targetModule.title,
            recordCount = count,
            onDismiss = { printExportTargetModule = null },
            onExportExcel = {
                PrintAndExcelUtils.exportToExcel(
                    context = context,
                    baseFileName = "${targetModule.title.replace(" ", "_")}_${compName.replace(" ", "_")}",
                    headers = headers,
                    rows = rows,
                    companyName = compName
                )
            },
            onPrintDocument = {
                PrintAndExcelUtils.printTableDocument(
                    context = context,
                    documentTitle = "${targetModule.title} - $compName",
                    companyName = compName,
                    headers = headers,
                    rows = rows,
                    summaryMetrics = summaryMetrics
                )
            }
        )
    }
}
