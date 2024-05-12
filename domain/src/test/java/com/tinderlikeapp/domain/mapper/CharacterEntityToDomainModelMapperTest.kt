package com.tinderlikeapp.domain.mapper

import com.tinderlikeapp.data.models.character.APICharacter
import com.tinderlikeapp.data.models.character.APILocation
import com.tinderlikeapp.data.models.character.APIOrigin
import com.tinderlikeapp.domain.model.CharacterDomainModel
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class CharacterEntityToDomainModelMapperTest {

    private lateinit var characterEntityToDomainModelMapper: CharacterEntityToDomainModelMapper

    @Before
    fun setUp() {
        characterEntityToDomainModelMapper = CharacterEntityToDomainModelMapperImpl()
    }

    @Test
    fun `given entity list when mapToDomainModel called then return domain model list`() {
        val expectedDomainList = listOf(
            CharacterDomainModel(
                id = 1,
                name = "name1",
                image = "image1",
                status = "status1",
                location = "location1"
            ),
            CharacterDomainModel(
                id = 2,
                name = "name2",
                image = "image2",
                status = "status2",
                location = "location2"
            )
        )

        val entityList = listOf(
            APICharacter(
                id = 1,
                name = "name1",
                image = "image1",
                status = "status1",
                location = APILocation(
                    name = "location1",
                    url = "url1"
                ),
                gender = "gender1",
                species = "species1",
                origin = APIOrigin(
                    name = "origin1",
                    url = "url1"
                ),
                episode = listOf("episode1", "episode2"),
                created = "created1",
                type = "type1",
                url = "url1",
            ),
            APICharacter(
                id = 2,
                name = "name2",
                image = "image2",
                status = "status2",
                location = APILocation(
                    name = "location2",
                    url = "url2"
                ),
                gender = "gender2",
                species = "species2",
                origin = APIOrigin(
                    name = "origin2",
                    url = "url2"
                ),
                episode = listOf("episode3", "episode4"),
                created = "created2",
                type = "type2",
                url = "url2",
            )
        )

        val actualDomainList = characterEntityToDomainModelMapper.mapToDomainModel(entityList)
        assertArrayEquals(expectedDomainList.toTypedArray(), actualDomainList.toTypedArray())
    }
}