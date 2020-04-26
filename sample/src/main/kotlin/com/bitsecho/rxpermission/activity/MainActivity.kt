package com.bitsecho.rxpermission.activity

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bitsecho.rxpermission.R
import com.bitsecho.rxpermission.requestPermissionOnce
import com.bitsecho.rxpermission.requestRxPermission
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.camera).setOnClickListener {
            //Subscribe permission result by Observable. Manage Observable manually.
            var dis: Disposable? = null
            dis = requestRxPermission(Manifest.permission.CAMERA, Manifest.permission.VIBRATE).subscribe {
                    val grant = if (it.isGrant) "Camera and Vibrate Grant" else "Camera or Vibrate Grant"
                    Toast.makeText(this, grant, Toast.LENGTH_SHORT).show()
                    dis?.dispose()
                }
        }

        findViewById<Button>(R.id.read_storage).setOnClickListener {
            //Trigger permission result once by code block. Briefer usage.
            requestPermissionOnce(Manifest.permission.READ_EXTERNAL_STORAGE) {
                val grant = if (it.isGrant) "Read Storage Grant" else "Read Storage NOT Grant"
                Toast.makeText(this, grant, Toast.LENGTH_SHORT).show()
            }
        }
    }
}