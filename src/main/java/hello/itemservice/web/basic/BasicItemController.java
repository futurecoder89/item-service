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
///basic/items 경로로오면 items메서드가 작동함 그러면 모델에 items라고 컬렉션이 담김. 그후 basic/items경로를 리턴
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;
    //상품 목록 페이지
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    //상품 상세 페이지
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }
    //상품 등록 폼
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
    //@PostMapping("/add")   //addForm에 method=post이기때문에 이게 호출됨
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
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
    public String addItemV2(@ModelAttribute("item") Item item ) {


        itemRepository.save(item);

        //model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        //@ModelAttribute("item")에서
        // ("item)을 생략한다면 클래스명인
        // Item에서 첫글자인 i를 소문자로
        // 전환해서 모델어트리뷰트에 넣어준다
        itemRepository.save(item);

        //model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);

        //model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); //PRG 적용


    }
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}"; //url 노출문제 해결한 버전


    }
    //상품 수정
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    //상품 수정 저장버튼 action
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}