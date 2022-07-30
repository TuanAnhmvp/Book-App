package com.example.bookapp.models

import android.util.Log

class ModelCategory {

    var id:String = ""
    var category:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    //empty constructor, required by firebase
    constructor()
    //parameteriezed constructor

     constructor(id: String, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }




}