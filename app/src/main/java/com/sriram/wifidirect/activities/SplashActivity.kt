package com.sriram.wifidirect.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.sriram.sampletestapp.util.requestPermissionsCompat
import com.sriram.sampletestapp.util.shouldShowRequestPermissionRationaleCompat
import com.sriram.sampletestapp.util.showSnackbar
import com.sriram.wifidirect.R


class SplashActivity : AppCompatActivity() {
    private lateinit var layout: View
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        readWhatsappData()

    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_STORAGE_READ) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.  Storage Accessible.

                layout.showSnackbar("Storage permission granted", Snackbar.LENGTH_SHORT)
                // startCamera()
            } else {
                //Storage Permission request was denied.

                layout.showSnackbar("Storage access permission denied", Snackbar.LENGTH_SHORT)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun readWhatsappData() =
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //    Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_LONG).show()
                intent= Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
            } else {
                requestCameraPermission()
            }


    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.

            layout.showSnackbar(
                    R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                        arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        PERMISSION_REQUEST_STORAGE_READ

                )
            }

        } else {
            layout.showSnackbar(R.string.camera_permission_not_available, Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),

                    PERMISSION_REQUEST_STORAGE_READ
            )
        }
    }
}
