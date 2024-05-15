package com.timberlikeapp.presentation.mapper.character

import com.timberlikeapp.presentation.model.CharacterUiModel
import com.tinderlikeapp.domain.model.CharacterDomainModel
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test


class CharacterDomainToUiModelMapperTest {

    private lateinit var uiModelMapper: CharacterDomainToUiModelMapper

    @Before
    fun setUp() {
        uiModelMapper = CharacterDomainToUiModelMapperImpl()
    }

    @Test
    fun `given domain list when mapToUiModel called then return ui model list`() {
        val expectedUiModelList = listOf(
            CharacterUiModel(
                id = "1",
                name = "name",
                imageUrl = "image",
                status = "status",
                location = "location"
            ),
            CharacterUiModel(
                id = "2",
                name = "name2",
                imageUrl = "image2",
                status = "status2",
                location = "location2"
            )
        )

        val domainList = listOf(
            CharacterDomainModel(
                id = 1,
                name = "name",
                image = "image",
                status = "status",
                location = "location"
            ),
            CharacterDomainModel(
                id = 2,
                name = "name2",
                image = "image2",
                status = "status2",
                location = "location2"
            )
        )
        val result = uiModelMapper.mapToUiModel(domainList)
        assertArrayEquals(expectedUiModelList.toTypedArray(), result.values.toTypedArray())
    }

}