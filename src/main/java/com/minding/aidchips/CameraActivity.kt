package com.minding.aidchips

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.webkit.URLUtil
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class CameraActivity : AppCompatActivity()
{
    lateinit var backBtn : ActionBar
    lateinit var toolbar: android.support.v7.widget.Toolbar

    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private val permissionCamera = 1
    private var token = ""
    private var tokenanterior = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        setTitle(R.string.title_camera)
        setupToolbar()
        cameraView = findViewById(R.id.camera_view)
        initQR()
    }

    private fun initQR()
    {

        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        // creo la camara
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1600, 1024)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        // listener de ciclo de vida de la camara
        cameraView!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(this@CameraActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA))
                        requestPermissions(arrayOf(Manifest.permission.CAMERA),
                            permissionCamera)
                    }
                    return
                } else {
                    try {
                        cameraSource!!.start(cameraView!!.holder)
                    } catch (ie: IOException) {
                        Log.e("CAMERA SOURCE", ie.message)
                    }

                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource!!.stop()
            }
        })

        // preparo el detector de QR
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}


            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems

                if (barcodes.size() > 0) {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString()

                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (token != tokenanterior)
                    {

                        // guardamos el ultimo token proceado
                        tokenanterior = token

                        if (URLUtil.isValidUrl(token) && token.contains(Regex("https://aidchips.tk")))
                        {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(token))
                            startActivity(browserIntent)
                        }
                        else
                            wrongQR()

                        Thread(object : Runnable
                        {
                            override fun run() =
                                try {
                                    synchronized(this)
                                    {
                                        wait(3000)
                                        // limpiamos el token
                                        tokenanterior = ""
                                    }
                                }
                                catch (e: InterruptedException)
                                {
                                    Log.e("Error", "Waiting didnt work!!")
                                    e.printStackTrace()
                                }
                        }).start()

                    }
                }
            }
        })

    }

    private fun wrongQR() {
        Toast.makeText(this@CameraActivity, "Solo puedes escanear codigos qr dentro de https://aidchips.tk", Toast.LENGTH_LONG).show()
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    private fun Runnable.wait(time: Long) = (this as Object).wait(time)

    @SuppressLint("PrivateResource")
    private fun setupToolbar()
    {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        backBtn = supportActionBar!!
        backBtn.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        backBtn.setDisplayHomeAsUpEnabled(true)
    }
}
