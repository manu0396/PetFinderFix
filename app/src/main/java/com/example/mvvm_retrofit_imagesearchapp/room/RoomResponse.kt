package com.example.mvvm_retrofit_imagesearchapp.room

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.util.*

class RoomResponse() :Parcelable {
    @SerializedName("url")
    var url: String? = null
    @SerializedName("user")
    var user: String? = null
    @SerializedName("description")
    var description : String? = null

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        user = parcel.readString()
        description = parcel.readString()
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(user)
        parcel.writeString(description)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<RoomResponse> {
        override fun createFromParcel(parcel: Parcel): RoomResponse {
            return RoomResponse(parcel)
        }
        override fun newArray(size: Int): Array<RoomResponse?> {
            return arrayOfNulls(size)
        }
    }
}