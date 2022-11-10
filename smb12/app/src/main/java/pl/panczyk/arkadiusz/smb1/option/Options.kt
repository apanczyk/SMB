package pl.panczyk.arkadiusz.smb1.option

import android.graphics.Color

object Options {

    var color: Int = 0
    var size: Float = 0.0f

    const val PREFERENCES = "preferences"

    const val COLOR = "color"
    const val BASIC_COLOR = Color.CYAN
    const val SIZE = "size"
    const val BASIC_SIZE = 12f

    fun colorOf(color: String): Int {
        return when (color) {
            "Red" -> Color.RED
            "Green" -> Color.GREEN
            "Blue" -> Color.BLUE
            "Cyan" -> Color.CYAN
            "Magenta" -> Color.MAGENTA
            "White" -> Color.WHITE
            else -> Color.BLACK
        }
    }

    fun fromColor(color: Int): String {
        return when (color) {
            Color.RED -> "Red"
            Color.GREEN -> "Green"
            Color.BLUE -> "Blue"
            Color.CYAN -> "Cyan"
            Color.MAGENTA -> "Magenta"
            Color.WHITE -> "White"
            else -> "Black"
        }
    }

}