package com.example.prakmobile11tugas.ui
// MainActivity.kt
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.prakmobile11tugas.R
import com.example.prakmobile11tugas.database.Pengaduan
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var edtJudul: EditText
    private lateinit var edtDeskripsi: EditText
    private lateinit var edtTanggal: EditText
    private lateinit var btnSimpan: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firestore.collection("pengaduan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtJudul = findViewById(R.id.edtJudul)
        edtDeskripsi = findViewById(R.id.edtDeskripsi)
        edtTanggal = findViewById(R.id.edtTanggal)
        btnSimpan = findViewById(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            val judul = edtJudul.text.toString()
            val deskripsi = edtDeskripsi.text.toString()
            val tanggal = edtTanggal.text.toString()

            val pengaduan = Pengaduan(judul, deskripsi, tanggal)

            tambahPengaduan(pengaduan)

            // Kembali ke ListActivity setelah menyimpan
            startActivity(ListActivity.createIntent(this))
        }
    }

    private fun tambahPengaduan(pengaduan: Pengaduan) {
        pengaduanCollectionRef.add(pengaduan)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

