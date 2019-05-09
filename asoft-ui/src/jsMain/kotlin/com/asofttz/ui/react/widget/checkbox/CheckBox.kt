package com.asofttz.ui.react.widget.checkbox

import com.asofttz.ui.react.tools.ThemedProps
import com.asofttz.ui.react.widget.checkbox.CheckBox.Props
import com.asofttz.ui.react.widget.checkbox.CheckBox.State
import kotlinx.css.em
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.*
import styled.css
import styled.styledDiv

class CheckBox(p: Props) : RComponent<Props, State>(p) {
    object Props : ThemedProps() {
        var size = 1.0   // in em
        var borderSize = 1 // in px
        var lineSize = 1 // in px
        var onChanged = { _: Boolean -> }
        var checked: Boolean? = null
    }

    class State(p: Props = Props) : RState {
        var checked = p.checked ?: false
    }

    init {
        state = State(p)
    }

    override fun RBuilder.render() {
        val borderSize = props.borderSize //in px
        val size = props.size // in em
        val lineSize = props.lineSize // in px

        styledDiv {
            attrs.onClickFunction = {
                setState {
                    checked = checked.not()
                    props.onChanged(checked)
                }
            }
            css {
                +CheckBoxStyles.root
                width = size.em
                height = size.em
                border = "solid ${borderSize}px white"

                border = "solid ${borderSize}px ${props.theme.text.onBackground.main}"
            }

            styledDiv {
                css {
                    borderRight = "solid ${lineSize}px black"
                    props.theme?.let {
                        borderRight = "solid ${lineSize}px ${it.text.onBackground.main}"
                    }
                    if (state.checked) {
                        +CheckBoxStyles.tailLeftChecked
                    } else {
                        left = (-lineSize / 2).px
                        +CheckBoxStyles.tailLeftUnChecked
                    }
                }
            }

            styledDiv {
                css {
                    borderLeft = "solid ${lineSize}px black"
                    borderLeft = "solid ${lineSize}px ${props.theme.text.onBackground.main}"
                    if (state.checked) {
                        +CheckBoxStyles.tailRightChecked
                    } else {
                        right = (-lineSize / 2).px
                        +CheckBoxStyles.tailRightUnChecked
                    }
                }
            }
        }
    }
}

fun RBuilder.checkbox(handler: RHandler<Props> = {}) = child(CheckBox::class.js, Props) {
    attrs {
        handler()
    }
}