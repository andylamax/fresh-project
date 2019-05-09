package com.asofttz.framework

import com.asofttz.framework.FrameworkComponent.Props
import com.asofttz.framework.FrameworkComponent.State
import com.asofttz.auth.User
import com.asofttz.framework.applications.Application
import com.asofttz.framework.navmenu.NavPane
import com.asofttz.module.Module
import com.asofttz.module.Page
import com.asofttz.module.WebsiteProps
import com.asofttz.module.react.ScopedRComponent
import com.asofttz.module.react.onDesktop
import com.asofttz.module.react.onMobile
import com.asofttz.theme.Theme
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLDivElement
import react.*
import react.router.dom.RouteResultHistory
import react.router.dom.hashRouter
import react.router.dom.redirect
import styled.css
import styled.styledDiv
import styled.styledSection
import kotlin.browser.document
import kotlin.browser.window
import kotlin.reflect.KClass

class FrameworkComponent(p: Props) : RComponent<Props, State>(p) {
    private lateinit var history: RouteResultHistory

    object Props : RProps {
        var themes = arrayOf<Theme>()
        var footer = "Footer"
        var title = "Title"
        var modules = arrayOf<Module>()
        var pages = arrayOf<Page>()
        lateinit var website: KClass<*>
        var isDevMode = false
    }

    class State(props: Props) : RState {
        var theme = props.themes.getOrNull(0) ?: Theme()
        var user: User? = null
        var title: String = "Dashboard"
        var selectedSection = props.modules[0].mainSection
        var drawerOpen = false
        var hasError = false
        var isDevMode = props.isDevMode
    }


    init {
        state = State(p)
    }

    private fun logout() = setState {
        isDevMode = false
        user = null
        history.push("/")
    }

    private fun login(it: User) = setState {
        user = it
        history.push("/dashboard/")
    }

    override fun componentDidCatch(error: Throwable, info: RErrorInfo) {
        console.log(error.message)
        setState {
            hasError = true
        }
    }

    override fun componentDidMount() {
        if (state.isDevMode) {
            setState { user = User.fakeUser.apply { permits += ":dev" } }
        }
    }

    private fun RBuilder.navPane() = child(NavPane::class) {
        attrs {
            title = props.title
            user = state.user!!
            theme = state.theme
            selectedSection = state.selectedSection
            footer = props.footer
            modules = props.modules
        }
        attrs.onCloseDrawer = {
            setState {
                drawerOpen = false
            }
        }
        attrs.onMenuItemClicked = {
            setState {
                drawerOpen = false
                selectedSection = it
            }
        }
    }

    private fun RBuilder.application() = child(Application::class) {
        attrs {
            theme = state.theme
            title = state.title
            user = state.user!!
            modules = props.modules
            themes = props.themes
            setTheme = {
                window.setTimeout({
                    if (state.theme != it)
                        setState {
                            theme = it
                        }
                }, 0)
            }
            onRouteResultHistory = {
                history = it
            }
        }

        attrs.onDrawerOpen = {
            setState {
                drawerOpen = true
            }
        }

        attrs.onLogout = {
            logout()
        }
    }

    private fun RBuilder.navigationSide() = styledSection {
        css {
            height = 100.pct
            onDesktop {
                width = 20.pct
            }
            transition(duration = 0.5.s)
            onMobile {
                position = Position.absolute
                left = if (state.drawerOpen) {
                    0.px
                } else {
                    (-80).vw
                }
                top = 0.px
                width = 80.vw
            }
        }
        navPane()
    }

    private fun RBuilder.applicationSide() = styledSection {
        css {
            height = 100.pct
            onDesktop {
                width = 80.pct
            }
            onMobile {
                width = 100.pct
            }
        }
        application()
    }

    private fun RBuilder.dashboardSection() = styledSection {
        css {
            display = Display.flex
            height = 100.vh
            width = 100.pct
            onMobile {
                fontSize = 2.em
            }
        }
        navigationSide()
        applicationSide()
    }

    private fun RBuilder.anonymousPage() = child(props.website.unsafeCast<KClass<RComponent<WebsiteProps, RState>>>()) {
        attrs.theme = state.theme
        attrs.footer = props.footer
        attrs.pages = props.pages
        attrs.onLogin = { user, prop ->
            history = prop!!.history
            login(user)
        }
    }

    override fun RBuilder.render(): dynamic {
        if (state.hasError) {
            return styledDiv {
                +"Opps Error Here"
            }
        }
        return hashRouter {
            if (state.user == null) {
                anonymousPage()
            } else {
                dashboardSection()
            }
        }
    }
}

fun RBuilder.framework(handler: RHandler<Props> = {}) = child(FrameworkComponent::class) {
    attrs { website = Website::class.unsafeCast<KClass<RComponent<WebsiteProps, RState>>>() }
    handler()
}