package tz.co.knauf.framework

import com.asofttz.date.Date
import com.asofttz.framework.SettingsModule
import com.asofttz.framework.framework
import com.asofttz.theme.Theme
import react.dom.render
import kotlin.browser.document

fun main() = render(document.getElementById("root")) {
    framework {
        attrs {
            title = "Knauf Gypsum Tz"
            val copyright = "Copyright ${169.toChar()} ${Date().getFullYear()}"
            footer = "$copyright $title - All Rights Reserved"
            themes = arrayOf(Theme())
            isDevMode = false

            pages = arrayOf()

            modules = arrayOf(
                    SettingsModule
            )
        }
    }
}
