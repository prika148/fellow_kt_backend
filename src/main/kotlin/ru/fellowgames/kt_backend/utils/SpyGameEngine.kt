package ru.fellowgames.kt_backend.utils

import org.springframework.stereotype.Component
import java.lang.Exception
import kotlin.math.floor

@Component
class SpyGameEngine {

    class Room(
        private val id: String,
        private val capacity: Int,
        private val owner: String,
        locations: List<String>,
    ) {
        class Player(val name: String, val locations: List<String>) {}

        private val players = mutableListOf<Player>()
        private val spy = floor(Math.random() * capacity).toInt()
        private var location: String? = null

        init {
            players.add(Player(owner, locations))
        }

        fun addPlayer(playerName: String, locations: List<String>) {
            val idx = players.indexOfFirst { it.name == playerName }
            if (idx != -1) throw Exception("Игрок с таким именем уже есть в игре!")
            if (isReady()) throw Exception("Набралось уже достаточно игроков!")
            players.add(Player(playerName, locations))
        }

        private fun isReady(): Boolean {
            return capacity == players.size
        }

        fun getLocOrSpy(playerName: String): String {
            if (!isReady()) return "Не все игроки подключились (${players.size} из ${capacity})"
            val idx = players.indexOfFirst { it.name == playerName }
            if (idx == -1) return "Не в списке!"
            if (idx == spy) return "Шпион"

            synchronized(this) {
                if (location == null) {
                    var allLocations = mutableSetOf<String>()
                    players.forEach { if (it.name != players[spy].name) allLocations.addAll(it.locations) }
                    println("spy = $spy, allLocations = $allLocations")
                    location = allLocations.random()
                }
            }
            return location ?: "Невозможно"
        }
    }

    val rooms = mutableMapOf<String, Room>()
}
