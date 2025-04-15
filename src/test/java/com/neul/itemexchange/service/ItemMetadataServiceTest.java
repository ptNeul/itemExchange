package com.neul.itemexchange.service;

import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import com.neul.itemexchange.exception.custom.ItemMetadataErrorCode;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.mapper.ItemMetadataMapper;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ItemMetadataServiceTest {

  @InjectMocks
  private ItemMetadataService itemMetadataService;

  @Mock
  private ItemMetadataRepository itemMetadataRepository;

  @Mock
  private ItemMetadataMapper itemMetadataMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void create() {
    // given
    ItemMetadataRequestDto dto = ItemMetadataRequestDto.builder()
        .itemName("라바돈의 죽음모자")
        .image("http://dummy.image.link/rabadon")
        .detail("주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다.")
        .build();

    ItemMetadata entity = ItemMetadata.builder()
        .itemId(1L)
        .itemName(dto.getItemName())
        .image(dto.getImage())
        .detail(dto.getDetail())
        .build();

    ItemMetadataResponseDto responseDto = ItemMetadataResponseDto.builder()
        .itemId(1L)
        .itemName(dto.getItemName())
        .image(dto.getImage())
        .detail(dto.getDetail())
        .build();

    when(itemMetadataRepository.existsByItemName(dto.getItemName())).thenReturn(false);
    when(itemMetadataRepository.existsByImage(dto.getImage())).thenReturn(false);
    when(itemMetadataMapper.toEntity(dto)).thenReturn(entity);
    when(itemMetadataRepository.save(entity)).thenReturn(entity);
    when(itemMetadataMapper.toDto(entity)).thenReturn(responseDto);

    // when
    ItemMetadataResponseDto result = itemMetadataService.create(dto);

    // then
    assertThat(result.getItemId()).isEqualTo(1L);
    assertThat(result.getItemName()).isEqualTo("라바돈의 죽음모자");
    verify(itemMetadataRepository).save(entity);
  }

  @Test
  void create_duplicateItemName() {
    // given
    ItemMetadataRequestDto dto = ItemMetadataRequestDto.builder()
        .itemName("라바돈의 죽음모자")
        .image("http://dummy.image.link/rabadon")
        .detail("주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다.")
        .build();

    when(itemMetadataRepository.existsByItemName(dto.getItemName())).thenReturn(true);

    // when
    // then
    assertThatThrownBy(() -> itemMetadataService.create(dto))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(DUPLICATE_ITEM_NAME.getMessage());
  }

  @Test
  void create_duplicateImage() {
    // given
    ItemMetadataRequestDto dto = ItemMetadataRequestDto.builder()
        .itemName("라바돈의 죽음모자")
        .image("http://dummy.image.link/rabadon")
        .detail("주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다.")
        .build();

    when(itemMetadataRepository.existsByItemName(dto.getItemName())).thenReturn(false);
    when(itemMetadataRepository.existsByImage(dto.getImage())).thenReturn(true);

    // when
    // then
    assertThatThrownBy(() -> itemMetadataService.create(dto))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(DUPLICATE_IMAGE.getMessage());
  }
}