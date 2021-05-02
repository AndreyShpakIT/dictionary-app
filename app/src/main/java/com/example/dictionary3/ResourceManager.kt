package com.example.dictionary3

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat

class ResourceManager() {

    companion object {

        lateinit var resources : Resources

        fun getSelectedBackgroundColor() : Int {
            return ResourcesCompat.getColor(resources, R.color.card_light_background_selected, null)
        }

        fun getUnselectedBackgroundColor() : Int {
            return ResourcesCompat.getColor(resources, R.color.card_light_background, null)
        }

    }

}