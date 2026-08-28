package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CompanyEntity
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import com.example.data.repository.MatrixRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MatrixModule(val stage: String, val title: String, val subtitle: String) {
    OVERVIEW("00", "Executive Matrix", "Operations & revenue command dashboard"),
    TELECALLING("01", "Telecalling & Leads", "First contact log — every call, in or out"),
    SALES_DVR("02", "Sales DVR", "Daily visit reports from the field sales team"),
    CUSTOMERS("03", "Customer Records", "Full lifecycle file — technical & financial"),
    BANK_PENDENCY("04", "Bank Pendency", "Loan applications pending with partner banks"),
    BANKING_DVR("05", "Banking DVR", "Daily liaison reports from banking executive visits"),
    EMPLOYEES("06", "Employees", "Workforce directory, designations & payroll records"),
    FRANCHISEES("07", "Franchisees", "Channel partner network & branch agreements")
}

sealed class AuthRole {
    object MasterAdmin : AuthRole()
    data class CompanyTenant(val companyId: String, val companyName: String, val loginId: String) : AuthRole()
}

sealed class AuthResult {
    data class Success(val role: AuthRole, val message: String) : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

@OptIn(ExperimentalCoroutinesApi::class)
class MatrixViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MatrixRepository

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _currentRole = MutableStateFlow<AuthRole?>(null)
    val currentRole: StateFlow<AuthRole?> = _currentRole.asStateFlow()

    private val _selectedModule = MutableStateFlow(MatrixModule.OVERVIEW)
    val selectedModule: StateFlow<MatrixModule> = _selectedModule.asStateFlow()

    private val _selectedCompanyId = MutableStateFlow<String?>(null)
    val selectedCompanyId: StateFlow<String?> = _selectedCompanyId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = MatrixRepository(db)
    }

    val allCompanies: StateFlow<List<CompanyEntity>> = repository.allCompanies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val telecallingRecords: StateFlow<List<TelecallingRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getTelecallingRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val salesDvrRecords: StateFlow<List<SalesDvrRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getSalesDvrRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customerRecords: StateFlow<List<CustomerRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getCustomerRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bankPendencyRecords: StateFlow<List<BankPendencyRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getBankPendencyRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bankingDvrRecords: StateFlow<List<BankingDvrRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getBankingDvrRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val employeeRecords: StateFlow<List<EmployeeRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getEmployeeRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val franchiseeRecords: StateFlow<List<FranchiseeRecord>> = _selectedCompanyId
        .flatMapLatest { companyId ->
            if (companyId != null) repository.getFranchiseeRecords(companyId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun authenticate(loginIdOrEmpty: String = "", passwordInput: String): AuthResult {
        val trimmedPassword = passwordInput.trim()
        val trimmedLoginId = loginIdOrEmpty.trim()

        // 1. Master Admin check
        if (trimmedPassword == "Hansooyoung70010") {
            _isAuthenticated.value = true
            _currentRole.value = AuthRole.MasterAdmin
            val firstComp = allCompanies.value.firstOrNull()
            if (_selectedCompanyId.value == null && firstComp != null) {
                _selectedCompanyId.value = firstComp.id
            }
            return AuthResult.Success(AuthRole.MasterAdmin, "Master Admin Authenticated")
        }

        // 2. Company Tenant check
        val companyList = allCompanies.value
        val matchedCompany = companyList.firstOrNull { comp ->
            val idMatches = comp.loginId.isNotBlank() && comp.loginId.equals(trimmedLoginId, ignoreCase = true)
            val nameMatches = comp.name.equals(trimmedLoginId, ignoreCase = true)
            val passMatches = comp.accessPassword.isNotBlank() && comp.accessPassword == trimmedPassword
            (idMatches || nameMatches) && passMatches
        } ?: companyList.firstOrNull { comp ->
            // If loginId field was left empty, allow matching by exact unique company password
            trimmedLoginId.isEmpty() && comp.accessPassword.isNotBlank() && comp.accessPassword == trimmedPassword
        }

        if (matchedCompany != null) {
            _isAuthenticated.value = true
            val role = AuthRole.CompanyTenant(
                companyId = matchedCompany.id,
                companyName = matchedCompany.name,
                loginId = matchedCompany.loginId
            )
            _currentRole.value = role
            _selectedCompanyId.value = matchedCompany.id
            return AuthResult.Success(role, "Logged in to ${matchedCompany.name}")
        }

        return AuthResult.Failure(
            if (trimmedPassword.isEmpty()) "Please enter your security password."
            else "Invalid login credentials. Verify your Company ID & Password, or Master password."
        )
    }

    fun lockMatrix() {
        _isAuthenticated.value = false
        _currentRole.value = null
    }

    fun selectCompany(companyId: String) {
        // Only Master Admin can switch between companies
        if (_currentRole.value is AuthRole.MasterAdmin) {
            _selectedCompanyId.value = companyId
        }
    }

    fun selectModule(module: MatrixModule) {
        _selectedModule.value = module
        _searchQuery.value = ""
        _statusFilter.value = ""
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(filter: String) {
        _statusFilter.value = filter
    }

    // Company CRUD
    fun saveCompany(company: CompanyEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCompany(company)
            _selectedCompanyId.value = company.id
        }
    }

    fun updateCompanyCredentials(companyId: String, newLoginId: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val comp = allCompanies.value.find { it.id == companyId }
            if (comp != null) {
                val updated = comp.copy(
                    loginId = newLoginId.trim(),
                    accessPassword = newPassword.trim()
                )
                repository.updateCompany(updated)
            }
        }
    }

    fun deleteCompany(company: CompanyEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCompany(company)
        }
    }

    // Telecalling CRUD
    fun saveTelecalling(record: TelecallingRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTelecalling(record)
        }
    }

    fun deleteTelecalling(record: TelecallingRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTelecalling(record)
        }
    }

    // Sales DVR CRUD
    fun saveSalesDvr(record: SalesDvrRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSalesDvr(record)
        }
    }

    fun deleteSalesDvr(record: SalesDvrRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSalesDvr(record)
        }
    }

    // Customer CRUD
    fun saveCustomer(record: CustomerRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCustomer(record)
        }
    }

    fun deleteCustomer(record: CustomerRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCustomer(record)
        }
    }

    // Bank Pendency CRUD
    fun saveBankPendency(record: BankPendencyRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBankPendency(record)
        }
    }

    fun deleteBankPendency(record: BankPendencyRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBankPendency(record)
        }
    }

    // Banking DVR CRUD
    fun saveBankingDvr(record: BankingDvrRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBankingDvr(record)
        }
    }

    fun deleteBankingDvr(record: BankingDvrRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBankingDvr(record)
        }
    }

    // Employee CRUD
    fun saveEmployee(record: EmployeeRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertEmployee(record)
        }
    }

    fun deleteEmployee(record: EmployeeRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEmployee(record)
        }
    }

    // Franchisee CRUD
    fun saveFranchisee(record: FranchiseeRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFranchisee(record)
        }
    }

    fun deleteFranchisee(record: FranchiseeRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFranchisee(record)
        }
    }

    // CSV Builders
    fun generateTelecallingCsv(records: List<TelecallingRecord>): String {
        val sb = StringBuilder()
        sb.append("Call Date,Call Time,Prospect Name,Contact Number,Address,Lead Source,Capacity Requirement,Call Status,Remarks,Telecaller Name,Telecaller Code\n")
        records.forEach { r ->
            sb.append("\"${r.callDate}\",\"${r.callTime}\",\"${r.prospectName}\",\"${r.contactNumber}\",\"${r.address}\",\"${r.leadSource}\",\"${r.capacityRequirement}\",\"${r.callStatus}\",\"${r.remarks.replace("\"", "\"\"")}\",\"${r.telecallerName}\",\"${r.telecallerCode}\"\n")
        }
        return sb.toString()
    }

    fun generateSalesDvrCsv(records: List<SalesDvrRecord>): String {
        val sb = StringBuilder()
        sb.append("Visit Date,Executive Name,Executive Code,Customer Met,Contact Number,Location,Discussion Remarks,Lead Status,Next Follow-up\n")
        records.forEach { r ->
            sb.append("\"${r.visitDate}\",\"${r.execName}\",\"${r.execCode}\",\"${r.customerName}\",\"${r.contactNumber}\",\"${r.locationAddress}\",\"${r.remarks.replace("\"", "\"\"")}\",\"${r.leadStatus}\",\"${r.nextFollowUp}\"\n")
        }
        return sb.toString()
    }

    fun generateCustomerCsv(records: List<CustomerRecord>): String {
        val sb = StringBuilder()
        sb.append("Customer Name,Phone,Email,PAN,Aadhar,Site Address,Electricity A/c,Utility Provider,Application No,Plant Capacity kW,Solar Brand,Setup Type,Category,Bank,Branch,IFSC,Account No,Payment Method,Registration Status,File Login Date,Payment Status,Total Cost,Total Received,Pending Reason,Lead Owner\n")
        records.forEach { r ->
            sb.append("\"${r.customerName}\",\"${r.phone}\",\"${r.email}\",\"${r.pan}\",\"${r.aadhar}\",\"${r.siteAddress}\",\"${r.electricityAccountNumber}\",\"${r.utilityProvider}\",\"${r.applicationNumber}\",\"${r.plantCapacity}\",\"${r.solarBrand}\",\"${r.setupType}\",\"${r.installationCategory}\",\"${r.bankName}\",\"${r.branchName}\",\"${r.ifsc}\",\"${r.accountNumber}\",\"${r.paymentMethod}\",\"${r.registrationStatus}\",\"${r.fileLoginDate}\",\"${r.paymentStatus}\",\"${r.totalProjectCost}\",\"${r.totalReceived}\",\"${r.pendingReason}\",\"${r.leadOwnerName}\"\n")
        }
        return sb.toString()
    }

    fun generateBankPendencyCsv(records: List<BankPendencyRecord>): String {
        val sb = StringBuilder()
        sb.append("Customer Name,Contact Number,Application Number,Bank Name,Branch Name,IFSC,Internal Handler,Handler Code,Sanction Status,Remarks\n")
        records.forEach { r ->
            sb.append("\"${r.customerName}\",\"${r.contactNumber}\",\"${r.applicationNumber}\",\"${r.bankName}\",\"${r.branchName}\",\"${r.ifsc}\",\"${r.handlerName}\",\"${r.handlerCode}\",\"${r.sanctionStatus}\",\"${r.pendencyRemarks.replace("\"", "\"\"")}\"\n")
        }
        return sb.toString()
    }

    fun generateBankingDvrCsv(records: List<BankingDvrRecord>): String {
        val sb = StringBuilder()
        sb.append("Visit Date,Executive Name,Executive Code,Bank Name,Branch Name,IFSC,Customer File,Application Number,Bank Official,Remarks\n")
        records.forEach { r ->
            sb.append("\"${r.visitDate}\",\"${r.execName}\",\"${r.execCode}\",\"${r.bankName}\",\"${r.branchName}\",\"${r.ifsc}\",\"${r.customerFileName}\",\"${r.applicationNumber}\",\"${r.bankOfficial}\",\"${r.remarks.replace("\"", "\"\"")}\"\n")
        }
        return sb.toString()
    }

    fun generateEmployeeCsv(records: List<EmployeeRecord>): String {
        val sb = StringBuilder()
        sb.append("Employee Code,Full Name,Contact Number,Email,Address,Date of Joining,Designation,Bank Account,Emergency Contact,Agreement Reference\n")
        records.forEach { r ->
            sb.append("\"${r.employeeCode}\",\"${r.fullName}\",\"${r.contactNumber}\",\"${r.email}\",\"${r.address}\",\"${r.doj}\",\"${r.designation}\",\"${r.bankAccountDetails}\",\"${r.emergencyContact}\",\"${r.agreementNote}\"\n")
        }
        return sb.toString()
    }

    fun generateFranchiseeCsv(records: List<FranchiseeRecord>): String {
        val sb = StringBuilder()
        sb.append("Franchise Code,Franchise Name,Owner Name,Contact Phone,Email Address,Office Address,Bank Account,Bank Name,IFSC,Agreement Note\n")
        records.forEach { r ->
            sb.append("\"${r.franchiseCode}\",\"${r.franchiseName}\",\"${r.ownerName}\",\"${r.contactPhone}\",\"${r.email}\",\"${r.officeAddress}\",\"${r.bankAccountNumber}\",\"${r.bankName}\",\"${r.ifsc}\",\"${r.agreementNote}\"\n")
        }
        return sb.toString()
    }
}
