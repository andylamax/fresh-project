package com.asofttz.ui.react.widget.hamburger

import com.asofttz.ui.react.tools.ThemedProps
import com.asofttz.ui.react.tools.isMobile
import com.asofttz.ui.react.widget.hamburger.Hamburger.Props
import com.asofttz.ui.react.widget.hamburger.Hamburger.State
import kotlinx.css.*
import kotlinx.css.properties.Angle
import kotlinx.css.properties.deg
import kotlinx.css.properties.rotateZ
import kotlinx.css.properties.transform
import kotlinx.html.js.onClickFunction
import react.*
import styled.css
import styled.styledDiv
import styled.styledSpan

class Hamburger(p: Props) : RComponent<Props, State>(p) {

    object Props : ThemedProps() {
        var size = 2.em
        var onToggled = { _: Boolean -> }
        var isOpen = isMobile
    }

    class State(p: Props = Props) : RState {
        var isOpen = p.isOpen
    }

    init {
        state = State(p)
    }

    override fun componentWillReceiveProps(nextProps: Props) {
        setState {
            isOpen = nextProps.isOpen
        }
    }

    private fun lineStyles(defaultTop: LinearDimension, finalAngle: Angle): CSSBuilder.() -> Unit = {
        +HamburgerStyles.line
        props.theme?.let {
            borderTop = "solid 1px ${it.primaryColor.main}"
            borderBottom = "solid 1px ${it.primaryColor.main}"
            backgroundColor = Color(it.primaryColor.main)
        }

        if (!state.isOpen!!) {
            top = 50.pct
            transform {
                rotateZ(finalAngle)
            }
        } else {
            top = defaultTop
        }
    }

    override fun RBuilder.render() {
        if (state.isOpen == null) {
            state.isOpen = props.isOpen
        }
        val size = props.size
        styledDiv {
            attrs.onClickFunction = {
                setState {
                    isOpen = !isOpen!!
                    props.onToggled(isOpen!!)
                }
            }
            css {
                +HamburgerStyles.wrapper
                +props.css
                width = size
                height = size
            }

            styledSpan {
                css {
                    +lineStyles(30.pct, (-45).deg)
                }
            }

            styledSpan {
                css {
                    +lineStyles(50.pct, 45.deg)
                }
            }

            styledSpan {
                css {
                    +lineStyles(70.pct, (-45).deg)
                }
            }
        }
    }
}

fun RBuilder.hamburger(handler: RHandler<Props> = {}) = child(Hamburger::class.js, Props) {
    attrs {
        handler()
    }
}