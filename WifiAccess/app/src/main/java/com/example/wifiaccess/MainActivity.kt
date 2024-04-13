package com.example.wifiaccess

import android.net.wifi.ScanResult
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wifiaccess.ui.theme.WifiAccessTheme


class MainActivity : ComponentActivity() {
    private lateinit var broadcastWifi: BroadcastWifi
    private val _scanResults = mutableStateOf(emptyList<ScanResult>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastWifi = BroadcastWifi(this) { results ->
            _scanResults.value = results
        }
        setContent {
            WifiAccessTheme {
                WifiList(scanResults = _scanResults.value)
            }
        }
        broadcastWifi.startScan()
    }
}

@Composable
fun WifiList(scanResults: List<ScanResult>) {
    if (scanResults.isNotEmpty()) {
        LazyColumn {
            items(scanResults) { result ->
                ItemNetwork(result)
            }
        }
    } else {
        // Mostrar indicador de carga
        CircularProgressIndicator()
    }
}
@Composable
fun ItemNetwork(result: ScanResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Nombre: ${result.SSID}", style = MaterialTheme.typography.headlineLarge)
            Text(text = "Seguridad: ${getSecurityType(result)}")
        }
    }
}
fun getSecurityType(result: ScanResult): String {
    return when {
        result.capabilities.contains("WEP") -> "WEP"
        result.capabilities.contains("WPA") -> "WPA"
        result.capabilities.contains("WPA2") -> "WPA2"
        result.capabilities.contains("WPA3") -> "WPA3"
        else -> "Abierto"
    }
}