package com.example.myservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {

    companion object {
        internal val TAG = MyService::class.java.simpleName
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    // setelah intent onStartCommand dijalankan
    /*
    disini menjalankan sebuah background process untuk melakukan simulasi proses yang sulit.
    Dan ia berjalan secara asynchronous Kekurangan dari service tipe ini adalah ia tak menyediakan
    background thread diluar ui thread secara default. Jadi tiada cara lainnya selain membuat thread secara sendiri
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service dijalankan...");
        serviceScope.launch {
            delay(3000)
            stopSelf() // memberhentikan atau mematikan MyService dari sistem Android
            Log.d(TAG, "==> Service dihentikan <==")
        }
        /*
        START_STICKY menandakan bahwa bila service tersebut dimatikan oleh sistem Android karena kekurangan memori,
        ia akan diciptakan kembali jika sudah ada memori yang bisa digunakan. Metode onStartCommand() juga akan kembali dijalankan
         */
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "on Destroy: ")
    }
}