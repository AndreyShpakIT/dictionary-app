package com.example.dictionary3

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Version {

    var value: String
    var date: Date

    constructor(value: String) {

        if (!isVersion(value)) {
            throw Exception("Строка не является версией")
        }

        this.value = value
        date = convertStringToVDate(value)

    }

    constructor(date: Date) {
        this.date = date
        value = convertDateToVString(date)
    }

    override operator fun equals(other: Any?): Boolean {

        if (other !is Version) {
            return false
        }

        return this.date == other.date
    }

    operator fun compareTo(other: Any?) : Int {

        if (other !is Version) {
           throw Exception("Объект не является объектом класса Version")
        }

        return this.date.compareTo(other.date)
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

    companion object {

        private var pattern = "ddMMyyHHmmss"

        @SuppressLint("SimpleDateFormat")
        fun convertDateToVString(date: Date) : String {

            return "V" + {SimpleDateFormat(pattern).format(date)}
        }

        fun convertStringToVDate(value: String) : Date {

            if (!isVersion(value)) {
                throw Exception("Строка не является версией")
            }

            val year = value.substring(1, 2).toInt()
            val month = value.substring(3, 2).toInt()
            val day = value.substring(5, 2).toInt()

            val hour = value.substring(7, 2).toInt()
            val minute = value.substring(9, 2).toInt()
            val second = value.substring(11, 2).toInt()

            return Date(year, month, day, hour, minute, second)
        }

        fun getNowVersion() : Version {
            val date = Calendar.getInstance().time
            return Version(date)
        }

        fun isVersion(value: String) : Boolean = Regex("(V\\d{12})").matches(value)

        fun getLocalVersion() : Version {
            //TODO("getLocalVersion")
            throw Exception("Не реализовано")
        }

        fun setLocalVersion(version: Version) {
            //TODO("setLocalVersion")
            throw Exception("Не реализовано")
        }
    }


}