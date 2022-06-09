package com.alan.commonlib.other

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

class GsonUtils private constructor() {
    companion object {
        private const val KEY_DEFAULT = "defaultGson"
        private const val KEY_DELEGATE = "delegateGson"
        private const val KEY_LOG_UTILS = "logUtilsGson"
        private val GSONS: MutableMap<String, Gson?> = ConcurrentHashMap()

        /**
         * Set the delegate of [Gson].
         *
         * @param delegate The delegate of [Gson].
         */
        fun setGsonDelegate(delegate: Gson?) {
            if (delegate == null) return
            GSONS[KEY_DELEGATE] = delegate
        }

        /**
         * Set the [Gson] with key.
         *
         * @param key  The key.
         * @param gson The [Gson].
         */
        fun setGson(key: String, gson: Gson?) {
            if (TextUtils.isEmpty(key) || gson == null) return
            GSONS[key] = gson
        }

        /**
         * Return the [Gson] with key.
         *
         * @param key The key.
         * @return the [Gson] with key
         */
        fun getGson(key: String): Gson? {
            return GSONS[key]
        }

        private val gson: Gson
            get() {
                val gsonDelegate = GSONS[KEY_DELEGATE]
                if (gsonDelegate != null) {
                    return gsonDelegate
                }
                var gsonDefault = GSONS[KEY_DEFAULT]
                if (gsonDefault == null) {
                    gsonDefault = createGson()
                    GSONS[KEY_DEFAULT] = gsonDefault
                }
                return gsonDefault
            }

        /**
         * Serializes an object into json.
         *
         * @param object The object to serialize.
         * @return object serialized into json.
         */
        fun toJson(`object`: Any?): String {
            return toJson(gson, `object`)
        }

        /**
         * Serializes an object into json.
         *
         * @param src       The object to serialize.
         * @param typeOfSrc The specific genericized type of src.
         * @return object serialized into json.
         */
        fun toJson(src: Any?, typeOfSrc: Type): String {
            return toJson(gson, src, typeOfSrc)
        }

        /**
         * Serializes an object into json.
         *
         * @param gson   The gson.
         * @param object The object to serialize.
         * @return object serialized into json.
         */
        fun toJson(gson: Gson, `object`: Any?): String {
            return gson.toJson(`object`)
        }

        /**
         * Serializes an object into json.
         *
         * @param gson      The gson.
         * @param src       The object to serialize.
         * @param typeOfSrc The specific genericized type of src.
         * @return object serialized into json.
         */
        fun toJson(gson: Gson, src: Any?, typeOfSrc: Type): String {
            return gson.toJson(src, typeOfSrc)
        }

        /**
         * Converts [String] to given type.
         *
         * @param json The json to convert.
         * @param type Type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(json: String?, type: Class<T>): T {
            return fromJson(gson, json, type)
        }

        /**
         * Converts [String] to given type.
         *
         * @param json the json to convert.
         * @param type type type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(json: String?, type: Type): T {
            return fromJson(gson, json, type)
        }

        /**
         * Converts [Reader] to given type.
         *
         * @param reader the reader to convert.
         * @param type   type type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(reader: Reader, type: Class<T>): T {
            return fromJson(gson, reader, type)
        }

        /**
         * Converts [Reader] to given type.
         *
         * @param reader the reader to convert.
         * @param type   type type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(reader: Reader, type: Type): T {
            return fromJson(gson, reader, type)
        }

        /**
         * Converts [String] to given type.
         *
         * @param gson The gson.
         * @param json The json to convert.
         * @param type Type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(gson: Gson, json: String?, type: Class<T>): T {
            return gson.fromJson(json, type)
        }

        /**
         * Converts [String] to given type.
         *
         * @param gson The gson.
         * @param json the json to convert.
         * @param type type type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(gson: Gson, json: String?, type: Type): T {
            return gson.fromJson(json, type)
        }

        /**
         * Converts [Reader] to given type.
         *
         * @param gson   The gson.
         * @param reader the reader to convert.
         * @param type   type type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(gson: Gson, reader: Reader?, type: Class<T>): T {
            return gson.fromJson(reader, type)
        }

        /**
         * Converts [Reader] to given type.
         *
         * @param gson   The gson.
         * @param reader the reader to convert.
         * @param type   type type json will be converted to.
         * @return instance of type
         */
        fun <T> fromJson(gson: Gson, reader: Reader?, type: Type): T {
            return gson.fromJson(reader, type)
        }

        /**
         * Return the type of [List] with the `type`.
         *
         * @param type The type.
         * @return the type of [List] with the `type`
         */
        fun getListType(type: Type): Type {
            return TypeToken.getParameterized(MutableList::class.java, type).type
        }

        /**
         * Return the type of [Set] with the `type`.
         *
         * @param type The type.
         * @return the type of [Set] with the `type`
         */
        fun getSetType(type: Type): Type {
            return TypeToken.getParameterized(MutableSet::class.java, type).type
        }

        /**
         * Return the type of map with the `keyType` and `valueType`.
         *
         * @param keyType   The type of key.
         * @param valueType The type of value.
         * @return the type of map with the `keyType` and `valueType`
         */
        fun getMapType(keyType: Type, valueType: Type): Type {
            return TypeToken.getParameterized(MutableMap::class.java, keyType, valueType).type
        }

        /**
         * Return the type of array with the `type`.
         *
         * @param type The type.
         * @return the type of map with the `type`
         */
        fun getArrayType(type: Type): Type {
            return TypeToken.getArray(type).type
        }

        /**
         * Return the type of `rawType` with the `typeArguments`.
         *
         * @param rawType       The raw type.
         * @param typeArguments The type of arguments.
         * @return the type of map with the `type`
         */
        fun getType(rawType: Type, vararg typeArguments: Type): Type {
            return TypeToken.getParameterized(rawType, *typeArguments).type
        }

        val gson4LogUtils: Gson?
            get() {
                var gson4LogUtils = GSONS[KEY_LOG_UTILS]
                if (gson4LogUtils == null) {
                    gson4LogUtils = GsonBuilder().setPrettyPrinting().serializeNulls().create()
                    GSONS[KEY_LOG_UTILS] = gson4LogUtils
                }
                return gson4LogUtils
            }

        private fun createGson(): Gson {
            return GsonBuilder().serializeNulls().disableHtmlEscaping().create()
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}