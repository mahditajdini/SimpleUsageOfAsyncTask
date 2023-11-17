package com.the_tj.simpleusageofasynctask

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.the_tj.simpleusageofasynctask.databinding.ActivityMainBinding
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
                val url = URL("https://run.mocky.io/v3/a883d7c6-d399-4d61-bb96-14749c05be0f")
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
            binding.tv.text=result.toString()

            Log.i(Tag,result.toString())

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