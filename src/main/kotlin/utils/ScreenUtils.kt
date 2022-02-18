package utils

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import java.awt.Toolkit

fun getCenterScreenLocation(): WindowPosition {
    val dimension = Toolkit.getDefaultToolkit().screenSize
    val width = dimension.width
    val height = dimension.height

    return WindowPosition((width/2.5).dp, (height/2.5).dp)
}