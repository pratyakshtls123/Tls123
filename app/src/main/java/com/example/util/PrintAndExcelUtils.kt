package com.example.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.BankPendencyRecord
import com.example.data.model.BankingDvrRecord
import com.example.data.model.CustomerRecord
import com.example.data.model.EmployeeRecord
import com.example.data.model.FranchiseeRecord
import com.example.data.model.SalesDvrRecord
import com.example.data.model.TelecallingRecord
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrintAndExcelUtils {

    private fun getFormattedTimestamp(): String {
        return SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(Date())
    }

    private fun getFileTimestamp(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    }

    /**
     * Exports data to a CSV/Excel file with UTF-8 BOM so Microsoft Excel and Google Sheets
     * open it with full character, symbol, and formatting fidelity.
     */
    fun exportToExcel(
        context: Context,
        baseFileName: String,
        headers: List<String>,
        rows: List<List<String>>,
        companyName: String = "StarLink TLS123"
    ) {
        try {
            val exportDir = File(context.cacheDir, "excel_exports")
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }

            val sanitizedName = baseFileName.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
            val fileName = "${sanitizedName}_${getFileTimestamp()}.csv"
            val file = File(exportDir, fileName)

            val fos = FileOutputStream(file)
            // Write UTF-8 BOM for MS Excel compatibility
            fos.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))

            val writer = fos.bufferedWriter(Charsets.UTF_8)

            // Header info row
            writer.write("\"Company:\",\"${companyName.replace("\"", "\"\"")}\",\"Generated:\",\"${getFormattedTimestamp()}\"")
            writer.newLine()
            writer.newLine()

            // Column Headers
            val headerLine = headers.joinToString(",") { "\"${it.replace("\"", "\"\"")}\"" }
            writer.write(headerLine)
            writer.newLine()

            // Data Rows
            rows.forEach { row ->
                val rowLine = row.joinToString(",") { "\"${it.replace("\"", "\"\"")}\"" }
                writer.write(rowLine)
                writer.newLine()
            }

            writer.flush()
            writer.close()
            fos.close()

            val authority = "${context.packageName}.fileprovider"
            val contentUri = FileProvider.getUriForFile(context, authority, file)

            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT, "$baseFileName - $companyName")
                putExtra(Intent.EXTRA_TEXT, "Exported Excel file for $baseFileName from StarLink TLS123 ($companyName) generated on ${getFormattedTimestamp()}.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(sendIntent, "Open / Print Excel ($fileName)")
            context.startActivity(chooser)
            Toast.makeText(context, "Excel file ready: $fileName", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to export Excel: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Prints or Saves as PDF via Android Native PrintManager with a styled tabular view
     */
    fun printTableDocument(
        context: Context,
        documentTitle: String,
        companyName: String,
        headers: List<String>,
        rows: List<List<String>>,
        summaryMetrics: Map<String, String> = emptyMap()
    ) {
        try {
            val html = buildPrintableHtml(
                documentTitle = documentTitle,
                companyName = companyName,
                headers = headers,
                rows = rows,
                summaryMetrics = summaryMetrics
            )

            val webView = WebView(context)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                    if (printManager != null) {
                        val printAdapter = webView.createPrintDocumentAdapter(documentTitle)
                        val jobName = "${documentTitle}_${getFileTimestamp()}"
                        val printAttributes = PrintAttributes.Builder()
                            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                            .build()
                        printManager.print(jobName, printAdapter, printAttributes)
                    } else {
                        Toast.makeText(context, "Printing service unavailable on this device", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Cannot initialize print layout: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildPrintableHtml(
        documentTitle: String,
        companyName: String,
        headers: List<String>,
        rows: List<List<String>>,
        summaryMetrics: Map<String, String>
    ): String {
        val metricCardsHtml = if (summaryMetrics.isNotEmpty()) {
            val cards = summaryMetrics.map { (key, value) ->
                """
                <div class="metric-card">
                    <div class="metric-key">$key</div>
                    <div class="metric-val">$value</div>
                </div>
                """.trimIndent()
            }.joinToString("\n")
            """<div class="metrics-container">$cards</div>"""
        } else ""

        val thHtml = headers.joinToString("") { "<th>$it</th>" }

        val tbodyHtml = rows.mapIndexed { idx, row ->
            val rowClass = if (idx % 2 == 0) "even" else "odd"
            val cells = row.joinToString("") { cell ->
                val isNum = cell.startsWith("₹") || cell.endsWith("kW") || cell.toDoubleOrNull() != null
                val alignClass = if (isNum) "align-right" else "align-left"
                "<td class=\"$alignClass\">${escapeHtml(cell)}</td>"
            }
            "<tr class=\"$rowClass\">$cells</tr>"
        }.joinToString("\n")

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>$documentTitle</title>
            <style>
                @page {
                    size: A4 landscape;
                    margin: 12mm 10mm 12mm 10mm;
                }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Arial, sans-serif;
                    color: #111827;
                    background: #ffffff;
                    margin: 0;
                    padding: 10px;
                    font-size: 11px;
                }
                .header-table {
                    width: 100%;
                    border-bottom: 2px solid #4F46E5;
                    padding-bottom: 8px;
                    margin-bottom: 12px;
                }
                .brand-title {
                    font-size: 18px;
                    font-weight: 800;
                    color: #4338CA;
                    letter-spacing: -0.5px;
                }
                .doc-title {
                    font-size: 14px;
                    font-weight: 700;
                    color: #1F2937;
                    margin-top: 2px;
                }
                .meta-text {
                    font-size: 10px;
                    color: #6B7280;
                    text-align: right;
                }
                .badge {
                    display: inline-block;
                    padding: 2px 8px;
                    background: #EEF2FF;
                    color: #4F46E5;
                    font-weight: 700;
                    border-radius: 4px;
                    font-size: 10px;
                }
                .metrics-container {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 10px;
                    margin-bottom: 14px;
                }
                .metric-card {
                    flex: 1;
                    min-width: 140px;
                    background: #F9FAFB;
                    border: 1px solid #E5E7EB;
                    border-radius: 6px;
                    padding: 8px 12px;
                }
                .metric-key {
                    font-size: 9px;
                    font-weight: 600;
                    text-transform: uppercase;
                    color: #6B7280;
                }
                .metric-val {
                    font-size: 14px;
                    font-weight: 800;
                    color: #111827;
                    margin-top: 2px;
                }
                table.data-table {
                    width: 100%;
                    border-collapse: collapse;
                    font-size: 10px;
                    margin-top: 6px;
                }
                table.data-table th {
                    background: #1E1B4B;
                    color: #FFFFFF;
                    padding: 6px 8px;
                    text-align: left;
                    font-weight: 700;
                    font-size: 10px;
                    border: 1px solid #312E81;
                }
                table.data-table td {
                    padding: 5px 8px;
                    border: 1px solid #E5E7EB;
                    font-size: 9.5px;
                }
                table.data-table tr.even {
                    background: #FFFFFF;
                }
                table.data-table tr.odd {
                    background: #F9FAFB;
                }
                .align-right {
                    text-align: right;
                    font-weight: 600;
                }
                .align-left {
                    text-align: left;
                }
                .footer {
                    margin-top: 16px;
                    border-top: 1px solid #E5E7EB;
                    padding-top: 6px;
                    font-size: 9px;
                    color: #9CA3AF;
                    display: flex;
                    justify-content: space-between;
                }
            </style>
        </head>
        <body>
            <table class="header-table">
                <tr>
                    <td style="vertical-align: top;">
                        <div class="brand-title">StarLink TLS123 Solar Energy Matrix</div>
                        <div class="doc-title">$documentTitle &bull; <span class="badge">$companyName</span></div>
                    </td>
                    <td class="meta-text" style="vertical-align: top;">
                        <div><strong>Generated On:</strong> ${getFormattedTimestamp()}</div>
                        <div><strong>Total Records:</strong> ${rows.size}</div>
                        <div><strong>Confidential Business Report</strong></div>
                    </td>
                </tr>
            </table>

            $metricCardsHtml

            <table class="data-table">
                <thead>
                    <tr>$thHtml</tr>
                </thead>
                <tbody>
                    $tbodyHtml
                </tbody>
            </table>

            <div class="footer">
                <div>StarLink TLS123 ERP & Solar Project Automation System</div>
                <div>Authorized Signatory / Verification Printout</div>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }

    // -------------------------------------------------------------------------
    // Category Mappers: Headers & Rows
    // -------------------------------------------------------------------------

    fun getTelecallingData(records: List<TelecallingRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Call Date", "Call Time", "Prospect Name", "Contact", "Address", "Lead Source", "Capacity Req.", "Call Status", "Remarks", "Telecaller", "Code")
        val rows = records.map {
            listOf(it.callDate, it.callTime, it.prospectName, it.contactNumber, it.address, it.leadSource, it.capacityRequirement, it.callStatus, it.remarks, it.telecallerName, it.telecallerCode)
        }
        return headers to rows
    }

    fun getSalesDvrData(records: List<SalesDvrRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Visit Date", "Executive", "Exec Code", "Customer Met", "Contact", "Location", "Discussion Remarks", "Lead Status", "Next Follow-Up")
        val rows = records.map {
            listOf(it.visitDate, it.execName, it.execCode, it.customerName, it.contactNumber, it.locationAddress, it.remarks, it.leadStatus, it.nextFollowUp)
        }
        return headers to rows
    }

    fun getCustomerData(records: List<CustomerRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Customer Name", "Phone", "PAN", "Aadhar", "Site Address", "Electricity A/C", "Plant kW", "Setup", "Category", "Bank", "Account No", "Reg. Status", "Payment Status", "Project Cost", "Total Received", "Pending Balance", "Lead Owner")
        val rows = records.map {
            val pending = it.totalProjectCost - it.totalReceived
            listOf(
                it.customerName,
                it.phone,
                it.pan,
                it.aadhar,
                it.siteAddress,
                it.electricityAccountNumber,
                "${it.plantCapacity} kW",
                it.setupType,
                it.installationCategory,
                it.bankName,
                it.accountNumber,
                it.registrationStatus,
                it.paymentStatus,
                FormatUtils.formatCurrency(it.totalProjectCost),
                FormatUtils.formatCurrency(it.totalReceived),
                FormatUtils.formatCurrency(pending),
                it.leadOwnerName
            )
        }
        return headers to rows
    }

    fun getBankPendencyData(records: List<BankPendencyRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Customer Name", "Contact", "Application No", "Bank Name", "Branch", "IFSC", "Internal Handler", "Code", "Sanction Status", "Remarks")
        val rows = records.map {
            listOf(it.customerName, it.contactNumber, it.applicationNumber, it.bankName, it.branchName, it.ifsc, it.handlerName, it.handlerCode, it.sanctionStatus, it.pendencyRemarks)
        }
        return headers to rows
    }

    fun getBankingDvrData(records: List<BankingDvrRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Visit Date", "Executive", "Exec Code", "Bank Name", "Branch", "IFSC", "Customer File", "Application No", "Bank Official", "Remarks")
        val rows = records.map {
            listOf(it.visitDate, it.execName, it.execCode, it.bankName, it.branchName, it.ifsc, it.customerFileName, it.applicationNumber, it.bankOfficial, it.remarks)
        }
        return headers to rows
    }

    fun getEmployeeData(records: List<EmployeeRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Emp Code", "Full Name", "Contact", "Email", "Address", "DOJ", "Designation", "Bank Account", "Emergency", "Agreement Note")
        val rows = records.map {
            listOf(it.employeeCode, it.fullName, it.contactNumber, it.email, it.address, it.doj, it.designation, it.bankAccountDetails, it.emergencyContact, it.agreementNote)
        }
        return headers to rows
    }

    fun getFranchiseeData(records: List<FranchiseeRecord>): Pair<List<String>, List<List<String>>> {
        val headers = listOf("Franchise Code", "Franchise Name", "Owner Name", "Contact Phone", "Email", "Office Address", "Bank Account", "Bank Name", "IFSC", "Agreement Note")
        val rows = records.map {
            listOf(it.franchiseCode, it.franchiseName, it.ownerName, it.contactPhone, it.email, it.officeAddress, it.bankAccountNumber, it.bankName, it.ifsc, it.agreementNote)
        }
        return headers to rows
    }
}
