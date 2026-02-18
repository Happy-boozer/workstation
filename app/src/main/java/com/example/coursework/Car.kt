package com.example.coursework

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "cars")
class Car {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "car_id")
    var id: Int = 0
    @ColumnInfo(name = "user_id")
    var user_id: Int? = null
    @ColumnInfo(name = "sighn")
    var sign: String? = null
    var name: String? = null
    var vin: String? = null


    constructor() {}

    constructor(id: Int, name: String, user_id: Int, vin: String, sign: String) {
        this.id = id
        this.name = name
        this.user_id = user_id
        this.vin = vin
        this.sign = sign

    }
    constructor(name: String, ) {
        this.name = name

    }

}