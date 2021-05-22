package ru.fellowgames.kt_backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import ru.fellowgames.kt_backend.utils.SpyGameEngine
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class SpyGameController @Autowired constructor(
    private val engine: SpyGameEngine
) {

    @GetMapping("/spy_main")
    fun main(model: Model): String {
        model["title"] = "Шпион - Главная"
        return "spy_main"
    }

    @GetMapping("/spy_v0")
    fun spygamev0(model: Model): String {
        model["title"] = "Шпион"
        return "spygame_v0"
    }

    @Synchronized
    fun initGame(
        capacity: String,
        raw_locations: String,
        name: String,
    ): String {
        val game_id = engine.rooms.size.toString()
        var locations = raw_locations.split("\n")
        locations.forEach { it.trim() }
        engine.rooms[game_id] = SpyGameEngine.Room(game_id, capacity.toInt(), name, locations)
        return game_id
    }

    @GetMapping("/spy_game_init")
    fun game_init(
        model: Model,
        @RequestParam(value = "capacity") capacity: String,
        @RequestParam(value = "locations") locations: String,
        @RequestParam(value = "name") name: String,
        response: HttpServletResponse
    ): String {
        val game_id = initGame(capacity, locations, name)
        response.addCookie(Cookie("game_id", game_id))
        response.addCookie(Cookie("name", name))

        return "redirect:/spy_game"
    }


    @GetMapping("/spy_game_join")
    fun game_join(
        model: Model,
        @RequestParam(value = "game_id") game_id: String,
        @RequestParam(value = "locations") locations: String,
        @RequestParam(value = "name") name: String,
        response: HttpServletResponse
    ): String {
        var locations_parsed = locations.split("\n")
        locations_parsed.forEach { it.trim() }
        val game = engine.rooms[game_id]
        if (game == null) {
            model["title"] = "Игра не найдена"
            return "spy_main"
        }
        try {
            game.addPlayer(name, locations_parsed)
        } catch (e: Exception) {
            model["title"] = e.message ?: "Невозможно добавить игрока в игру"
            return "spy_main"
        }
        response.addCookie(Cookie("game_id", game_id))
        response.addCookie(Cookie("name", name))

        return "redirect:/spy_game"
    }

    @GetMapping("/spy_game")
    fun game(
        model: Model,
        @CookieValue(value = "game_id") game_id: String?,
        @CookieValue(value = "name") name: String?
    ): String {
        model["title"] = "Шпион. Игра $game_id"
        model["game_id"] = game_id ?: "null"
        model["name"] = name ?: "null"
        model["locations"] = "TODO"

        return "spy_game"
    }

    @ResponseBody
    @GetMapping("/spy_game_get_loc")
    fun getLoc(
        model: Model,
        @CookieValue(value = "game_id") game_id: String?,
        @CookieValue(value = "name") name: String?
    ): String {
        if (name == null) return "Не получено имя"
        val game = engine.rooms[game_id]
        return game?.getLocOrSpy(name) ?: "Игра не найдена"
    }

}
