package com.example.meonail.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class WishListResponse(
    @SerializedName("GGCULTUREVENTSTUS")
    val eventList: List<WishEventData>
)

data class WishEventData(
    @SerializedName("row")
    val events: List<WishItem>
)

data class WishItem(
    @SerializedName("INST_NM") val instName: String,  // 기관명
    @SerializedName("TITLE") val title: String,  // 행사명
    @SerializedName("CATEGORY_NM") val category: String,  // 카테고리
    @SerializedName("URL") val url: String,  // 상세 URL
    @SerializedName("EVENT_TM_INFO") val eventTime: String?,  // 시간 정보
    @SerializedName("PARTCPT_EXPN_INFO") val participationFee: String?,  // 참가비
    @SerializedName("TELNO_INFO") val contact: String?,  // 연락처
    @SerializedName("IMAGE_URL") val imageUrl: String? = null // ✅ 기본값 추가 (null 가능)
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() // ✅ imageUrl도 읽어옴
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(instName)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeString(url)
        parcel.writeString(eventTime)
        parcel.writeString(participationFee)
        parcel.writeString(contact)
        parcel.writeString(imageUrl) // ✅ imageUrl 저장
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WishItem> {
        override fun createFromParcel(parcel: Parcel): WishItem = WishItem(parcel)
        override fun newArray(size: Int): Array<WishItem?> = arrayOfNulls(size)
    }
}
