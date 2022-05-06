package no.nordicsemi.android.material.you

//import androidx.compose.material3.CardColors
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Immutable
//import androidx.compose.runtime.State
//import androidx.compose.runtime.rememberUpdatedState
//import androidx.compose.ui.graphics.Color

//@Immutable
//class DefaultCardColors(
//    private val containerColor: Color,
//    private val contentColor: Color,
//    private val disabledContainerColor: Color,
//    private val disabledContentColor: Color,
//) : CardColors {
//    @Composable
//    override fun containerColor(enabled: Boolean): State<Color> {
//        return rememberUpdatedState(if (enabled) containerColor else disabledContainerColor)
//    }
//
//    @Composable
//    override fun contentColor(enabled: Boolean): State<Color> {
//        return rememberUpdatedState(if (enabled) contentColor else disabledContentColor)
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || this::class != other::class) return false
//
//        other as DefaultCardColors
//
//        if (containerColor != other.containerColor) return false
//        if (contentColor != other.contentColor) return false
//        if (disabledContainerColor != other.disabledContainerColor) return false
//        if (disabledContentColor != other.disabledContentColor) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = containerColor.hashCode()
//        result = 31 * result + contentColor.hashCode()
//        result = 31 * result + disabledContainerColor.hashCode()
//        result = 31 * result + disabledContentColor.hashCode()
//        return result
//    }
//}
