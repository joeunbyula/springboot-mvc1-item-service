package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item",item);

        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item",item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item updateParam) {

        itemRepository.update(itemId, updateParam);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam Integer price,
                            @RequestParam Integer quantity,
                            Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
        itemRepository.save(item);

        /*
            @ModelAttribute("item")으로 설정해주면 자동으로 model이 넘어간다. (oh..wow..)
            Model model 생략가능..
         */
        //model.addAttribute("item", item);

        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);

        /*
            @ModelAttribute으로 설정해주면
            클래스 이름의 첫글자를 소문자로 해서 "item" key로
            자동으로 model이 넘어간다.
            Model model 생략가능..
         */
        //model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }


    /**
     * PRG
     * POST -> Redirect -> GET
     * V4 버전에서 저장 후에 return해서 item상세로 가게되면
     * 마지막 처리가 POST방식이므로 새로고침 시 다시 save하는 오류 발생
     * -> 해결 방법은 save 후 redirect를 이용하여 GET방식으로 item상세로 이동
     *    새로고침헤도 마지막 처리가 GET방식이기때문에 다시 save하는 오류를 막을 수 있다.
     */
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        Long id = item.getId();
        return "redirect:/basic/items/"+id;
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        Item item1 = new Item("itemA", 10000, 10);
        Item item2 = new Item("itemB", 20000, 10);

        itemRepository.save(item1);
        itemRepository.save(item2);
    }
}
