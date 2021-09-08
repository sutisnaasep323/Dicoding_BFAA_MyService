package com.example.myservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class MyBoundService : Service() {

    companion object {
        private val TAG = MyBoundService::class.java.simpleName
    }

    private val mBinder = MyBinder()
    private val startTime = System.currentTimeMillis()

    // Buat class MyBinder lalu kita warisi dari kelas Binder
    /*
    Kode di bawah adalah kelas yang dipanggil di metode onServiceConnected untuk memanggil kelas service.
    Fungsinya untuk mengikat kelas service. Kelas MyBinder yang diberi turunan kelas Binder,
    mempunyai fungsi untuk melakukan mekanisme pemanggilan prosedur jarak jauh
     */
    internal inner class MyBinder : Binder(){
        val getService: MyBoundService = this@MyBoundService
    }

    // variabel untuk menghitung mundur
    /*
    countdown timer akan berjalan sampai 100.000 milisecond atau 100 detik. Intervalnya setiap 1.000 milisecond atau 1 detik akan menampilkan log.
    Hitungan mundur tersebut berfungsi untuk melihat proses terikatnya kelas MyBoundService ke MainActivity
     */
    private var mTimer: CountDownTimer = object : CountDownTimer(100_000, 1000){
        override fun onTick(l: Long) {
            val elapsedTime = System.currentTimeMillis() - startTime
            Log.d(TAG, "onTick: $elapsedTime")
        }

        override fun onFinish() {
            TODO("Not yet implemented")
        }

    }

    // menerapkan CountTimerDown
    override fun onCreate() { // dipanggil ketika memulai pembentukan kelas MyBoundService.
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }
    override fun onBind(intent: Intent): IBinder? { // service akan berjalan dan diikatkan atau ditempelkan dengan activity pemanggil. Pada metode ini juga, mTimer akan mulai berjalan.
        Log.d(TAG, "onBind: ")
        mTimer.start()
        return mBinder
    }
    /*
    berfungsi untuk melakukan penghapusan kelas MyBoundService dari memori. Jadi setelah service sudah terlepas dari kelas MainActivity, kelas MyBoundService juga terlepas dari memori android.
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
    /*
    berfungsi untuk melepaskan service dari activity pemanggil. Kemudian setelah metode onUnbind dipanggil, maka ia akan memanggil metode onDestroy() di kelas MyBoundService
     */
    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind: ")
        mTimer.cancel()
        return super.onUnbind(intent)
    }
    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind: ")
    }

}