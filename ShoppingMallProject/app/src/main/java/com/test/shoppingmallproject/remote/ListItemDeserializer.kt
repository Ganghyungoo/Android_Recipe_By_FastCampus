package com.test.shoppingmallproject.remote

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.test.shoppingmallproject.model.Coupon
import com.test.shoppingmallproject.model.Empty
import com.test.shoppingmallproject.model.FullAd
import com.test.shoppingmallproject.model.Horizontal
import com.test.shoppingmallproject.model.Image
import com.test.shoppingmallproject.model.ListItem
import com.test.shoppingmallproject.model.Sale
import com.test.shoppingmallproject.model.SellItem
import com.test.shoppingmallproject.model.ViewPager
import com.test.shoppingmallproject.model.ViewType
import java.lang.Exception
import java.lang.reflect.Type

class ListItemDeserializer: JsonDeserializer<ListItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): ListItem {
        val viewTypeName = json.asJsonObject.getAsJsonPrimitive("viewType").asString
        val gson = GsonBuilder()
            .registerTypeAdapter(ListItem::class.java, ListItemDeserializer())
            .create()

        return try {
            when(viewTypeName) {
                ViewType.VIEW_PAGER.name -> gson.fromJson(json, ViewPager::class.java)
                ViewType.HORIZONTAL.name -> gson.fromJson(json, Horizontal::class.java)
                ViewType.FULL_AD.name -> gson.fromJson(json, FullAd::class.java)

                ViewType.SELL_ITEM.name -> gson.fromJson(json, SellItem::class.java)
                ViewType.IMAGE.name -> gson.fromJson(json, Image::class.java)
                ViewType.SALE.name -> gson.fromJson(json, Sale::class.java)
                ViewType.COUPON.name -> gson.fromJson(json, Coupon::class.java)
                else -> gson.fromJson(json, Empty::class.java)
            }
        }catch (e:Exception){
            gson.fromJson(json, Empty::class.java)
        }
    }
}