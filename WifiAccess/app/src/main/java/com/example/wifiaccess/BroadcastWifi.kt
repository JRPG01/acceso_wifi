package com.example.wifiaccess

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.ScanResult
import android.os.Handler
import android.util.Log


class BroadcastWifi(context: Context, val scanFinished: (List<ScanResult>) -> Unit) {
    var wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val _scanResults = mutableListOf<ScanResult>()

    val wifiManagerReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success == true){
                scanSuccess()
            }
            else{
                scanFailure()
            }
        }
    }
    init {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)

        context.registerReceiver(wifiManagerReceiver,intentFilter)
    }

    fun startScan(){
        val success = wifiManager.startScan()
        if (!success){
            scanFailure()
        }
    }

    fun scanSuccess(){
        _scanResults.clear()
        _scanResults.addAll(wifiManager.scanResults)
        Log.d("Escaneo de red", "Escaneo exitoso: ${_scanResults.size} redes encontradas")

        // Ejecutar scanFinished en el hilo principal
        val handler = Handler()
        handler.post {
            scanFinished(_scanResults)
        }
    }
    fun getScanResults(): List<ScanResult> = _scanResults
    fun scanFailure(){
        val result = wifiManager.scanResults
        Log.d("Resultado Fallido",result.toString())
    }
}
