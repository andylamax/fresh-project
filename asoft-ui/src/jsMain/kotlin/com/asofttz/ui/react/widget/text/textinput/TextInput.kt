package com.asofttz.ui.react.widget.text.textinput

import com.asofttz.ui.react.tools.ThemedProps
import com.asofttz.ui.react.tools.View
import com.asofttz.ui.react.widget.text.textinput.TextInput.Props
import com.asofttz.ui.react.widget.text.textinput.TextInput.State
import com.asofttz.view.widgets.text.textinput.TextInputStyles
import kotlinx.css.Color
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onFocusFunction
import org.w3c.dom.HTMLInputElement
import react.*
import styled.css
import styled.styledDiv
import styled.styledInput
import kotlin.browser.document

class TextInput(p: Props) : RComponent<Props, State>(p) {

    object Props : ThemedProps() {
        var hint = ""
        var type = InputType.text
        var name = ""
        var onChange = { _: String -> }
        var onBlur = {}
    }

    class State : RState {
        var textValue = ""
        var isFocused = false
    }

    init {
        state = State()
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                +TextInputStyles.root
                +props.css
            }

            styledDiv {
                css {
                    if (state.isFocused) {
                        +TextInputStyles.tagNameFocused
                    } else {
                        +TextInputStyles.tagNameUnFocused
                    }
                }
                +props.name
            }


            styledInput(type = props.type) {
                attrs {
                    id = View.getId()
                    if (state.isFocused) {
                        placeholder = props.hint
                    }
                    name = props.name

                    onChangeFunction = {
                        setState {
                            textValue = (document.getElementById(id) as HTMLInputElement).value
                            props.onChange(textValue)
                        }
                    }

                    onFocusFunction = {
                        setState {
                            isFocused = true
                        }
                    }

                    onBlurFunction = {
                        setState {
                            isFocused = !textValue.isEmpty()
                        }
                        props.onBlur()
                    }
                }

                css {
                    +TextInputStyles.input
                    color = Color(props.theme.text.onBackground.light)
                    focus {
                        borderBottomColor = Color(props.theme.primaryColor.dark)
                    }
                }
            }
        }
    }
}

fun RBuilder.textInput(handler: RHandler<Props> = {}) = child(TextInput::class.js, Props) {
    handler()
}