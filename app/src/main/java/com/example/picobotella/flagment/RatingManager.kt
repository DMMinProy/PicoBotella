// Paquete al que pertenece la app
package com.example.picobotella

// Permite acceder al contexto de la app (ej: para leer preferencias guardadas)
import android.content.Context
// Permite manejar los fragmentos que se muestran en pantalla
import androidx.fragment.app.FragmentManager

// Objeto singleton que decide CUÁNDO mostrar el panel de calificación
// Ejemplo: como un portero que decide quién puede entrar a una fiesta
object RatingManager {

    // Nombre del archivo donde se guardan las preferencias de la app
    private const val PREFS = "picobotella_prefs"
    // Clave para saber si el usuario ya calificó la app
    private const val KEY_YA_CALIFICO = "ya_califico"
    // Clave para guardar hasta qué fecha posponer la calificación
    private const val KEY_POSPONER = "posponer_hasta"
    // Clave para contar cuántas veces abrió la app
    private const val KEY_SESIONES = "sesiones_count"
    // Clave para contar cuántas partidas jugó
    private const val KEY_PARTIDAS = "partidas_count"

    // Mínimo de veces que debe abrir la app antes de pedirle que califique
    private const val SESIONES_MINIMAS = 3
    // Mínimo de partidas que debe jugar antes de pedirle que califique
    private const val PARTIDAS_MINIMAS = 5

    // Registra una nueva sesión cada vez que el usuario abre la app
    // Ejemplo: si abrió la app 3 veces, el contador llega a 3
    fun registrarSesion(context: Context) {
        // Abre las preferencias guardadas
        val prefs = prefs(context)
        // Lee el número actual de sesiones (0 si nunca se guardó)
        val actual = prefs.getInt(KEY_SESIONES, 0)
        // Guarda el nuevo valor sumando 1 al anterior
        prefs.edit().putInt(KEY_SESIONES, actual + 1).apply()
    }

    // Registra una partida cada vez que el usuario termina de jugar
    // Ejemplo: si jugó 5 veces, el contador llega a 5
    fun registrarPartida(context: Context) {
        // Abre las preferencias guardadas
        val prefs = prefs(context)
        // Lee el número actual de partidas (0 si nunca se guardó)
        val actual = prefs.getInt(KEY_PARTIDAS, 0)
        // Guarda el nuevo valor sumando 1 al anterior
        prefs.edit().putInt(KEY_PARTIDAS, actual + 1).apply()
    }

    // Verifica todas las condiciones y muestra el panel solo si corresponde
    // Ejemplo: como un semáforo que solo deja pasar si todo está en verde
    fun mostrarSiCorresponde(context: Context, fragmentManager: FragmentManager) {
        // Solo muestra el panel si la función interna dice que sí
        if (deberíaMostrar(context)) {
            RatingFragment.show(context, fragmentManager)
        }
    }

    // Muestra el panel de calificación sin verificar ninguna condición
    // Útil para pruebas o para un botón manual de "Calificar app"
    fun mostrarForzado(context: Context, fragmentManager: FragmentManager) {
        // Muestra el panel directamente sin revisar reglas
        RatingFragment.show(context, fragmentManager)
    }

    // Devuelve true si el usuario ya dejó una calificación antes
    fun yaCalifco(context: Context): Boolean =
        // Lee el valor guardado, false si nunca calificó
        prefs(context).getBoolean(KEY_YA_CALIFICO, false)

    // Devuelve la calificación que dejó el usuario (0 si nunca calificó)
    fun obtenerCalificacion(context: Context): Int =
        // Lee el número de estrellas guardado, 0 si no hay ninguno
        prefs(context).getInt("calificacion", 0)

    // Borra todos los contadores y preferencias guardadas
    // Útil durante el desarrollo para probar como si fuera la primera vez
    fun resetearParaTesting(context: Context) {
        // Limpia todo el archivo de preferencias
        prefs(context).edit().clear().apply()
    }

    // Función interna que evalúa si se deben cumplir todas las condiciones
    // para mostrar el panel de calificación
    private fun deberíaMostrar(context: Context): Boolean {
        // Abre las preferencias guardadas
        val prefs = prefs(context)

        // Condición 1: si ya calificó, no mostrar nunca más
        if (prefs.getBoolean(KEY_YA_CALIFICO, false)) return false

        // Condición 2: si eligió "Ahora no", verifica si ya pasaron los 3 días
        // Ejemplo: si pospuso el lunes, no mostrar hasta el jueves
        val posponerHasta = prefs.getLong(KEY_POSPONER, 0L)
        // Si el tiempo actual es menor al límite, aún no toca mostrar
        if (System.currentTimeMillis() < posponerHasta) return false

        // Condición 3: verifica si ya abrió la app al menos 3 veces
        val sesiones = prefs.getInt(KEY_SESIONES, 0)
        // Si no llegó al mínimo, no mostrar todavía
        if (sesiones < SESIONES_MINIMAS) return false

        // Condición 4: verifica si ya jugó al menos 5 partidas
        val partidas = prefs.getInt(KEY_PARTIDAS, 0)
        // Si no llegó al mínimo, no mostrar todavía
        if (partidas < PARTIDAS_MINIMAS) return false

        // Si pasó todas las condiciones, sí se debe mostrar el panel
        return true
    }

    // Función auxiliar que abre el archivo de preferencias de la app
    // Ejemplo: como abrir una libreta donde se apuntan datos importantes
    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
