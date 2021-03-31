package id.itborneo.dod_scopestorage

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream

class DownloadToSharedStorageActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_to_shared_storage)

        findViewById<Button>(R.id.btn_download).setOnClickListener {
            downloadImage()

        }

        findViewById<Button>(R.id.btn_show).setOnClickListener {
            loadImage()
        }
    }

    var downloadImageUrl =
        "https://i.imgur.com/yc3CbKN.jpg"

    val fileName = "dod_image_to_shared_storage.jpg"

    private fun downloadImage() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

        askPermissions {
            downloadImageToDownloadFolder()
        }

//        } else {
//            downloadImageToDownloadFolder()
//        }
    }

    fun askPermissions(success: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted
            success()
        }
    }

    private fun downloadImageToDownloadFolder() {
        val mgr = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(downloadImageUrl)
        val request = DownloadManager.Request(
            downloadUri
        )
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
            .setAllowedOverRoaming(false)
            .setDescription("Sample Image Demo New")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "dod_image_to_shared_storage.jpg"
            )

        Toast.makeText(
            applicationContext,
            "Download successfully to ${downloadUri?.path}",
            Toast.LENGTH_LONG
        ).show()

        mgr.enqueue(request)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay!
                    // Download the Image
                    downloadImage()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun loadImage() {
        val image =
            getImageFromInternalStorage(
                this,
                fileName
            )
        Log.d("initUI", image.toString())
        if (image != null) {
            findViewById<ImageView>(R.id.iv_image).setImageBitmap(image)
        }
    }

    private fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
        val directory =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) // NEED TO UPDATE TO PROPER WAY
            } else {
                getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//                ContextCompat.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            }


        val file = File(directory.toString(), imageFileName)
        Log.d("getImages", directory.toString())
        return BitmapFactory.decodeStream(FileInputStream(file))
    }

}