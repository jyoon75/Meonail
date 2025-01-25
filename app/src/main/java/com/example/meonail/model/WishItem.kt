package com.example.meonail.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

// ✅ 개별 전시 아이템 클래스 (Parcelable 추가)
@Parcelize
@Root(name = "item", strict = false)
data class WishItem(
    @field:Element(name = "title", required = false)
    var title: String = "",

    @field:Element(name = "period", required = false)
    var period: String = "",

    @field:Element(name = "eventPeriod", required = false)
    var eventPeriod: String = "",

    @field:Element(name = "description", required = false)
    var description: String = "",

    @field:Element(name = "charge", required = false)
    var charge: String = "",

    @field:Element(name = "contactPoint", required = false)
    var contactPoint: String = "",

    @field:Element(name = "url", required = false)
    var url: String = "",

    @field:Element(name = "imageObject", required = false)
    var imageObject: String = ""
) : Parcelable
