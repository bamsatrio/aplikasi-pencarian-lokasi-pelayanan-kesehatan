package com.bams.pk.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.bams.pk.R
import com.bams.pk.adapter.MainAdapter
import com.bams.pk.data.model.ModelResults
import com.bams.pk.viewmodel.ListResultViewModel
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*

class HospitalActivity : AppCompatActivity() {

   lateinit var simpleLocation: SimpleLocation
   lateinit var strLokasi: String
   lateinit var progressDialog: ProgressDialog
   lateinit var mainAdapter: MainAdapter
   lateinit var listResultViewModel: ListResultViewModel
   var strLatitude = 0.0
   var strLongitude = 0.0

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_location)

      setSupportActionBar(toolbar)
      assert(supportActionBar != null)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowTitleEnabled(false)

      progressDialog = ProgressDialog(this)
      progressDialog.setTitle("Mohon tunggu...")
      progressDialog.setCancelable(false)
      progressDialog.setMessage("sedang menampilkan lokasi")

      //set library location
      simpleLocation = SimpleLocation(this)
      if (!simpleLocation.hasLocationEnabled()) {
         SimpleLocation.openSettings(this)
      }

      //get location
      strLatitude = simpleLocation.latitude
      strLongitude = simpleLocation.longitude

      //set location lat long
      strLokasi = "$strLatitude,$strLongitude"

      //set title
      tvTitle.setText("Rumah Sakit Terdekat")

      //set data adapter
      mainAdapter = MainAdapter(this)
      rvListResult.setLayoutManager(LinearLayoutManager(this))
      rvListResult.setAdapter(mainAdapter)
      rvListResult.setHasFixedSize(true)

      //viewmodel
      listResultViewModel = ViewModelProvider(this, NewInstanceFactory()).get(ListResultViewModel::class.java)
      listResultViewModel.setHospitalLocation(strLokasi)
      progressDialog.dismiss()
      listResultViewModel.getHospitalLocation().observe(this, { modelResults: ArrayList<ModelResults> ->
         if (modelResults.size != 0) {
            mainAdapter.setResultAdapter(modelResults)
            progressDialog.dismiss()
         } else {
            Toast.makeText(this, "Oops, lokasi Rumah Sakit tidak ditemukan!", Toast.LENGTH_SHORT).show()
         }
      })
   }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
      if (item.itemId == android.R.id.home) {
         finish()
         return true
      }
      return super.onOptionsItemSelected(item)
   }

}