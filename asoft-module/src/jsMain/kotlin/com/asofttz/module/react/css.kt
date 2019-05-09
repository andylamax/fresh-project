package com.asofttz.module.react

import kotlinx.css.CSSBuilder
import kotlinx.css.Color
import kotlinx.css.LinearDimension
import kotlinx.css.px

fun String.toColor() = Color(this)

@Deprecated("Use the one from asofttz.ui package")
fun CSSBuilder.onMobile(block: CSSBuilder.() -> Unit) {
    media("only screen and (orientation: portrait)") {
        block()
    }
}

@Deprecated("Use the one from asofttz.ui package")
fun CSSBuilder.onDesktop(block: CSSBuilder.() -> Unit) {
    media("only screen and (orientation: landscape)") {
        block()
    }
}