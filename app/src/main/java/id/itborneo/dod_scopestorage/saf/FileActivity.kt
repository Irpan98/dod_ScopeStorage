package id.itborneo.dod_scopestorage.saf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.dod_scopestorage.R
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class FileActivity : AppCompatActivity() {

    companion object {
        private const val CREATE_FILE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            createFile()
        }
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_TITLE, "${findViewById<EditText>(R.id.edit_file_name).text}.pdf")
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
    }

    private fun writeFileContent(uri: Uri?) {
        try {
            val file = uri?.let { this.contentResolver.openFileDescriptor(it, "w") }

            file?.let {
                val fileOutputStream = FileOutputStream(
                    it.fileDescriptor
                )
                val textContent = findViewById<EditText>(R.id.edit_file_name).text.toString()

                fileOutputStream.write(textContent.toByteArray())

                fileOutputStream.close()
                it.close()
            }

        } catch (e: FileNotFoundException) {
            //print logs
        } catch (e: IOException) {
            //print logs
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Write the file content
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                writeFileContent(data.data)
            }

        }
    }
}

