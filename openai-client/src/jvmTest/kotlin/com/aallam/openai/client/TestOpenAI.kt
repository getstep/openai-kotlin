package com.aallam.openai.client

import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.engine.EngineId
import com.aallam.openai.api.search.SearchRequest
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class TestOpenAI {

    private val openAI = OpenAI(config)

    @Test
    fun search() {
        runBlocking {
            val documents = listOf("White House", "hospital", "school")
            val query = "the president"
            val request = SearchRequest(documents, query)
            val response = openAI.search(EngineId.Davinci, request)
            assertEquals(documents.size, response.data.size)
        }
    }

    @Test
    fun engines() {
        runBlocking {
            val response = openAI.engines()
            assertNotEquals(0, response.data.size)
        }
    }

    @Test
    fun engine() {
        runBlocking {
            val engineId = EngineId.Davinci
            val response = openAI.engine(engineId)
            assertEquals(engineId, response.id)
        }
    }

    @Test
    fun completion() {
        runBlocking {
            val request = CompletionRequest(
                prompt = "Once upon a time",
                maxTokens = 5,
                temperature = 1.0,
                topP = 1.0,
                n = 1,
                stream = false,
                logprobs = null,
                stop = listOf("\n")
            )

            val response = openAI.createCompletion(EngineId.Davinci, request)
            assertNotNull(response.choices[0].text)
        }
    }
}
