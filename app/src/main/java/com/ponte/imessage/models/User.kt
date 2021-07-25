package com.ponte.imessage.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize //questo ci fa serializzare l'utente e ci permette di incapsulare uno User in un intent e passarlo ad altre activity
class User(val uid:String, val username:String, val profileImageUrl: String) :Parcelable {
    constructor() : this("", "", "")
}