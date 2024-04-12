package com.example.wifiaccess

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.ScanResult


class BroadcastWifi (context: Context){
    var wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

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
        val result = wifiManager.scanResults
    }
    fun scanFailure(){
        val result = wifiManager.scanResults
    }
}