package study.springboot.webapplicationdevelopment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.springboot.webapplicationdevelopment.domain.Member;
import study.springboot.webapplicationdevelopment.domain.item.Item;
import study.springboot.webapplicationdevelopment.service.ItemService;
import study.springboot.webapplicationdevelopment.service.MemberService;
import study.springboot.webapplicationdevelopment.service.OrderService;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;

	@GetMapping("/order")
	public String createForm(Model model) {
		List<Member> members = memberService.findMembers();
		List<Item> items = itemService.findItems();

		model.addAttribute("members", members);
		model.addAttribute("items", items);

		return "order/orderForm";
	}

	@PostMapping("/order")
	public String order(
		@RequestParam("memberId") Long memberId,
		@RequestParam("itemId") Long itemId,
		@RequestParam("count") int count
	) {
		orderService.order(memberId, itemId, count);

		return "redirect:/orders";
	}
}
