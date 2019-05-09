package com.asofttz.ui.react.widget.reactcorousel

import kotlinext.js.require
import react.*

private var cssLoaded = false

external interface Props : RProps

@JsModule("react-responsive-carousel")
external class ReactCarousel : Component<Props, RState> {
    override fun render(): dynamic = definedExternally
}

fun RBuilder.reactCarousel(handler: RHandler<Props>) = child(ReactCarousel::class) {
    if (!cssLoaded) {
        require("react-responsive-carousel/lib/styles/carousel.min.css")
    }
    handler()
}