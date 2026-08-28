package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicPink
import com.example.ui.theme.CosmicRose
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.SpaceBorder
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardElevated
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.MatrixModule
import com.example.util.FormatUtils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OverviewDashboard(
    telecallingList: List<TelecallingRecord>,
    salesDvrList: List<SalesDvrRecord>,
    customerList: List<CustomerRecord>,
    bankPendencyList: List<BankPendencyRecord>,
    bankingDvrList: List<BankingDvrRecord>,
    employeeList: List<EmployeeRecord>,
    franchiseeList: List<FranchiseeRecord>,
    onNavigateModule: (MatrixModule) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalProjectCost = customerList.sumOf { it.totalProjectCost }
    val totalReceived = customerList.sumOf { it.totalReceived }
    val pendingBalance = totalProjectCost - totalReceived

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Financial Pipeline Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            CosmicViolet.copy(alpha = 0.2f),
                            CosmicIndigo.copy(alpha = 0.1f),
                            SpaceCardElevated
                        )
                    )
                )
                .border(1.dp, CosmicViolet.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CosmicViolet.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Matrix Hub",
                                tint = CosmicViolet,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Financial Matrix Summary",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(CosmicEmerald.copy(alpha = 0.15f))
                            .border(1.dp, CosmicEmerald.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE SYNC",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Total Project Value
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total Project Value", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = FormatUtils.formatCurrency(totalProjectCost),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    // Received
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Realized Collections", fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = FormatUtils.formatCurrency(totalReceived),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Pending Balance highlight
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SpaceCard)
                        .border(1.dp, SpaceBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Pending Balance: ", fontSize = 12.sp, color = TextMuted)
                    Text(
                        text = FormatUtils.formatCurrency(pendingBalance),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicAmber
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${customerList.size} Projects",
                        fontSize = 11.sp,
                        color = TextDim
                    )
                }
            }
        }

        // Section Title: Operational Matrix Stages
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Pipeline Stages & Directories",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "7 Modules",
                fontSize = 11.sp,
                color = CosmicIndigo
            )
        }

        // Pipeline Stages Grid
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            StageCard(
                stage = "01",
                title = "Telecalling & Leads",
                subtitle = "First contact calls, source attribution & requirement log",
                count = telecallingList.size,
                icon = Icons.Default.Call,
                accentColor = CosmicViolet,
                onClick = { onNavigateModule(MatrixModule.TELECALLING) },
                testTag = "stage_card_telecalling"
            )
            StageCard(
                stage = "02",
                title = "Sales DVR",
                subtitle = "Field sales visits, feasibility discussion & next follow-ups",
                count = salesDvrList.size,
                icon = Icons.Default.TrendingUp,
                accentColor = CosmicPink,
                onClick = { onNavigateModule(MatrixModule.SALES_DVR) },
                testTag = "stage_card_sales_dvr"
            )
            StageCard(
                stage = "03",
                title = "Customer Records",
                subtitle = "Complete plant kW capacity, payment milestones & portal IDs",
                count = customerList.size,
                icon = Icons.Default.Receipt,
                accentColor = CosmicEmerald,
                onClick = { onNavigateModule(MatrixModule.CUSTOMERS) },
                testTag = "stage_card_customers"
            )
            StageCard(
                stage = "04",
                title = "Bank Pendency",
                subtitle = "Loan sanction stage, branch handler & subsidy liaison files",
                count = bankPendencyList.size,
                icon = Icons.Default.AccountBalance,
                accentColor = CosmicAmber,
                onClick = { onNavigateModule(MatrixModule.BANK_PENDENCY) },
                testTag = "stage_card_bank_pendency"
            )
            StageCard(
                stage = "05",
                title = "Banking DVR",
                subtitle = "Daily bank branch visits and loan officer meeting outcomes",
                count = bankingDvrList.size,
                icon = Icons.Default.Assignment,
                accentColor = CosmicCyan,
                onClick = { onNavigateModule(MatrixModule.BANKING_DVR) },
                testTag = "stage_card_banking_dvr"
            )
            StageCard(
                stage = "06",
                title = "Employees",
                subtitle = "Internal workforce directory, designations & payroll matrix",
                count = employeeList.size,
                icon = Icons.Default.People,
                accentColor = CosmicIndigo,
                onClick = { onNavigateModule(MatrixModule.EMPLOYEES) },
                testTag = "stage_card_employees"
            )
            StageCard(
                stage = "07",
                title = "Franchisees",
                subtitle = "Authorized regional solar hub partners & agreements",
                count = franchiseeList.size,
                icon = Icons.Default.Handshake,
                accentColor = CosmicViolet,
                onClick = { onNavigateModule(MatrixModule.FRANCHISEES) },
                testTag = "stage_card_franchisees"
            )
        }
    }
}

@Composable
private fun StageCard(
    stage: String,
    title: String,
    subtitle: String,
    count: Int,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Stage Number Badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "STAGE $stage",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextMuted,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Count Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(SpaceCardElevated)
                    .border(1.dp, SpaceBorder, RoundedCornerShape(100.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$count",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go",
                tint = TextDim,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
