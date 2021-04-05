package id.itborneo.dod_scopestorage.saf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.dod_scopestorage.saf.FileActivity
import id.itborneo.dod_scopestorage.R

class StorageAccessFrameworkActivity : AppCompatActivity() {

    companion object {
        private const val OPEN_FILE_REQUEST_CODE = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_access_framework)

        findViewById<Button>(R.id.btn_create).setOnClickListener {
            createFile()

        }

        findViewById<Button>(R.id.btn_open).setOnClickListener {
            openFile()
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
            //Adding Read URI permission
            flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivityForResult(intent, OPEN_FILE_REQUEST_CODE)
    }

    private fun createFile() {
        startActivity(Intent(this, FileActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_FILE_REQUEST_CODE) {
                data?.data?.also { documentUri ->

                    //Permission needed if you want to retain access even after reboot
                    contentResolver.takePersistableUriPermission(
                        documentUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    Toast.makeText(this, documentUri.path.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}