package ru.fellowgames.kt_backend

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils

@Controller
class SpyGameController {

  @GetMapping("/spy_main")
  fun main(model: Model): String {
    model["title"] = "Шпион"
    return "spy_main"
  }

  @GetMapping("/spy_game")
  fun game(model: Model,
      @RequestParam(value = "game_id") game_id: String,
      @CookieValue(value = "game_id_c") game_id_c: String?,
      response: HttpServletResponse): String {
    model["title"] = game_id + (game_id_c ?: "")
    
    val cookie = Cookie("game_id_c", game_id)
    response.addCookie(cookie)

    return "spy_game"
  }

}
