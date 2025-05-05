package com.stewardapostol.currencyconverter.data.util

import com.stewardapostol.currencyconverter.data.model.CurrencyCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

object CurrencyCodeListSerializer : KSerializer<List<CurrencyCode>> {
    override val descriptor: SerialDescriptor = ListSerializer(CurrencyCode.serializer()).descriptor

    override fun deserialize(decoder: Decoder): List<CurrencyCode> {
        val jsonArray = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        val jsonElement = jsonArray.decodeJsonElement()
        return jsonElement.jsonArray.map {
            val array = it.jsonArray
            CurrencyCode(array[0].jsonPrimitive.content, array[1].jsonPrimitive.content)
        }
    }

    override fun serialize(encoder: Encoder, value: List<CurrencyCode>) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("Expected JsonEncoder")
        val jsonArray = JsonArray(value.map {
            JsonArray(listOf(JsonPrimitive(it.code), JsonPrimitive(it.name)))
        })
        jsonEncoder.encodeJsonElement(jsonArray)
    }
}
