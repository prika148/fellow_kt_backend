package ru.fellowgames.kt_backend

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.util.WebUtils

@Controller
class SpyGameController {

  @GetMapping("/spy_main")
  fun main(model: Model): String {
    model["title"] = "Шпион"
    return "spy_main"
  }

  @GetMapping("/spy_game")
  fun game(model: Model): String {
    println(model)
    model["title"] = "Шпион"
    return "spy_game"
  }

}
