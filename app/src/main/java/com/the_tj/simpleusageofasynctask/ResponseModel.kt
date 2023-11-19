package com.the_tj.simpleusageofasynctask

data class ResponseModel (
    val name: String,
    val family: String,
    val number: String,
    val age: String,
    val characteristics:characteristics
)

data class characteristics (
    val weight:String,
    val hight:String,
    val diet:String
)