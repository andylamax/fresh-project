package com.asofttz.ui.react.widget.image

import com.asofttz.ui.react.tools.ThemedProps
import com.asofttz.ui.react.tools.View
import com.asofttz.ui.react.widget.image.ImageUpload.Props
import com.asofttz.ui.react.widget.image.ImageUpload.State
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get
import react.*
import styled.css
import styled.styledDiv
import styled.styledImg
import styled.styledInput

class ImageUpload(p: Props) : RComponent<Props, State>(p) {

    object Props : ThemedProps() {
        var onUploaded = { _: File, _: String -> }
    }

    class State : RState {
        var imageSrc = ""
    }

    init {
        state = State()
    }

    private val inputId: String = View.getId()

    private val input get() = View.byId<HTMLInputElement>(inputId)

    private fun readImage() {
        input?.files?.let {
            val file = it[0]!!
            val fileReader = FileReader()
            fileReader.onload = {
                setState {
                    imageSrc = (it.target as FileReader).result as String
                    props.onUploaded(file, imageSrc)
                }
            }
            fileReader.readAsDataURL(file)
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                position = Position.relative
                backgroundColor = Color.gray
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = Align.center
                cursor = Cursor.pointer
                margin(2.pct)
                maxWidth = 20.em
                maxHeight = 20.em
                textAlign = TextAlign.center
                +props.css
            }

            attrs {
                onClickFunction = {
                    input?.click()
                }
            }

            if (state.imageSrc.isEmpty().not()) {
                styledImg(src = state.imageSrc) {
                    css {
                        width = 100.pct
                        height = 100.pct
                    }
                }
            } else {
                styledDiv {
                    +"Click To Set User Pic"
                }
            }

            styledInput(type = InputType.file) {
                attrs {
                    id = inputId
                    accept = "image/*"
                    onChangeFunction = {
                        readImage()
                    }
                }
                css {
                    position = Position.absolute
                    visibility = Visibility.hidden
                }
            }
        }
    }
}

fun RBuilder.imageUpload(handler: RHandler<Props> = {}) = child(ImageUpload::class.js, Props) {
    handler()
}

