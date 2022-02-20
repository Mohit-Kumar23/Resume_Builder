package com.mohit.resumebuilder

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.security.Permission
import java.util.jar.Manifest

class Splash_Screen : AppCompatActivity() {

    companion object{
        private const val STORAGE_CODE = 100;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__screen)

        checkPermission()
    }

    fun checkPermission()
    {
        if((ContextCompat.checkSelfPermission(this@Splash_Screen,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this@Splash_Screen,android.Manifest.permission.WRITE_EXTERNAL_STORAGE))!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@Splash_Screen,arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_CODE)
        }
        else
        {
            intentFuntion()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_CODE)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                intentFuntion()
            }
            else{
                checkPermission()
            }
        }

    }

    fun intentFuntion()
    {
        var mHandler = Handler()
        mHandler.postDelayed({
            var intent = Intent(this@Splash_Screen,MainActivity::class.java)
            startActivity(intent)
        },2000)
    }
}