package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CompanyEntity
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyDao {
    @Query("SELECT * FROM companies ORDER BY createdAt ASC")
    fun getAllCompanies(): Flow<List<CompanyEntity>>

    @Query("SELECT * FROM companies WHERE id = :id LIMIT 1")
    suspend fun getCompanyById(id: String): CompanyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: CompanyEntity)

    @Update
    suspend fun updateCompany(company: CompanyEntity)

    @Delete
    suspend fun deleteCompany(company: CompanyEntity)
}

@Dao
interface TelecallingDao {
    @Query("SELECT * FROM telecalling_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<TelecallingRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: TelecallingRecord)

    @Update
    suspend fun updateRecord(record: TelecallingRecord)

    @Delete
    suspend fun deleteRecord(record: TelecallingRecord)
}

@Dao
interface SalesDvrDao {
    @Query("SELECT * FROM sales_dvr_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<SalesDvrRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: SalesDvrRecord)

    @Update
    suspend fun updateRecord(record: SalesDvrRecord)

    @Delete
    suspend fun deleteRecord(record: SalesDvrRecord)
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<CustomerRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: CustomerRecord)

    @Update
    suspend fun updateRecord(record: CustomerRecord)

    @Delete
    suspend fun deleteRecord(record: CustomerRecord)
}

@Dao
interface BankPendencyDao {
    @Query("SELECT * FROM bank_pendency_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<BankPendencyRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: BankPendencyRecord)

    @Update
    suspend fun updateRecord(record: BankPendencyRecord)

    @Delete
    suspend fun deleteRecord(record: BankPendencyRecord)
}

@Dao
interface BankingDvrDao {
    @Query("SELECT * FROM banking_dvr_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<BankingDvrRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: BankingDvrRecord)

    @Update
    suspend fun updateRecord(record: BankingDvrRecord)

    @Delete
    suspend fun deleteRecord(record: BankingDvrRecord)
}

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employee_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<EmployeeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: EmployeeRecord)

    @Update
    suspend fun updateRecord(record: EmployeeRecord)

    @Delete
    suspend fun deleteRecord(record: EmployeeRecord)
}

@Dao
interface FranchiseeDao {
    @Query("SELECT * FROM franchisee_records WHERE companyId = :companyId ORDER BY updatedAt DESC")
    fun getRecordsByCompany(companyId: String): Flow<List<FranchiseeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: FranchiseeRecord)

    @Update
    suspend fun updateRecord(record: FranchiseeRecord)

    @Delete
    suspend fun deleteRecord(record: FranchiseeRecord)
}
