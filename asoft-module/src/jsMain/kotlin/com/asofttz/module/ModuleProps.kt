package com.asofttz.module

import com.asofttz.auth.User
import com.asofttz.module.Module
import com.asofttz.theme.Theme
import react.RProps
import react.router.dom.RouteResultHistory
import react.router.dom.RouteResultLocation
import react.router.dom.RouteResultMatch
import react.router.dom.RouteResultProps

abstract class ModuleProps : RProps {
    var theme = Theme()
    var themes = arrayOf<Theme>()
    var user: User = User.fakeUser
    var allPerms = arrayOf<String>()
    var setTitle = { _: String -> }
    var setTheme = { _: Theme -> }
    var modules = arrayOf<Module>()
    var routeProps: RouteResultProps<*>? = null
}