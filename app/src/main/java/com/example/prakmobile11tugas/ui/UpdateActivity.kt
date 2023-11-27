package com.example.prakmobile11tugas.ui
// UpdateActivity.kt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.prakmobile11tugas.R
import com.example.prakmobile11tugas.database.Pengaduan
import com.google.firebase.firestore.FirebaseFirestore

class UpdateActivity : AppCompatActivity() {

    private lateinit var edtUpdatedJudul: EditText
    private lateinit var edtUpdatedDeskripsi: EditText
    private lateinit var edtUpdatedTanggal: EditText
    private lateinit var btnUpdate: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firestore.collection("pengaduan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        edtUpdatedJudul = findViewById(R.id.edtUpdatedJudul)
        edtUpdatedDeskripsi = findViewById(R.id.edtUpdatedDeskripsi)
        edtUpdatedTanggal = findViewById(R.id.edtUpdatedTanggal)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Retrieve data from the intent
        val pengaduanJudul = intent.getStringExtra("pengaduan_judul")
        val pengaduanDeskripsi = intent.getStringExtra("pengaduan_deskripsi")
        val pengaduanTanggal = intent.getStringExtra("pengaduan_tanggal")
        val pengaduanId = intent.getStringExtra("pengaduan_id")

        // Set the retrieved data to the EditText fields
        edtUpdatedJudul.setText(pengaduanJudul)
        edtUpdatedDeskripsi.setText(pengaduanDeskripsi)
        edtUpdatedTanggal.setText(pengaduanTanggal)

        btnUpdate.setOnClickListener {
            // Get updated values from EditText fields
            val updatedJudul = edtUpdatedJudul.text.toString()
            val updatedDeskripsi = edtUpdatedDeskripsi.text.toString()
            val updatedTanggal = edtUpdatedTanggal.text.toString()

            // Update the data in Firebase Firestore
            updatePengaduan(pengaduanId, updatedJudul, updatedDeskripsi, updatedTanggal)
        }
    }

    private fun updatePengaduan(id: String?, judul: String, deskripsi: String, tanggal: String) {
        if (id != null) {
            val updatedPengaduan = Pengaduan(id, judul, deskripsi, tanggal)

            pengaduanCollectionRef.document(id)
                .set(updatedPengaduan)
                .addOnSuccessListener {
                    Log.d("Update Activity", "Pengaduan successfully Updated!")
                    navigateToListActivity()
                }
                .addOnFailureListener {e->
                    Log.e("UpdateActivity", "Error Updating pengaduan", e)
                }
        }
    }
    private fun navigateToListActivity() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
