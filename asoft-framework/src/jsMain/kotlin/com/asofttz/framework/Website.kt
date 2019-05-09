package com.asofttz.framework

import com.asofttz.module.Page
import com.asofttz.module.WebsiteProps
import react.RBuilder
import react.RComponent
import react.RState
import react.ReactElement
import react.router.dom.RouteResultProps
import react.router.dom.route

class Website : RComponent<WebsiteProps, RState>() {
    private fun Page.toReactComponent(): (RouteResultProps<WebsiteProps>) -> ReactElement = {
        it.match.params.apply {
            theme = props.theme
            user = props.user
            footer = props.footer
            onLogin = { u,p->
                props.onLogin(u,p)
            }
        }
        (component.unsafeCast<((RouteResultProps<WebsiteProps>) -> ReactElement)>()(it))
    }

    override fun RBuilder.render(): dynamic = props.pages.forEach { page ->
        route(path = page.route, exact = true, strict = true) { p: RouteResultProps<WebsiteProps> ->
            page.toReactComponent()(p)
        }
    }
}