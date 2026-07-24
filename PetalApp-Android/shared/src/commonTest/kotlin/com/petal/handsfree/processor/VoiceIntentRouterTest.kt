package com.petal.handsfree.processor

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VoiceIntentRouterTest {

    // ─── CALL ─────────────────────────────────────────────────────────────────

    @Test
    fun llamaAWithPhoneNumberRoutesToCallWithDigits() {
        val result = VoiceIntentRouter.tryOffline("llama a 611223344")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_CALL, result.intent)
        assertEquals("611223344", result.argument)
    }

    @Test
    fun llamaAlWithPhoneNumberRoutesToCallWithDigits() {
        val result = VoiceIntentRouter.tryOffline("llama al 666555444")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_CALL, result.intent)
        assertEquals("666555444", result.argument)
    }

    @Test
    fun llamaAContactNameKeepsNameAsArgument() {
        val result = VoiceIntentRouter.tryOffline("llama a María")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_CALL, result.intent)
        assertEquals("María", result.argument)
    }

    @Test
    fun marcarRoutesToCall() {
        val result = VoiceIntentRouter.tryOffline("marcar 912345678")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_CALL, result.intent)
    }

    // ─── NAVIGATE ─────────────────────────────────────────────────────────────

    @Test
    fun navegaARoutesToNavigateWithDestination() {
        val result = VoiceIntentRouter.tryOffline("navega a Casa Carmela")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_NAVIGATE, result.intent)
        assertEquals("Casa Carmela", result.argument)
    }

    @Test
    fun irARoutesToNavigate() {
        val result = VoiceIntentRouter.tryOffline("ir a la farmacia")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_NAVIGATE, result.intent)
        assertEquals("la farmacia", result.argument)
    }

    @Test
    fun llevameARoutesToNavigate() {
        val result = VoiceIntentRouter.tryOffline("llévame a Valencia Centro")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_NAVIGATE, result.intent)
        assertEquals("Valencia Centro", result.argument)
    }

    @Test
    fun ponRumboARoutesToNavigate() {
        val result = VoiceIntentRouter.tryOffline("pon rumbo a Madrid")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_NAVIGATE, result.intent)
        assertEquals("Madrid", result.argument)
    }

    // ─── BATTERY ──────────────────────────────────────────────────────────────

    @Test
    fun bateriaRoutesToBattery() {
        val result = VoiceIntentRouter.tryOffline("batería")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_BATTERY, result.intent)
        assertEquals("", result.argument)
    }

    @Test
    fun cuantaBateriaRoutesToBattery() {
        val result = VoiceIntentRouter.tryOffline("cuánta batería")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_BATTERY, result.intent)
    }

    // ─── TIME ─────────────────────────────────────────────────────────────────

    @Test
    fun queHoraEsRoutesToTime() {
        val result = VoiceIntentRouter.tryOffline("qué hora es")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_TIME, result.intent)
        assertEquals("", result.argument)
    }

    @Test
    fun horaActualRoutesToTime() {
        val result = VoiceIntentRouter.tryOffline("hora actual")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_TIME, result.intent)
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    @Test
    fun tengoHambreRoutesToSearchWithRestauranteCerca() {
        val result = VoiceIntentRouter.tryOffline("tengo hambre")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_SEARCH, result.intent)
        assertTrue(result.argument.contains("restaurante"), "Expected 'restaurante cerca', got '${result.argument}'")
    }

    @Test
    fun buscaUnaFarmaciaRoutesToSearch() {
        val result = VoiceIntentRouter.tryOffline("busca una farmacia")
        assertNotNull(result)
        assertEquals(VoiceIntentRouter.INTENT_SEARCH, result.intent)
    }

    // ─── No offline match — goes to Gemini ────────────────────────────────────

    @Test
    fun unknownPhraseReturnsNull() {
        assertNull(VoiceIntentRouter.tryOffline("texto aleatorio sin ningún patrón conocido"))
    }

    @Test
    fun ponmeMusicaReturnsNullFromOfflineAndGoesToGemini() {
        // Music is not handled offline — Gemini picks it up
        assertNull(VoiceIntentRouter.tryOffline("ponme música de Coldplay"))
    }

    @Test
    fun trafficQuestionReturnsNullFromOffline() {
        assertNull(VoiceIntentRouter.tryOffline("cómo está el tráfico"))
    }

    @Test
    fun emptyStringReturnsNull() {
        assertNull(VoiceIntentRouter.tryOffline(""))
    }
}
