package ru.fellowgames.kt_backend.utils

// @Bean
class SpyGameEngine {

  class Room(id: String) {
    val players = mutableListOf<String>()

    // TODO: do not show spy's locations
    val locations = mutableListOf<String>()

    fun addPlayer(player: String) {
      players.add(player)
    }

    fun addLocation(location: String) {
      locations.add(location)
    }
  }

  val rooms = mutableMapOf<String, Room>()

  // @GetMapping("/spy_game")
  // fun blog(model: Model): String {
  //   model["title"] = "Шпион"
  //   return "spy_game"
  // }

}
