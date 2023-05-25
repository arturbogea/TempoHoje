package com.arturbogea.tempohoje.model

import com.google.gson.JsonObject



//Criei um data classe, pois irei precisar de getter e setter
//Dentro da data class, irei criar algumas propriedades que vão ser associados com os campos do Json
// Esse Json, é um Json object
//criei a classe sys, pois precisarei para recuperar o nome do pais
data class Main (
    /*
    Criei uma variavel, onde herda um JsonObject. Pois não conseguirei acessar diretamente a temperatura  e outras propriedades. Eles estão dentro de um Jsonobject
     */
    val main: JsonObject,
    val sys: JsonObject,
    val weather: List<Weather>,
    val name: String
)