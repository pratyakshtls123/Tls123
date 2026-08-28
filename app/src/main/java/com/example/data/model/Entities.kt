package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val logoUrl: String = "",
    val ownerName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "telecalling_records")
data class TelecallingRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val callDate: String = "",
    val callTime: String = "",
    val prospectName: String = "",
    val contactNumber: String = "",
    val address: String = "",
    val leadSource: String = "",
    val capacityRequirement: String = "",
    val callStatus: String = "", // Free text status typed by user
    val remarks: String = "",
    val telecallerName: String = "",
    val telecallerCode: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sales_dvr_records")
data class SalesDvrRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val visitDate: String = "",
    val execName: String = "",
    val execCode: String = "",
    val customerName: String = "",
    val contactNumber: String = "",
    val locationAddress: String = "",
    val remarks: String = "",
    val leadStatus: String = "", // Free text status typed by user
    val nextFollowUp: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "customer_records")
data class CustomerRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val customerName: String = "",
    val phone: String = "",
    val email: String = "",
    val pan: String = "",
    val siteAddress: String = "",
    val electricityAccountNumber: String = "",
    val utilityProvider: String = "",
    val applicationNumber: String = "",
    val plantCapacity: Double = 0.0,
    val solarBrand: String = "",
    val setupType: String = "", // On-Grid, Off-Grid, Hybrid, etc.
    val installationCategory: String = "", // Residential, Commercial, Industrial
    val bankName: String = "",
    val branchName: String = "",
    val ifsc: String = "",
    val accountNumber: String = "",
    val paymentMethod: String = "",
    val registrationStatus: String = "",
    val fileLoginDate: String = "",
    val paymentStatus: String = "", // Free text status typed by user
    val totalProjectCost: Double = 0.0,
    val totalReceived: Double = 0.0,
    val pendingReason: String = "",
    val leadOwnerName: String = "",
    val leadOwnerCode: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bank_pendency_records")
data class BankPendencyRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val customerName: String = "",
    val contactNumber: String = "",
    val applicationNumber: String = "",
    val bankName: String = "",
    val branchName: String = "",
    val ifsc: String = "",
    val handlerName: String = "",
    val handlerCode: String = "",
    val sanctionStatus: String = "", // Free text status typed by user
    val pendencyRemarks: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "banking_dvr_records")
data class BankingDvrRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val visitDate: String = "",
    val execName: String = "",
    val execCode: String = "",
    val bankName: String = "",
    val branchName: String = "",
    val ifsc: String = "",
    val customerFileName: String = "",
    val applicationNumber: String = "",
    val bankOfficial: String = "",
    val remarks: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "employee_records")
data class EmployeeRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val employeeCode: String = "",
    val fullName: String = "",
    val contactNumber: String = "",
    val email: String = "",
    val address: String = "",
    val doj: String = "",
    val designation: String = "",
    val bankAccountDetails: String = "",
    val emergencyContact: String = "",
    val agreementNote: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "franchisee_records")
data class FranchiseeRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val companyId: String,
    val franchiseCode: String = "",
    val franchiseName: String = "",
    val ownerName: String = "",
    val contactPhone: String = "",
    val email: String = "",
    val officeAddress: String = "",
    val bankAccountNumber: String = "",
    val bankName: String = "",
    val ifsc: String = "",
    val agreementNote: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
