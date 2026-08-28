package com.example.util

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CosmicAmber
import com.example.ui.theme.CosmicCyan
import com.example.ui.theme.CosmicEmerald
import com.example.ui.theme.CosmicIndigo
import com.example.ui.theme.CosmicPink
import com.example.ui.theme.CosmicRose
import com.example.ui.theme.CosmicViolet
import com.example.ui.theme.TextDim
import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    fun formatCurrency(amount: Double): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 0
            format.format(amount)
        } catch (e: Exception) {
            "₹" + amount.toLong().toString()
        }
    }

    fun formatKw(kw: Double): String {
        return if (kw >= 1000.0) {
            String.format(Locale.getDefault(), "%.2f MW", kw / 1000.0)
        } else {
            String.format(Locale.getDefault(), "%.2f kW", kw)
        }
    }

    fun getDynamicStatusColor(statusText: String?): Color {
        if (statusText.isNullOrBlank()) return TextDim
        val normalized = statusText.trim().lowercase(Locale.ROOT)
        return when {
            normalized.contains("interest") || normalized.contains("converted") ||
                    normalized.contains("approved") || normalized.contains("disbursed") ||
                    normalized.contains("fully paid") || normalized.contains("active") ||
                    normalized.contains("complete") || normalized.contains("success") ||
                    normalized.contains("sanction") -> CosmicEmerald

            normalized.contains("hot") || normalized.contains("urgent") ||
                    normalized.contains("high") || normalized.contains("reject") ||
                    normalized.contains("cancel") -> CosmicRose

            normalized.contains("warm") || normalized.contains("follow") ||
                    normalized.contains("partial") || normalized.contains("in progress") ||
                    normalized.contains("site visit") || normalized.contains("review") -> CosmicViolet

            normalized.contains("cold") || normalized.contains("doc") ||
                    normalized.contains("pending") || normalized.contains("hold") ||
                    normalized.contains("delay") || normalized.contains("wait") -> CosmicAmber

            normalized.contains("schedule") || normalized.contains("new") ||
                    normalized.contains("lead") || normalized.contains("on-grid") -> CosmicCyan

            else -> {
                // Deterministic neon tint based on hash
                val hash = kotlin.math.abs(statusText.hashCode()) % 6
                when (hash) {
                    0 -> CosmicViolet
                    1 -> CosmicIndigo
                    2 -> CosmicCyan
                    3 -> CosmicEmerald
                    4 -> CosmicAmber
                    else -> CosmicPink
                }
            }
        }
    }

    fun shareCsv(context: Context, fileName: String, csvContent: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, csvContent)
            putExtra(Intent.EXTRA_TITLE, fileName)
            putExtra(Intent.EXTRA_SUBJECT, fileName)
            type = "text/csv"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Export / Share $fileName")
        context.startActivity(shareIntent)
    }
}
