package com.example.gd10_d_10778

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gd10_d_10778.databinding.ActivityFormAddMahasiswaBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FormAddMahasiswaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFormAddMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            saveData()
        }

        binding.tvTgl.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, dayofMonth ->
                binding.tglView.text = dateToString(year,month,dayofMonth)
            }
            dateDialog(this,datePicker).show()
        }
    }

    fun saveData(){
        with(binding) {
            val nobp = txtNobp.text.toString()
            val nama= txtNama.text.toString()
            val alamat = txtAlamat.text.toString()
            val prodi = txtProdi.text.toString()
            val lahir = tglView.text.toString()


            RClient.instances.createData(nobp,nama,alamat,prodi,lahir).enqueue(object : Callback<ResponseCreate> {
                override fun onResponse(
                    call: Call<ResponseCreate>,
                    response: Response<ResponseCreate>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext,"${response.body()?.pesan}", Toast.LENGTH_LONG).show()
                        finish()
                    }else {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        txtNobp.setError(jsonObj.getString("message"))
                        Toast.makeText(applicationContext,"Maaf sudah ada datanya", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseCreate>, t: Throwable) {
                }

            })

        }
    }

    private fun dateToString(year: Int, month: Int, dayofMonth: Int): String {
        return year.toString()+"-"+(month+1)+"-"+dayofMonth.toString()
    }




    private fun dateDialog(context: Context, datePicker: DatePickerDialog.OnDateSetListener):DatePickerDialog {

        val calender = Calendar.getInstance()
        return DatePickerDialog(
            context, datePicker,
            calender[Calendar.YEAR],
            calender[Calendar.MONTH],
            calender[Calendar.DAY_OF_MONTH],
        )
    }
}