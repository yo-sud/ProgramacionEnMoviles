package com.tecsup.cafecampus.model

/** Estados de un pedido. En Room se guarda como String (EstadoPedido.name). */
enum class EstadoPedido(val etiqueta: String) {
    RECIBIDO("Recibido"),
    PREPARANDO("Preparando"),
    LISTO("Listo"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    /** Siguiente estado del flujo normal; null si ya terminó (ENTREGADO o CANCELADO). */
    fun siguiente(): EstadoPedido? = when (this) {
        RECIBIDO -> PREPARANDO
        PREPARANDO -> LISTO
        LISTO -> ENTREGADO
        ENTREGADO, CANCELADO -> null
    }

    /** Pedido en curso (aparece en "Activos"). */
    val esActivo: Boolean get() = this == RECIBIDO || this == PREPARANDO || this == LISTO

    /** Estimación fija (en minutos) hasta que el pedido esté listo; null si ya no aplica. */
    val minutosEstimados: Int? get() = when (this) {
        RECIBIDO -> 15
        PREPARANDO -> 10
        LISTO, ENTREGADO, CANCELADO -> null
    }

    companion object {
        /** Convierte el String guardado en Room; ante un valor desconocido usa RECIBIDO. */
        fun desde(valor: String): EstadoPedido = entries.firstOrNull { it.name == valor } ?: RECIBIDO
    }
}