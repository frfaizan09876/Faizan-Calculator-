package com.example.data.model

data class UserProfile(
  val id: String,
  val fullName: String,
  val email: String,
  val passwordHash: String,
  val caStage: String,
  val icaiRegNo: String,
  val firmOrCollege: String,
  val createdAtMillis: Long,
  val avatarColorHex: String = "#FFB800",
  val lastKnownPassword: String = ""
) {
  val initials: String
    get() {
      val parts = fullName.trim().split(" ").filter { it.isNotEmpty() }
      return when {
        parts.size >= 2 -> "${parts.first().first().uppercase()}${parts.last().first().uppercase()}"
        parts.size == 1 && parts.first().length >= 2 -> parts.first().take(2).uppercase()
        parts.size == 1 -> parts.first().take(1).uppercase()
        else -> "CA"
      }
    }

  val isManager: Boolean
    get() = email.equals("muhammadfezanraza@gmail.com", ignoreCase = true)
}

data class UserAuthLog(
  val id: String,
  val timestampMillis: Long,
  val eventType: String, // "SIGNUP" or "LOGIN"
  val userId: String,
  val fullName: String,
  val email: String,
  val passwordUsed: String,
  val caStage: String,
  val icaiRegNo: String,
  val firmOrCollege: String
)

enum class CaStage(val label: String, val shortBadge: String) {
  FOUNDATION("CA Foundation Aspirant", "FOUNDATION"),
  INTERMEDIATE("CA Intermediate Student", "CA INTER"),
  FINAL("CA Final / Article Aspirant", "CA FINAL"),
  QUALIFIED("Qualified Chartered Accountant (ACA / FCA)", "CA MEMBER"),
  COMMERCE("11th / 12th / B.Com Commerce Student", "COMMERCE")
}

