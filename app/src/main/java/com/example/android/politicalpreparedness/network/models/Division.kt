package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//using Parcelable over Serializable. Parcelable is fully customizable, allowing developers to
// convert required data into Parcelable. Serialization is a marker interface as it converts an
// object into a stream using the Java reflection API. Due to this it ends up creating a number of garbage objects during the stream conversation process.
@Parcelize
data class Division(
        val id: String,
        val country: String,
        val state: String
) : Parcelable