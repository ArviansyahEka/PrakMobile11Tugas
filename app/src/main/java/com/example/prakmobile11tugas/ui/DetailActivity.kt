package com.example.prakmobile11tugas.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.prakmobile11tugas.R
import com.example.prakmobile11tugas.database.Pengaduan
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {

    private lateinit var judulTextView: TextView
    private lateinit var deskripsiTextView: TextView
    private lateinit var tanggalTextView: TextView

    private lateinit var selectedPengaduan: Pengaduan

    private val firestore = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firestore.collection("pengaduan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        judulTextView = findViewById(R.id.detailsjudul)
        deskripsiTextView = findViewById(R.id.detailsdeskripsi)
        tanggalTextView = findViewById(R.id.detailstanggal)

        // Mendapatkan data dari Intent
        val pengaduanJudul = intent.getStringExtra("pengaduan_judul")
        val pengaduanDeskripsi = intent.getStringExtra("pengaduan_deskripsi")
        val pengaduanTanggal = intent.getStringExtra("pengaduan_tanggal")

        // Log the received data for debugging
        Log.d("DetailActivity", "Received data - judul: $pengaduanJudul, deskripsi: $pengaduanDeskripsi, tanggal: $pengaduanTanggal")

        // Mengatur nilai TextView dengan data yang diterima
        judulTextView.text = pengaduanJudul
        deskripsiTextView.text = pengaduanDeskripsi
        tanggalTextView.text = pengaduanTanggal

        // Initialize selectedPengaduan
        selectedPengaduan = Pengaduan(
            id = intent.getStringExtra("pengaduan_id") ?: "",
            judul = pengaduanJudul ?: "",
            deskripsi = pengaduanDeskripsi ?: "",
            tanggal = pengaduanTanggal ?: ""
        )

        // Add Delete Button Click Listener
        val deleteButton: Button = findViewById(R.id.btnDelete)
        deleteButton.setOnClickListener {
            // Call a method to handle delete logic
            handleDelete()
        }

        val updateButton: Button = findViewById(R.id.btnUpdate)
        updateButton.setOnClickListener {
            // Call a method to handle update logic
            handleUpdate()
        }
    }

    private fun handleUpdate() {
        // Get the data you want to pass to UpdateActivity
        val pengaduanJudul = selectedPengaduan.judul
        val pengaduanDeskripsi = selectedPengaduan.deskripsi
        val pengaduanTanggal = selectedPengaduan.tanggal
        val pengaduanId = selectedPengaduan.id

        // Create an intent to start UpdateActivity and pass the data
        val intent = Intent(this, UpdateActivity::class.java).apply {
            putExtra("pengaduan_judul", pengaduanJudul)
            putExtra("pengaduan_deskripsi", pengaduanDeskripsi)
            putExtra("pengaduan_tanggal", pengaduanTanggal)
            putExtra("pengaduan_id", pengaduanId)
        }

        // Start the UpdateActivity
        startActivity(intent)
    }


    private fun handleDelete() {
        // Show a confirmation dialog before deleting
        AlertDialog.Builder(this)
            .setTitle("Delete Pengaduan")
            .setMessage("Are you sure you want to delete this pengaduan?")
            .setPositiveButton("Yes") { dialog, which ->
                // Get the id of the selected pengaduan
                val pengaduanId = selectedPengaduan.id

                // Delete the pengaduan
                deletePengaduan(pengaduanId)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deletePengaduan(pengaduanId: String) {
        // Check if pengaduanId is not empty
        if (pengaduanId.isNotEmpty()) {
            Log.d("DetailActivity", "Deleting pengaduan with id: $pengaduanId")
            pengaduanCollectionRef.document(pengaduanId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Pengaduan deleted successfully", Toast.LENGTH_SHORT).show()
                    // Finish activity and navigate back to the list
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error deleting pengaduan", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle the case where pengaduanId is empty or invalid
            Toast.makeText(this, "Invalid pengaduanId", Toast.LENGTH_SHORT).show()
        }
    }
}
