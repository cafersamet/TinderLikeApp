package com.timberlikeapp.presentation.listsource.character

import com.timberlikeapp.presentation.mapper.character.CharacterDomainToUiModelMapper
import com.timberlikeapp.presentation.model.CharacterUiModel
import com.tinderlikeapp.test.CoroutineTestRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterListSourceTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    lateinit var characterDomainToUiModelMapper: CharacterDomainToUiModelMapper

    private lateinit var characterListSource: CharacterListSource

    @Before
    fun setUp() {
        characterListSource = CharacterListSource(
            characterDomainToUiModelMapper = characterDomainToUiModelMapper,
            defaultDispatcher = coroutineTestRule.testDispatcher
        )
        // Add character ui model list before each test
        val characterUiModelMap = linkedMapOf(
            "1" to CharacterUiModel(
                id = "1",
                name = "name",
                imageUrl = "image",
                status = "status",
                location = "location"
            ),
            "2" to CharacterUiModel(
                id = "2",
                name = "name2",
                imageUrl = "image2",
                status = "status2",
                location = "location2"
            )
        )
        every {
            characterDomainToUiModelMapper.mapToUiModel(any())
        }.returns(characterUiModelMap)
    }

    @Test
    fun `given empty list when addList method called then update character list correctly`() =
        runTest {
            val expectedUiModelMap = linkedMapOf(
                "1" to CharacterUiModel(
                    id = "1",
                    name = "name",
                    imageUrl = "image",
                    status = "status",
                    location = "location"
                ),
                "2" to CharacterUiModel(
                    id = "2",
                    name = "name2",
                    imageUrl = "image2",
                    status = "status2",
                    location = "location2"
                )
            )

            characterListSource.addList(emptyList())

            assertArrayEquals(
                expectedUiModelMap.values.toTypedArray(),
                characterListSource.characterList.toTypedArray(),
            )
            verify { characterDomainToUiModelMapper.mapToUiModel(any()) }
        }

    @Test
    fun `given id when removeItem method called then remove character from list correctly`() =
        runTest {
            val expectedUiModelMap = linkedMapOf(
                "1" to CharacterUiModel(
                    id = "1",
                    name = "name",
                    imageUrl = "image",
                    status = "status",
                    location = "location"
                )
            )

            characterListSource.addList(emptyList())
            characterListSource.removeItem("2")

            assertArrayEquals(
                expectedUiModelMap.values.toTypedArray(),
                characterListSource.characterList.toTypedArray(),
            )
        }

    @Test
    fun `given id which is not exist in the list when removeItem method called then do not update character list`() =
        runTest {
            val expectedUiModelMap = linkedMapOf(
               "1" to CharacterUiModel(
                    id = "1",
                    name = "name",
                    imageUrl = "image",
                    status = "status",
                    location = "location"
                ),
                "2" to CharacterUiModel(
                    id = "2",
                    name = "name2",
                    imageUrl = "image2",
                    status = "status2",
                    location = "location2"
                )
            )

            characterListSource.addList(emptyList())
            characterListSource.removeItem("5")

            assertArrayEquals(
                expectedUiModelMap.values.toTypedArray(),
                characterListSource.characterList.toTypedArray(),
            )
        }

}