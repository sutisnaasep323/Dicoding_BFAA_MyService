package com.example.myservice

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {

    // buat ServiceConnection untuk menghubungkan MainActivity dengan MyBoundService
    private var mServiceBound = false
    private lateinit var mBoundService: MyBoundService

    /*
    sebuah listener untuk menerima callback dari ServiceConnetion. Kalau dilihat ada dua callback,
    yakni ketika mulai terhubung dengan kelas service dan juga ketika kelas service sudah terputus
     */
    private val mServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBoundService.MyBinder
            mBoundService= myBinder.getService
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnStartService = findViewById<Button>(R.id.btn_start_service)
        btnStartService.setOnClickListener {
            val mStartService = Intent(this, MyService::class.java)
            startService(mStartService) // karena kita menginginkan sebuah service yang berjalan bukan startActivity
        }

        /*
        melakukan pemrosesan obyek Intent yang dikirimkan dan menjalankan suatu proses yang berjalan di background
         */
        val btnStartJobIntentService = findViewById<Button>(R.id.btn_start_job_intent_service)
        btnStartJobIntentService.setOnClickListener {
            val mStartIntentService = Intent(this, MyJobIntentService::class.java)
            mStartIntentService.putExtra(MyJobIntentService.EXTRA_DURATION,5000L)
            MyJobIntentService.enqueueWork(this, mStartIntentService)

        }

        val btnStartBoundService = findViewById<Button>(R.id.btn_start_bound_service)
        btnStartBoundService.setOnClickListener {
            /*
            1. bindService = memulai mengikat kelas MyBoundService ke kelas MainActivity
            2. mBoundServiceIntent = sebuah intent eksplisit yang digunakan untuk menjalankan komponen dari dalam sebuah aplikasi
            3. mServiceConnection = sebuah ServiceConnection berfungsi sebagai callback dari kelas MyBoundService
            4.  BIND_AUTO_CREATE yang membuat sebuah service jika service tersebut belum aktif
             */
            val mBoundServiceIntent = Intent(this, MyBoundService::class.java)
            bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
        }

        val btnStopBoundService = findViewById<Button>(R.id.btn_stop_bound_service)
        btnStopBoundService.setOnClickListener {
            unbindService(mServiceConnection) // berfungsi untuk melepaskan service dari activity pemanggil. Secara tidak langsung maka ia akan memanggil metode onUnbind yang ada di kelas MyBoundService.
        }
    }

    // tujuan onDestroy adalah ketika aplikasi sudah keluar, service akan berhenti secara otomatis
    /*
    Kode onDestroy() seperti yang dijelaskan di metode sebelumnya, akan memanggil unBindService atau melakukan pelepasan service dari Activity.
    Pemanggilan unbindService di dalam onDestroy ditujukan untuk mencegah memory leaks dari bound services.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mServiceBound){
            unbindService(mServiceConnection)
        }
    }
}