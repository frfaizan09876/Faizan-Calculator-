package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.repository.AuthRepository
import com.example.data.repository.HistoryRepository
import com.example.ui.navigation.CaAppHost
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private lateinit var historyRepository: HistoryRepository
  private lateinit var authRepository: AuthRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    historyRepository = HistoryRepository(applicationContext)
    authRepository = AuthRepository(applicationContext)

    setContent {
      MyApplicationTheme(dynamicColor = false) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = DeepBlack
        ) {
          CaAppHost(
            historyRepository = historyRepository,
            authRepository = authRepository
          )
        }
      }
    }
  }
}

