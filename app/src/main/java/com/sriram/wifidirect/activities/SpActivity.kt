package com.sriram.wifidirect.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sriram.wifidirect.R

class SpActivity : AppCompatActivity() {
    val motionLayout:MotionLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //checkPermission()


        motionLayout?.setTransitionListener(object : MotionLayout.TransitionListener {

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                intent = Intent(this@SpActivity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
    override fun onResume() {
        super.onResume()
        motionLayout?.startLayoutAnimation()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
    fun checkPermission() {
        if ((ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ),
                    permission)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            permission -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {




                } else {
                    checkPermission()
                }
                return
            }
        }
    }

    companion object {
        const val permission = 1
    }



}