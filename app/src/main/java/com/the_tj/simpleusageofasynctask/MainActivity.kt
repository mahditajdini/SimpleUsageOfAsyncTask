package com.the_tj.simpleusageofasynctask

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.the_tj.simpleusageofasynctask.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val Tag="MYTAG"
    private lateinit var name:String
    private lateinit var family:String
    private lateinit var number:String
    private lateinit var age:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CallApi().execute()
    }

    private inner class CallApi() : AsyncTask<Any, Void, String>() {
        private lateinit var customProgressDialog:Dialog

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }
        override fun doInBackground(vararg params: Any?): String {
            var result: String
            var connection: HttpURLConnection? = null
            try {
                //val url = URL("https://run.mocky.io/v3/a883d7c6-d399-4d61-bb96-14749c05be0f")
                val url = URL("https://run.mocky.io/v3/57f4d2c4-fcfb-40d2-98ed-72f031740e8d")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true

                val httpResult: Int = connection.responseCode
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line+"\n")
                        }
                    }catch (e:IOException){
                        e.printStackTrace()
                    }finally {
                        try {
                            inputStream.close()

                        }catch (e:IOException){
                            e.printStackTrace()

                        }
                    }

                    result=stringBuilder.toString()
                }else{
                    result=connection.responseMessage
                }

            }catch (e:SocketTimeoutException){
                result="Connection Time out"
            } finally {
                connection?.disconnect()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            cancelProgressDialog()
            binding.tv.text="raw json: \n"+result.toString()

            val jsonObject=JSONObject(result)
            name=jsonObject.optString("name")
            family=jsonObject.optString("family")
            number=jsonObject.optString("number")
            age=jsonObject.optString("age")
            val characteristics=jsonObject.optJSONObject("characteristics")
            val weight=characteristics.optString("weight")
            val hight=characteristics.optString("hight")
            val diet=characteristics.optString("diet")


            binding.apply {
                tvName.text=name
                tvAge.text=age
                tvNumber.text=number
                tvFamily.text="characteristics: \n$family \n $weight \n $hight \n $diet "
            }

        }

        private fun showProgressDialog(){
            customProgressDialog= Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.custom_progress)
            customProgressDialog.show()
        }
        private fun cancelProgressDialog(){
            customProgressDialog.dismiss()
        }
    }
}