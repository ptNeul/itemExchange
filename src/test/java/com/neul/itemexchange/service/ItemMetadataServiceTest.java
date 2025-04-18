package com.neul.itemexchange.service;


import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.DUPLICATE_IMAGE;
import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.DUPLICATE_ITEM_NAME;
import static com.neul.itemexchange.exception.custom.ItemMetadataErrorCode.ITEM_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.neul.itemexchange.domain.ItemMetadata;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataPatchRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataRequestDto;
import com.neul.itemexchange.dto.itemmetadata.ItemMetadataResponseDto;
import com.neul.itemexchange.exception.custom.ItemMetadataException;
import com.neul.itemexchange.mapper.ItemMetadataMapper;
import com.neul.itemexchange.repository.ItemMetadataRepository;
import java.util.List;
import java.util.Optional;
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

  @Test
  void readAll() {
    // given
    ItemMetadata item1 = ItemMetadata.builder()
        .itemId(1L)
        .itemName("라바돈의 죽음모자")
        .image("http://dummy.image.link/rabadon")
        .detail("주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다.")
        .build();

    ItemMetadata item2 = ItemMetadata.builder()
        .itemId(2L)
        .itemName("무한의 대검")
        .image("http://dummy.image.link/infinity")
        .detail("공격력 65\n"
            + " 치명타 확률 25%\n"
            + " 치명타 피해량 40%")
        .build();

    when(itemMetadataRepository.findAll()).thenReturn(List.of(item1, item2));

    when(itemMetadataMapper.toDto(item1)).thenReturn(new ItemMetadataResponseDto(1L,
        "라바돈의 죽음모자",
        "http://dummy.image.link/rabadon",
        "주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다."));
    when(itemMetadataMapper.toDto(item2)).thenReturn(new ItemMetadataResponseDto(2L,
        "무한의 대검",
        "http://dummy.image.link/infinity",
        "공격력 65\n"
            + " 치명타 확률 25%\n"
            + " 치명타 피해량 40%"));

    // when
    List<ItemMetadataResponseDto> result = itemMetadataService.readAll();

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getItemName()).isEqualTo("라바돈의 죽음모자");
    assertThat(result.get(1).getItemName()).isEqualTo("무한의 대검");
  }

  @Test
  void readOne() {
    // given
    Long itemId = 1L;
    ItemMetadata item = ItemMetadata.builder()
        .itemId(itemId)
        .itemName("라바돈의 죽음모자")
        .image("http://dummy.image.link/rabadon")
        .detail("주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다.")
        .build();

    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.of(item));
    when(itemMetadataMapper.toDto(item)).thenReturn(new ItemMetadataResponseDto(itemId,
        "라바돈의 죽음모자",
        "http://dummy.image.link/rabadon",
        "주문력 130\n"
            + " 신비한 작품\n"
            + " 총 주문력이 30% 증가합니다."));

    // when
    ItemMetadataResponseDto result = itemMetadataService.readOne(itemId);

    // then
    assertThat(result.getItemId()).isEqualTo(itemId);
    assertThat(result.getItemName()).isEqualTo("라바돈의 죽음모자");
  }

  @Test
  void readOne_itemNotFound() {
    // given
    when(itemMetadataRepository.findById(999L)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemMetadataService.readOne(999L))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(ITEM_NOT_FOUND.getMessage());
  }

  @Test
  void patch() {
    // given
    Long itemId = 1L;
    ItemMetadata item = ItemMetadata.builder()
        .itemId(itemId)
        .itemName("Sword")
        .image("sword.png")
        .detail("old detail")
        .build();

    ItemMetadataPatchRequestDto dto = new ItemMetadataPatchRequestDto();
    dto.setItemName("New Sword");
    dto.setImage("new-sword.png");
    dto.setDetail("new detail");

    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.of(item));
    when(itemMetadataRepository.existsByItemName("New Sword")).thenReturn(false);
    when(itemMetadataRepository.existsByImage("new-sword.png")).thenReturn(false);

    ItemMetadataResponseDto expected = new ItemMetadataResponseDto(itemId, "New Sword",
        "new-sword.png", "new detail");
    when(itemMetadataMapper.toDto(item)).thenReturn(expected);

    // when
    ItemMetadataResponseDto result = itemMetadataService.patch(itemId, dto);

    // then
    assertThat(result.getItemName()).isEqualTo("New Sword");
    assertThat(result.getImage()).isEqualTo("new-sword.png");
    assertThat(result.getDetail()).isEqualTo("new detail");
  }

  @Test
  void patch_itemNotFound() {
    // given
    Long itemId = 999L;
    ItemMetadataPatchRequestDto dto = new ItemMetadataPatchRequestDto();
    dto.setItemName("New Name");

    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemMetadataService.patch(itemId, dto))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(ITEM_NOT_FOUND.getMessage());
  }

  @Test
  void patch_duplicateItemName() {
    // given
    Long itemId = 1L;
    ItemMetadata item = ItemMetadata.builder()
        .itemId(itemId)
        .itemName("Sword")
        .image("sword.png")
        .detail("detail")
        .build();

    ItemMetadataPatchRequestDto dto = new ItemMetadataPatchRequestDto();
    dto.setItemName("ExistingItem");

    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.of(item));
    when(itemMetadataRepository.existsByItemName("ExistingItem")).thenReturn(true);

    // when
    // then
    assertThatThrownBy(() -> itemMetadataService.patch(itemId, dto))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(DUPLICATE_ITEM_NAME.getMessage());
  }

  @Test
  void delete() {
    // given
    Long itemId = 1L;
    ItemMetadata item = ItemMetadata.builder()
        .itemId(itemId)
        .itemName("Sword")
        .image("sword.png")
        .detail("detail")
        .build();

    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.of(item));

    // when
    itemMetadataService.delete(itemId);

    // then
    verify(itemMetadataRepository).delete(item);
  }

  @Test
  void delete_itemNotFound() {
    // given
    Long itemId = 999L;
    when(itemMetadataRepository.findById(itemId)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> itemMetadataService.delete(itemId))
        .isInstanceOf(ItemMetadataException.class)
        .hasMessage(ITEM_NOT_FOUND.getMessage());
  }
}
