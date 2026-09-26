package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CalculationRecord
import com.example.data.model.ResultRowItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class HistoryRepository(private val context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("ca_calculator_history", Context.MODE_PRIVATE)

  private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  private val listType = Types.newParameterizedType(List::class.java, CalculationRecord::class.java)
  private val listAdapter = moshi.adapter<List<CalculationRecord>>(listType)

  private val _historyFlow = MutableStateFlow<List<CalculationRecord>>(loadHistoryFromPrefs())
  val historyFlow: StateFlow<List<CalculationRecord>> = _historyFlow.asStateFlow()

  private val _lastCalculation = MutableStateFlow<CalculationRecord?>(loadHistoryFromPrefs().firstOrNull())
  val lastCalculation: StateFlow<CalculationRecord?> = _lastCalculation.asStateFlow()

  private fun loadHistoryFromPrefs(): List<CalculationRecord> {
    val json = prefs.getString("history_json", null) ?: return emptyList()
    return try {
      listAdapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
      emptyList()
    }
  }

  private fun saveHistoryToPrefs(records: List<CalculationRecord>) {
    val json = listAdapter.toJson(records)
    prefs.edit().putString("history_json", json).apply()
    _historyFlow.value = records
    _lastCalculation.value = records.firstOrNull()
  }

  fun saveCalculation(
    calculatorId: String,
    calculatorTitle: String,
    moduleTitle: String,
    inputSummary: String,
    resultSummary: String,
    detailedRows: List<ResultRowItem>,
    userId: String? = null,
    userEmail: String? = null
  ): CalculationRecord {
    val newRecord =
      CalculationRecord(
        id = UUID.randomUUID().toString(),
        calculatorId = calculatorId,
        calculatorTitle = calculatorTitle,
        moduleTitle = moduleTitle,
        inputSummary = inputSummary,
        resultSummary = resultSummary,
        timestampMillis = System.currentTimeMillis(),
        detailedRows = detailedRows,
        userId = userId,
        userEmail = userEmail
      )
    val current = loadHistoryFromPrefs().toMutableList()
    current.add(0, newRecord)
    // Keep max 2000 calculation records so history is saved permanently
    val trimmed = if (current.size > 2000) current.take(2000) else current
    saveHistoryToPrefs(trimmed)
    return newRecord
  }

  fun getHistoryForUser(userId: String?): List<CalculationRecord> {
    val all = _historyFlow.value
    if (userId == null) return all
    return all.filter { it.userId == userId || it.userId == null }
  }

  fun deleteRecord(id: String) {
    val current = loadHistoryFromPrefs().filter { it.id != id }
    saveHistoryToPrefs(current)
  }

  fun clearAllHistory() {
    saveHistoryToPrefs(emptyList())
  }
}
