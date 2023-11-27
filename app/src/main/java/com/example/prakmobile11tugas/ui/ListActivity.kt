package com.example.prakmobile11tugas.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.prakmobile11tugas.R
import com.example.prakmobile11tugas.database.Pengaduan
import com.google.firebase.firestore.FirebaseFirestore

class ListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var tambahButton: Button
    private lateinit var pengaduanList: MutableList<Pengaduan>
    private lateinit var adapter: ArrayAdapter<Pengaduan>

    private val firestore = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firestore.collection("pengaduan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        listView = findViewById(R.id.listView)
        tambahButton = findViewById(R.id.btnTambah)
        pengaduanList = mutableListOf()

        adapter = ArrayAdapter(
            this@ListActivity,
            android.R.layout.simple_list_item_1,
            pengaduanList
        )
        listView.adapter = adapter

        observePengaduanChanges()

        tambahButton.setOnClickListener {
            // Buka MainActivity untuk menambahkan data
            startActivity(Intent(this, MainActivity::class.java))
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPengaduan = pengaduanList[position]
            // Handle the selected note, you can navigate to UpdateDeleteActivity with the selected note's ID
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("pengaduan_judul", selectedPengaduan.judul)
            intent.putExtra("pengaduan_deskripsi", selectedPengaduan.deskripsi)
            intent.putExtra("pengaduan_tanggal", selectedPengaduan.tanggal)
            intent.putExtra("pengaduan_id", selectedPengaduan.id)
            startActivity(intent)
        }
    }

    private fun observePengaduanChanges() {
        pengaduanCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            val pengaduans = snapshots?.toObjects(Pengaduan::class.java)
            if (pengaduans != null) {
                pengaduanList.clear()
                pengaduanList.addAll(pengaduans)
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent {
            return Intent(context, ListActivity::class.java)
        }
    }
}
