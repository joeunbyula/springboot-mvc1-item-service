package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);
        //when
        itemRepository.save(item);
        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(item);

    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("itemA", 10000, 10);
        Item item2 = new Item("itemB", 20000, 8);
        Item item3 = new Item("itemC", 40000, 5);
        //when
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        //then
        List<Item> items = itemRepository.findAll();
        assertThat(items.size()).isEqualTo(3);
        assertThat(items).contains(item1,item2,item3);

    }

    @Test
    void updateItem() {
        //given
        Item item1 = new Item("itemA", 10000, 10);
        itemRepository.save(item1);
        Long id = item1.getId();
        //when
        Item updateParam = new Item("itemB", 20000, 8);
        itemRepository.update(id, updateParam);

        Item findItem = itemRepository.findById(id);
        //then
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }
}