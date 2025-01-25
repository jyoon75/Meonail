package com.example.meonail.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "response", strict = false)
data class WishListResponse(
    @field:Element(name = "header", required = false)
    var header: ResponseHeader? = null,

    @field:Element(name = "body", required = false)
    var body: ResponseBody? = null
)

@Root(name = "header", strict = false)
data class ResponseHeader(
    @field:Element(name = "resultCode", required = false)
    var resultCode: String = "",

    @field:Element(name = "resultMsg", required = false)
    var resultMsg: String = ""
)

@Root(name = "body", strict = false)
data class ResponseBody(
    @field:Element(name = "items", required = false)
    var items: WishItems? = null
)

@Root(name = "items", strict = false)
data class WishItems(
    @field:ElementList(entry = "item", inline = true, required = false)
    var itemList: List<WishItem>? = null
)
