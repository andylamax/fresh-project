package com.asofttz.framework.navmenu

import com.asofttz.framework.navmenu.NavPane.Props
import com.asofttz.framework.navmenu.NavPane.State
import com.asofttz.module.Module
import com.asofttz.module.ModuleProps
import com.asofttz.module.react.onDesktop
import com.asofttz.module.react.onMobile
import com.asofttz.theme.Theme
import kotlinx.css.*
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RState
import styled.css
import styled.styledDiv
import styled.styledSection

class NavPane : RComponent<Props, State>() {
    companion object Props : ModuleProps() {
        var onMenuItemClicked = { _: Module.Section -> }
        var title = "Nav Pane Title"
        var onCloseDrawer = {}
        lateinit var selectedSection: Module.Section
        var footer = "Nav Footer"
    }

    class State : RState

    private fun RBuilder.headerSection() = styledSection {
        css {
            height = 4.em
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.center
            position = Position.relative
        }

        styledDiv {
            +props.title
        }

        styledDiv {
            css {
                onDesktop {
                    display = Display.none
                }
                position = Position.absolute
                top = 35.pct
                right = 0.pct
                marginRight = 1.em
                onMobile { fontSize = 1.5.em }
            }

            attrs.onClickFunction = {
                props.onCloseDrawer()
            }
            +"X"
        }
    }

    private fun RBuilder.midSection() = styledSection {
        css {
            height = 100.pct - 7.em - 1.px
            overflowY = Overflow.auto
            overflowX = Overflow.hidden
        }

        props.modules.forEach { module ->
            if (props.user.hasPermits(module.permits))
                navMenu(module) {
                    attrs {
                        theme = props.theme
                        user = props.user
                        onClick = {
                            props.onMenuItemClicked(it)
                        }
                        selectedSection = props.selectedSection
                    }
                }
        }
    }

    private fun RBuilder.footerSection() = styledSection {
        css {
            minHeight = 3.em
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.center
            borderTop = "solid 1px ${props.theme.text.onAlternate[0].main}"
        }

        styledDiv {
            css {
                fontSize = 0.8.em
                textAlign = TextAlign.center
                padding(horizontal = 2.em)
            }
            +props.footer
        }
    }

    override fun RBuilder.render(): dynamic = styledSection {
        css {
            width = 100.pct
            height = 100.pct
            display = Display.flex
            position = Position.relative
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.stretch
            backgroundColor = Color(props.theme.alternateColor[0].main)
            color = Color(props.theme.text.onAlternate[0].main)
            transition(duration = 3.s)
            zIndex = 999999
        }

        headerSection()

        midSection()

        footerSection()
    }
}