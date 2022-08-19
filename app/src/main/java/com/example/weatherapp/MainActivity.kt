package com.example.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
   var CITY:String="kanpur"
    lateinit var cityName: String
    val API:String="f50248de56ce0ef5228f0de13c67dbe9"
    lateinit var editText: EditText
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//weatherTask().execute()

button=findViewById(R.id.cityButton)
        editText=findViewById(R.id.cityEdit)
        button.setOnClickListener{
            cityName=editText.text.toString()
            weatherTask().execute()
        }


        }




    inner class weatherTask(): AsyncTask<String,Void,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility=View.VISIBLE
            findViewById<RelativeLayout>(R.id.maincontainer).visibility=View.GONE
            findViewById<TextView>(R.id.errortext).visibility=View.GONE

        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try {
                response= URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e:Exception){
                response=null

         }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj=JSONObject(result)
                val main=jsonObj.getJSONObject("main")
                val sys=jsonObj.getJSONObject("sys")
                val wind=jsonObj.getJSONObject("wind")
                val weather=jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt=jsonObj.getLong("dt")
                val updatedAtText="updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp =main.getString("temp")+"°C"
                val tempMin="Min Temp"+main.getString("temp_min")+"°C"
                val tempMax="Max Temp"+main.getString("temp_max")+"°C"
                val pressure=main.getString("pressure")
                val humidity=main.getString("humidity")
                val sunrise:Long=sys.getLong("sunrise")
                val sunset:Long=sys.getLong("sunset")
                val windSpeed=wind.getString("speed")
                val weatherDescription=weather.getString("description")
                val address=jsonObj.getString("name")+", "+sys.getString("country")


                findViewById<TextView>(R.id.address).text=address
                findViewById<TextView>(R.id.updated_at).text=updatedAtText
                findViewById<TextView>(R.id.status).text= weatherDescription.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                findViewById<TextView>(R.id.temp).text=temp
                findViewById<TextView>(R.id.temp_min).text=tempMin
                findViewById<TextView>(R.id.temp_max).text=tempMax
                findViewById<TextView>(R.id.sunrise).text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(
                    Date(sunrise*1000)
                )
                findViewById<TextView>(R.id.sunset).text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(
                    Date(sunset*1000))
                findViewById<TextView>(R.id.Wind).text=windSpeed
                findViewById<TextView>(R.id.pressure).text=pressure
                findViewById<TextView>(R.id.address).text=address
                findViewById<TextView>(R.id.humidity).text=humidity


                findViewById<ProgressBar>(R.id.loader).visibility=View.GONE
                findViewById<RelativeLayout>(R.id.maincontainer).visibility=View.VISIBLE
            }
            catch (e:Exception){

                //findViewById<ProgressBar>(R.id.loader).visibility=View.GONE
              //findViewById<TextView>(R.id.errortext).visibility=View.VISIBLE
                findViewById<RelativeLayout>(R.id.maincontainer).visibility=View.VISIBLE
            }
        }

    }
}