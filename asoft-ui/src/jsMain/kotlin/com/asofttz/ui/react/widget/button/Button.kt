package com.asofttz.ui.react.widget.button

import com.asofttz.theme.light
import com.asofttz.ui.react.tools.ThemedProps
import com.asofttz.ui.react.widget.button.Button.Props
import com.asofttz.ui.react.widget.icon.Icon
import com.asofttz.ui.react.widget.icon.icon
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.boxShadow
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kotlinx.html.js.onMouseUpFunction
import react.RBuilder
import react.RComponent
import react.RHandler
import react.RState
import styled.css
import styled.styledDiv

class Button(p: Props) : RComponent<Props, RState>(p) {
    object Props : ThemedProps() {
        var text = ""
        var icon: Icon? = null
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                userSelect = UserSelect.none
                display = Display.inlineBlock
                backgroundColor = Color.white
                color = Color.black
                height = 1.5.em
                cursor = Cursor.pointer
                lineHeight = LineHeight(height.value)
                padding(0.5.em)
                transition(duration = 0.2.s)
                boxShadow(Color.gray, 0.px, 10.px, 7.px, (-4).px)

                hover {
                    boxShadow(Color.gray, 0.px, 12.px, 10.px, (-2).px)
                }

                active {
                    boxShadow(Color.gray, 0.px, 9.px, 5.px, (-5).px)
                }

                props.theme.let {
                    backgroundColor = Color(it.backgroundColor.light)
                    color = it.text.onBackground.light()
                }
                +props.css
            }

            attrs {
                onMouseUpFunction = {
                    props.onClick()
                }
            }
            props.icon?.let {
                icon {
                    attrs {
                        icon = it
                        css = {
                            props.theme?.let {
                                borderColor = Color(it.text.onBackground.light).withAlpha(0.0)
                                color = Color(it.text.onBackground.light)
                                fontSize = 1.em
                            }
                        }
                    }
                }
            }
            +props.text
        }
    }
}

fun RBuilder.button(text: String = "Button", handler: RHandler<Props> = {}) = child(Button::class.js, Props) {
    attrs {
        this.text = text
        handler()
    }
}