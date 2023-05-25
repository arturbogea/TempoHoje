package com.arturbogea.tempohoje.model
/*
Criei a classe weather, pois ela é uma lista, e para ter acesso as informações do tempo que desejo na classe main, precisa passar pelo weather, que é uma lista
*/
data class Weather (
        val main: String,
        val description: String,
)