package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CompanyEntity
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import kotlinx.coroutines.flow.Flow

class MatrixRepository(private val database: AppDatabase) {

    // Companies
    val allCompanies: Flow<List<CompanyEntity>> = database.companyDao().getAllCompanies()

    suspend fun insertCompany(company: CompanyEntity) = database.companyDao().insertCompany(company)
    suspend fun updateCompany(company: CompanyEntity) = database.companyDao().updateCompany(company)
    suspend fun deleteCompany(company: CompanyEntity) = database.companyDao().deleteCompany(company)

    // Telecalling
    fun getTelecallingRecords(companyId: String): Flow<List<TelecallingRecord>> =
        database.telecallingDao().getRecordsByCompany(companyId)
    suspend fun insertTelecalling(record: TelecallingRecord) = database.telecallingDao().insertRecord(record)
    suspend fun updateTelecalling(record: TelecallingRecord) = database.telecallingDao().updateRecord(record)
    suspend fun deleteTelecalling(record: TelecallingRecord) = database.telecallingDao().deleteRecord(record)

    // Sales DVR
    fun getSalesDvrRecords(companyId: String): Flow<List<SalesDvrRecord>> =
        database.salesDvrDao().getRecordsByCompany(companyId)
    suspend fun insertSalesDvr(record: SalesDvrRecord) = database.salesDvrDao().insertRecord(record)
    suspend fun updateSalesDvr(record: SalesDvrRecord) = database.salesDvrDao().updateRecord(record)
    suspend fun deleteSalesDvr(record: SalesDvrRecord) = database.salesDvrDao().deleteRecord(record)

    // Customers
    fun getCustomerRecords(companyId: String): Flow<List<CustomerRecord>> =
        database.customerDao().getRecordsByCompany(companyId)
    suspend fun insertCustomer(record: CustomerRecord) = database.customerDao().insertRecord(record)
    suspend fun updateCustomer(record: CustomerRecord) = database.customerDao().updateRecord(record)
    suspend fun deleteCustomer(record: CustomerRecord) = database.customerDao().deleteRecord(record)

    // Bank Pendency
    fun getBankPendencyRecords(companyId: String): Flow<List<BankPendencyRecord>> =
        database.bankPendencyDao().getRecordsByCompany(companyId)
    suspend fun insertBankPendency(record: BankPendencyRecord) = database.bankPendencyDao().insertRecord(record)
    suspend fun updateBankPendency(record: BankPendencyRecord) = database.bankPendencyDao().updateRecord(record)
    suspend fun deleteBankPendency(record: BankPendencyRecord) = database.bankPendencyDao().deleteRecord(record)

    // Banking DVR
    fun getBankingDvrRecords(companyId: String): Flow<List<BankingDvrRecord>> =
        database.bankingDvrDao().getRecordsByCompany(companyId)
    suspend fun insertBankingDvr(record: BankingDvrRecord) = database.bankingDvrDao().insertRecord(record)
    suspend fun updateBankingDvr(record: BankingDvrRecord) = database.bankingDvrDao().updateRecord(record)
    suspend fun deleteBankingDvr(record: BankingDvrRecord) = database.bankingDvrDao().deleteRecord(record)

    // Employees
    fun getEmployeeRecords(companyId: String): Flow<List<EmployeeRecord>> =
        database.employeeDao().getRecordsByCompany(companyId)
    suspend fun insertEmployee(record: EmployeeRecord) = database.employeeDao().insertRecord(record)
    suspend fun updateEmployee(record: EmployeeRecord) = database.employeeDao().updateRecord(record)
    suspend fun deleteEmployee(record: EmployeeRecord) = database.employeeDao().deleteRecord(record)

    // Franchisees
    fun getFranchiseeRecords(companyId: String): Flow<List<FranchiseeRecord>> =
        database.franchiseeDao().getRecordsByCompany(companyId)
    suspend fun insertFranchisee(record: FranchiseeRecord) = database.franchiseeDao().insertRecord(record)
    suspend fun updateFranchisee(record: FranchiseeRecord) = database.franchiseeDao().updateRecord(record)
    suspend fun deleteFranchisee(record: FranchiseeRecord) = database.franchiseeDao().deleteRecord(record)
}
