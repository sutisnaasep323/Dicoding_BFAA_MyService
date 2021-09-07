package com.example.myservice

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    companion object {
        private const val JOB_ID = 1000
        internal const val EXTRA_DURATION = "extra_duration"
        private val TAG = MyJobIntentService::class.java.simpleName

        fun enqueueWork(contex: Context, intent: Intent){
            enqueueWork(contex, MyJobIntentService::class.java, JOB_ID, intent)
        }

    }

    /*
    Kode di bawah akan dijalankan pada thread terpisah secara asynchronous. Jadi kita tak lagi perlu
    membuat background thread seperti pada service sebelumnya
     */
    override fun onHandleWork(intent: Intent) {
        Log.d(TAG,"onHandleWork: Start...")
        val duration = intent.getLongExtra(EXTRA_DURATION, 0)
        try{
            Thread.sleep(duration)
            Log.d(TAG,"onHandleWork: Finish... ")
        } catch (e: InterruptedException){
            e.printStackTrace()
            Thread.currentThread().interrupt()
        }
    }

    /*
    IntentService tak perlu mematikan dirinya sendiri. Service ini akan berhenti dengan sendirinya ketika sudah selesai menyelesaikan tugasnya
     */

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }
}