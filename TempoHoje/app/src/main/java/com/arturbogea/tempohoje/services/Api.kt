package com.arturbogea.tempohoje.services

import com.arturbogea.tempohoje.model.Main
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?id={city id}&appid={API key}
// no @get precisamos passar 2 parametos, o nome da cidade e a api key
//301d77ffad943a95d4d656d7796f820e - chave da api

interface Api {

    @GET("weather")

    /*
    Criei o metodo weatherMap, ao inves de passar os parametros no Path, iremos utilizar a anotação Query do Retrofit2
    Nesse caso temos um parametro q. Esse parametro q, espera o nome da cidade. Como o parametro da cidade está entre {}, ele é um parametro de consulta da Api.
    Para passar esse valor, é passado atraves do Query, que é um parametro de consulta.
    Essa anotação, ela representa qualquer par de valor de chaves de consulta, enviados juntamente com a solicitação de rede.
    Quando passar um parametro de consulta para a Api, utilizamos o Query
    Diferença entre path e query. Path é um parametro de caminho e Query é um parametro de consulta
    Foi passado 2 Query, onde um é passamos o parametro q = city name e outro que passamos o parametro appid = api key
    */
    fun weatherMap(@Query("q") cityName: String,
                   @Query("appid") api_id: String) : Call<Main>


}