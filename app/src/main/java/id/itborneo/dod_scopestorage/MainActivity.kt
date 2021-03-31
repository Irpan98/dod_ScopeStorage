package id.itborneo.dod_scopestorage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import id.itborneo.dod_scopestorage.mediastore.MediaStoreActivity
import id.itborneo.dod_scopestorage.saf.StorageAccessFrameworkActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_app_2).setOnClickListener {
            val intent = Intent(this, MediaStoreActivity::class.java)
            startActivity(intent)

        }

        findViewById<Button>(R.id.btn_app_3).setOnClickListener {
            val intent = Intent(this, StorageAccessFrameworkActivity::class.java)
            startActivity(intent)
        }
    }


}