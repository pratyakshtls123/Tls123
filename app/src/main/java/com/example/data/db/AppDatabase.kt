package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CompanyEntity
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CompanyEntity::class,
        TelecallingRecord::class,
        SalesDvrRecord::class,
        CustomerRecord::class,
        BankPendencyRecord::class,
        BankingDvrRecord::class,
        EmployeeRecord::class,
        FranchiseeRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    abstract fun telecallingDao(): TelecallingDao
    abstract fun salesDvrDao(): SalesDvrDao
    abstract fun customerDao(): CustomerDao
    abstract fun bankPendencyDao(): BankPendencyDao
    abstract fun bankingDvrDao(): BankingDvrDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun franchiseeDao(): FranchiseeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "starlink_tls123_matrix.db"
                ).addCallback(DatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            val defaultCompanyId = "comp_neelanjali_01"
            val company = CompanyEntity(
                id = defaultCompanyId,
                name = "Neelanjali Solar Matrix Pvt Ltd",
                logoUrl = "⚡",
                ownerName = "Pratyaksh Hans",
                email = "contact@neelanjalisolar.com",
                phone = "+91 98765 43210",
                address = "Plot 42, Solar Energy Tech Park, Sector 62, Noida, UP"
            )
            db.companyDao().insertCompany(company)

            // Seed Telecalling
            db.telecallingDao().insertRecord(
                TelecallingRecord(
                    id = "lead_01",
                    companyId = defaultCompanyId,
                    callDate = "2026-08-27",
                    callTime = "02:30 PM",
                    prospectName = "Dr. Rajesh Sharma",
                    contactNumber = "+91 98112 34567",
                    address = "B-14, Green Park Extension, Delhi",
                    leadSource = "Meta Ads Campaign",
                    capacityRequirement = "10 kW Rooftop",
                    callStatus = "Interested",
                    remarks = "Doctor with high electricity bill (~₹28,000/mo). Requested site survey quote.",
                    telecallerName = "Pooja Verma",
                    telecallerCode = "TC-104"
                )
            )
            db.telecallingDao().insertRecord(
                TelecallingRecord(
                    id = "lead_02",
                    companyId = defaultCompanyId,
                    callDate = "2026-08-28",
                    callTime = "11:15 AM",
                    prospectName = "Vikram Singhania",
                    contactNumber = "+91 97118 90123",
                    address = "Industrial Area Phase 2, Manesar",
                    leadSource = "Referral from Franchisee",
                    capacityRequirement = "50 kW Commercial",
                    callStatus = "Follow-up Scheduled",
                    remarks = "Wants Net Metering feasibility report and ROI breakdown.",
                    telecallerName = "Amit Kumar",
                    telecallerCode = "TC-102"
                )
            )

            // Seed Sales DVR
            db.salesDvrDao().insertRecord(
                SalesDvrRecord(
                    id = "dvr_01",
                    companyId = defaultCompanyId,
                    visitDate = "2026-08-28",
                    execName = "Rohit Mehra",
                    execCode = "SALES-07",
                    customerName = "Anil Mittal Cold Storage",
                    contactNumber = "+91 94120 77889",
                    locationAddress = "NH-24 Hapur Bypass, Ghaziabad",
                    remarks = "Completed shadow analysis & roof inspection. Client ready for 25kW On-Grid.",
                    leadStatus = "Hot Lead",
                    nextFollowUp = "2026-08-30"
                )
            )

            // Seed Customer Record
            db.customerDao().insertRecord(
                CustomerRecord(
                    id = "cust_01",
                    companyId = defaultCompanyId,
                    customerName = "Kavita Rathi",
                    phone = "+91 99887 76655",
                    email = "kavita.rathi@gmail.com",
                    pan = "ABCDE1234F",
                    siteAddress = "House 89, Sector 15, Gurgaon, Haryana",
                    electricityAccountNumber = "DHBVN-7788990",
                    utilityProvider = "DHBVN",
                    applicationNumber = "SL-APP-2026-881",
                    plantCapacity = 8.0,
                    solarBrand = "Tata Power Mono PERC 550W + Growatt Inverter",
                    setupType = "On-Grid",
                    installationCategory = "Residential",
                    bankName = "State Bank of India",
                    branchName = "Sector 14 Gurgaon",
                    ifsc = "SBIN0004521",
                    accountNumber = "30982348712",
                    paymentMethod = "Finance",
                    registrationStatus = "Subsidy Portal Approved",
                    fileLoginDate = "2026-08-15",
                    paymentStatus = "Partially Paid",
                    totalProjectCost = 420000.0,
                    totalReceived = 250000.0,
                    pendingReason = "Bank loan disbursement balance pending final inspection",
                    leadOwnerName = "Rohit Mehra",
                    leadOwnerCode = "SALES-07"
                )
            )

            // Seed Bank Pendency
            db.bankPendencyDao().insertRecord(
                BankPendencyRecord(
                    id = "bank_01",
                    companyId = defaultCompanyId,
                    customerName = "Kavita Rathi",
                    contactNumber = "+91 99887 76655",
                    applicationNumber = "SL-APP-2026-881",
                    bankName = "State Bank of India",
                    branchName = "Sector 14 Gurgaon",
                    ifsc = "SBIN0004521",
                    handlerName = "Suresh Gupta",
                    handlerCode = "BNK-02",
                    sanctionStatus = "Sanction Letter Issued",
                    pendencyRemarks = "Disbursement scheduled post meter installation report submission."
                )
            )

            // Seed Banking DVR
            db.bankingDvrDao().insertRecord(
                BankingDvrRecord(
                    id = "bdvr_01",
                    companyId = defaultCompanyId,
                    visitDate = "2026-08-27",
                    execName = "Suresh Gupta",
                    execCode = "BNK-02",
                    bankName = "Punjab National Bank",
                    branchName = "Sector 18 Noida",
                    ifsc = "PUNB0182900",
                    customerFileName = "Mittal Textiles 30kW",
                    applicationNumber = "PNB-SOL-9901",
                    bankOfficial = "Mr. Verma (Chief Loan Officer)",
                    remarks = "Submitted technical viability certificate. Sanction promised by Friday."
                )
            )

            // Seed Employees
            db.employeeDao().insertRecord(
                EmployeeRecord(
                    id = "emp_01",
                    companyId = defaultCompanyId,
                    employeeCode = "TLS-E-001",
                    fullName = "Rohit Mehra",
                    contactNumber = "+91 98711 22334",
                    email = "rohit.m@neelanjalisolar.com",
                    address = "Flat 402, Royal Palms, Greater Noida",
                    doj = "2024-03-15",
                    designation = "Senior Sales Manager",
                    bankAccountDetails = "HDFC Bank A/c 5010023419082",
                    emergencyContact = "+91 98711 22335 (Spouse)",
                    agreementNote = "Full-time Executive Agreement 2024-2027"
                )
            )

            // Seed Franchisees
            db.franchiseeDao().insertRecord(
                FranchiseeRecord(
                    id = "fran_01",
                    companyId = defaultCompanyId,
                    franchiseCode = "FRAN-NCR-01",
                    franchiseName = "SunPower Hub Faridabad",
                    ownerName = "Deepak Chaudhary",
                    contactPhone = "+91 99100 44556",
                    email = "deepak@sunpowerhub.in",
                    officeAddress = "SCO 45, Main Market, Sector 16, Faridabad",
                    bankAccountNumber = "ICICI Bank 002105009823",
                    bankName = "ICICI Bank",
                    ifsc = "ICIC0000021",
                    agreementNote = "5-Year Exclusive Regional Franchise Master Agreement"
                )
            )
        }
    }
}
