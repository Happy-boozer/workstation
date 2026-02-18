package com.example.coursework

import androidx.room.Entity
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "userId")
    var id: Int = 0
    @ColumnInfo(name = "name")
    var name: String? = null
    var phone_number: String? = null
    var password: String? = null


    constructor() {}

    constructor(id: Int, name: String, phone_number: String, password: String) {
        this.id = id
        this.name = name
        this.phone_number = phone_number
        this.password = password

    }
    constructor(phone_number: String, password: String) {
        this.phone_number = phone_number
        this.password = password

    }
}