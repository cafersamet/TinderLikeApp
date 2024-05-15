package com.tinderlikeapp.domain.usecase

import app.cash.turbine.test
import com.tinderlikeapp.data.models.character.APICharactersRequest
import com.tinderlikeapp.data.models.character.APICharactersResponse
import com.tinderlikeapp.data.models.character.APIInfo
import com.tinderlikeapp.data.network.APIError
import com.tinderlikeapp.data.network.APIErrorCode.UNKNOWN
import com.tinderlikeapp.data.network.APIResult
import com.tinderlikeapp.data.network.repository.CharactersRepository
import com.tinderlikeapp.domain.mapper.CharacterEntityToDomainModelMapper
import com.tinderlikeapp.test.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.slot
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCharactersUseCaseTest {

    @get: Rule
    val mockKRule = MockKRule(this)

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    @MockK
    lateinit var repository: CharactersRepository

    @MockK
    lateinit var characterEntityToDomainModelMapper: CharacterEntityToDomainModelMapper

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Before
    fun setUp() {
        getCharactersUseCase = GetCharactersUseCase(
            repository,
            characterEntityToDomainModelMapper,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `given valid params when execute method called then return success result`() = runTest {
        val requestBodyCapturingSlot = slot<APICharactersRequest>()
        coEvery {
            repository.getCharacters(capture(requestBodyCapturingSlot))
        }.returns(
            APIResult.Success(
                data = APICharactersResponse(
                    info = APIInfo(
                        count = 20,
                        pages = 1,
                        next = null,
                        prev = null
                    ),
                    results = emptyList()
                )
            )
        )
        every {
            characterEntityToDomainModelMapper.mapToDomainModel(any())
        }.returns(emptyList())

        val param = GetCharactersUseCaseParam(
            page = 1,
            next = null
        )
        val resultFlow = getCharactersUseCase
            .execute(param)
            .asFlow()

        resultFlow.test {
            val useCaseResult = awaitItem()
            assertTrue(useCaseResult is GetCharacterUseCaseResult.Success)
            val successResult = useCaseResult as GetCharacterUseCaseResult.Success
            assertTrue(successResult.characters.isEmpty())
            assertNull(successResult.next)
            awaitComplete()
        }
        val capturedRequestBody = requestBodyCapturingSlot.captured
        assertEquals(1, capturedRequestBody.page)
        assertNull(capturedRequestBody.next)
    }

    @Test
    fun `given invalid params when execute method called then return error result`() = runTest {
        val expectedMessage = "Something went wrong"
        val requestBodyCapturingSlot = slot<APICharactersRequest>()
        coEvery {
            repository.getCharacters(capture(requestBodyCapturingSlot))
        }.returns(
            APIResult.Error(
                apiError = APIError(
                    code = UNKNOWN,
                    message = expectedMessage
                )
            )
        )
        every {
            characterEntityToDomainModelMapper.mapToDomainModel(any())
        }.returns(emptyList())

        val param = GetCharactersUseCaseParam(
            page = 1,
            next = null
        )
        val resultFlow = getCharactersUseCase
            .execute(param)
            .asFlow()

        resultFlow.test {
            val useCaseResult = awaitItem()
            assertTrue(useCaseResult is GetCharacterUseCaseResult.Error)
            val errorResult = useCaseResult as GetCharacterUseCaseResult.Error
            assertEquals(expectedMessage, errorResult.message)
            cancelAndConsumeRemainingEvents()
        }
        val capturedRequestBody = requestBodyCapturingSlot.captured
        assertEquals(1, capturedRequestBody.page)
        assertNull(capturedRequestBody.next)
    }

    @Test
    fun `given valid params with next is not null when execute method called then return success result`() =
        runTest {
            val expectedResult = GetCharacterUseCaseResult.Success(
                characters = emptyList(),
                next = "https://rickandmortyapi.com/api/character/?page=3"
            )
            val requestBodyCapturingSlot = slot<APICharactersRequest>()
            coEvery {
                repository.getCharacters(capture(requestBodyCapturingSlot))
            }.returns(
                APIResult.Success(
                    data = APICharactersResponse(
                        info = APIInfo(
                            count = 20,
                            pages = 2,
                            next = "https://rickandmortyapi.com/api/character/?page=3",
                            prev = "https://rickandmortyapi.com/api/character/?page=2"
                        ),
                        results = emptyList()
                    )
                )
            )
            every {
                characterEntityToDomainModelMapper.mapToDomainModel(any())
            }.returns(emptyList())

            val param = GetCharactersUseCaseParam(
                page = 1,
                next = "https://rickandmortyapi.com/api/character/?page=2"
            )
            val resultFlow = getCharactersUseCase
                .execute(param)
                .asFlow()

            resultFlow.test {
                val useCaseResult = awaitItem()
                assertTrue(useCaseResult is GetCharacterUseCaseResult.Success)
                val successResult = useCaseResult as GetCharacterUseCaseResult.Success
                assertEquals(expectedResult, successResult)
                awaitComplete()
            }
            val capturedRequestBody = requestBodyCapturingSlot.captured
            assertEquals(1, capturedRequestBody.page)
            assertEquals("https://rickandmortyapi.com/api/character/?page=2", capturedRequestBody.next)
        }

}