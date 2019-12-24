package messages.fbesoain.cl.emessage.api.models

data class MessageClassification(
        var message: String = "",
        var user: String = "",
        var puntuation: Number = 0,
        var reason: String = ""
)