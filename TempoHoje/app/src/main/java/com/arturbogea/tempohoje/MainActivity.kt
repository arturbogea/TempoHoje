package com.arturbogea.tempohoje

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arturbogea.tempohoje.constantes.Const
import com.arturbogea.tempohoje.databinding.ActivityMainBinding
import com.arturbogea.tempohoje.model.Main
import com.arturbogea.tempohoje.services.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.tema.setOnCheckedChangeListener { compoundButton, b ->

            if (b){//tema escuro
                binding.layoutPrincipal.setBackgroundColor(Color.parseColor("#000000"))
                binding.containerInfo.setBackgroundResource(R.drawable.container_info_tema_escuro)
                binding.txtTemperatura.setTextColor(Color.parseColor("#FFFFFF"))
                binding.txtPaiscidade.setTextColor(Color.parseColor("#FFFFFF"))
                binding.txtInfo.setTextColor(Color.parseColor("#000000"))
                binding.informacoes1.setTextColor(Color.parseColor("#000000"))
                binding.informacoes2.setTextColor(Color.parseColor("#000000"))
                binding.editBuscarCidade.setTextColor(Color.parseColor("#000000"))

            }else{//tema claro
                binding.layoutPrincipal.setBackgroundColor(Color.parseColor("#396BCB"))
                binding.containerInfo.setBackgroundResource(R.drawable.container_info_tema_claro)
                binding.txtTemperatura.setTextColor(Color.parseColor("#FFFFFF"))
                binding.txtPaiscidade.setTextColor(Color.parseColor("#FFFFFF"))
                binding.txtInfo.setTextColor(Color.parseColor("#FFFFFF"))
                binding.informacoes1.setTextColor(Color.parseColor("#FFFFFF"))
                binding.informacoes2.setTextColor(Color.parseColor("#FFFFFF"))
                binding.editBuscarCidade.setTextColor(Color.parseColor("#000000"))
            }
        }

        binding.btBuscar.setOnClickListener {
            val cidade = binding.editBuscarCidade.text.toString()

            binding.progressbar.visibility = View.VISIBLE

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build()
                .create(Api::class.java)

            retrofit.weatherMap(cidade,Const.API_KEY).enqueue(object : Callback<Main>{
                //aqui a resposta da API
                override fun onResponse(call: Call<Main>, response: Response<Main>) {
                    if (response.isSuccessful){// Se a resposta com o servidor for um sucesso, iremos recuperar cada metodo de forma dinamica e passar aos componentes
                        //criei um metodo para organizar todos os dados
                        respostaServidor(response) //irei tratar tudo dentro desse metodo
                    }else{//caso aconteça alguma falha, irei mostrar uma mensagem de erro.
                        Toast.makeText(applicationContext,"Cidade Invalida",Toast.LENGTH_SHORT).show()
                        binding.progressbar.visibility = View.INVISIBLE
                    }
                }

                //aqui caso aconteça algo grave
                override fun onFailure(call: Call<Main>, t: Throwable) {
                    Toast.makeText(applicationContext,"Erro no servidor!",Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.INVISIBLE
                }

            })
        }

    }


    override fun onResume() {
        super.onResume()

        binding.progressbar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(Api::class.java)

        retrofit.weatherMap("New York",Const.API_KEY).enqueue(object : Callback<Main>{
            //aqui a resposta da API
            override fun onResponse(call: Call<Main>, response: Response<Main>) {
                if (response.isSuccessful){// Se a resposta com o servidor for um sucesso, iremos recuperar cada metodo de forma dinamica e passar aos componentes
                    //criei um metodo para organizar todos os dados
                    respostaServidor(response) //irei tratar tudo dentro desse metodo
                }else{//caso aconteça alguma falha, irei mostrar uma mensagem de erro.
                    Toast.makeText(applicationContext,"Cidade Invalida",Toast.LENGTH_SHORT).show()
                    binding.progressbar.visibility = View.INVISIBLE
                }
            }

            //aqui caso aconteça algo grave
            override fun onFailure(call: Call<Main>, t: Throwable) {
                Toast.makeText(applicationContext,"Erro no servidor!",Toast.LENGTH_SHORT).show()
                binding.progressbar.visibility = View.INVISIBLE
            }

        })
    }

    private fun respostaServidor(response: Response<Main>){
        //primeiro irei recuperar a temperatura atual, min, max e atual. Ela está dentro de um Jsonobjetic
        val main = response.body()!!.main
        val temp = main.get("temp").toString() // aqui criei uma variavel temp, que recebe o main, e irá recuperar, a temperatura que está escrita como temp
        val tempMin = main.get("temp_min").toString() // aqui criei uma variavel tempMin, que recebe o main, e irá recuperar, a temperatura minima que está escrita como temp_min
        val tempMax = main.get("temp_max").toString() // aqui criei uma variavel tempMax, que recebe o main, e irá recuperar, a temperatura maxima que está escrita como temp_max
        val humidity = main.get("humidity").toString() // aqui criei uma variavel humidity, que recebe o main, e irá recuperar, a umidade que está escrita como humidity

        // converter de kelvin para graus celsius - /temperatura de agora menos - 273.15
        val tempCel = (temp.toDouble()- 273.15)
        val tempMinCel = (tempMin.toDouble()- 273.15)
        val tempMaxCel = (tempMax.toDouble()- 273.15)


        //irei recuperar o nome do pais. Ela está dentro de um Jsonobjetic
        val sys = response.body()!!.sys
        val country = sys.get("country").asString // aqui criei uma variavel country que irá recuperar o nome do pais. Está escrito como country. Coloquei asString, pois não quero que apareça as ""

        //aqui irei recuperar os campos que estão dentro de weather
        val weather = response.body()!!.weather
        // o weather é uma lista, colocamos os itens que queremos buscar, quando declaramos nele. Nesse caso irei recuperar 2 itens apenas.
        val main_weather = weather[0].main
        val description_weather = weather[0].description

        // abaixo, vamos fazer a logica, para utilizar as imagens conforme está o tempo no momento

        if(main_weather.equals("Clouds") && description_weather.equals("few clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.flewclouds)
        }else if(main_weather.equals("Clouds") && description_weather.equals("few scattered clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.clouds)
        }else if(main_weather.equals("Clouds") && description_weather.equals("broken clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.brokenclouds)
        }else if(main_weather.equals("Clouds") && description_weather.equals("overcast clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.brokenclouds)
        }else if(main_weather.equals("Clear")&& description_weather.equals("clear sky")){
            binding.imgClima.setBackgroundResource(R.drawable.clearsky)
        }else if (main_weather.equals("Snow")){
            binding.imgClima.setBackgroundResource(R.drawable.snow)
        }else if (main_weather.equals("Rain")){
            binding.imgClima.setBackgroundResource(R.drawable.rain)
        }else if(main_weather.equals("Drizzle")){
            binding.imgClima.setBackgroundResource(R.drawable.rain)
        }else if(main_weather.equals("Thunderstorm")){
            binding.imgClima.setBackgroundResource(R.drawable.trunderstorm)
        }

        // abaixo, vamos traduzir, as informações para português

        val descricaoBr = when(description_weather){
            "clear sky" -> "Céu limpo"
            "few clouds" -> "Poucas nuvens"
            "scattered clouds" -> "Nuvens dispersas"
            "broken clouds" -> "Nuvens quebradas"
            "shower rain" -> "Chuva de banho"
            "rain" -> "Chuva"
            "thunderstorm" -> "Trovoada"
            "snow" -> "Neve"
            else -> "Névoa"

        }

        //aqui irei recuperar o campo da cidade
        val name = response.body()!!.name

        //Abaixo, iremos recuperar os id
        binding.txtTemperatura.setText("${tempCel.toInt()} °C")
        binding.txtPaiscidade.setText("$name - $country")
        binding.informacoes1.setText("Clima \n $descricaoBr \n\n Umidade \n $humidity ")
        binding.informacoes2.setText("Temp. Min \n ${tempMinCel.toInt()} °C \n\n Temp. Max \n ${tempMaxCel.toInt()} °C")

        binding.progressbar.visibility = View.INVISIBLE


    }

}