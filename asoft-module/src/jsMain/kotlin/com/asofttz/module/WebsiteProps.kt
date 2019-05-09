package com.asofttz.module

import com.asofttz.auth.User
import com.asofttz.module.ModuleProps
import kotlinx.html.RP
import react.RProps
import react.router.dom.RouteResultProps

abstract class WebsiteProps : ModuleProps() {
    var footer = ""
    var onLogin = { _: User, _: RouteResultProps<RProps>? -> }
    var pages = arrayOf<Page>()
}