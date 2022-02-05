package study.springboot.webapplicationdevelopment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springboot.webapplicationdevelopment.domain.Delivery;
import study.springboot.webapplicationdevelopment.domain.Member;
import study.springboot.webapplicationdevelopment.domain.Order;
import study.springboot.webapplicationdevelopment.domain.OrderItem;
import study.springboot.webapplicationdevelopment.domain.item.Item;
import study.springboot.webapplicationdevelopment.repository.ItemRepository;
import study.springboot.webapplicationdevelopment.repository.MemberRepository;
import study.springboot.webapplicationdevelopment.repository.OrderRepository;
import study.springboot.webapplicationdevelopment.repository.OrderSearch;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	/*
	 * 주문
	 * */
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {

		// 엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);

		// 배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());

		// 주문상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);

		// 주문 저장
		orderRepository.save(order);

		return order.getId();
	}

	/*
	 * 주문 취소
	 * */
	@Transactional
	public void cancelOrder(Long orderId) {

		// 주문 엔티티
		Order order = orderRepository.findOne(orderId);

		// 주문 취소
		order.cancel();
	}

	/*
	 * 검색
	 * */
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByCriteria(orderSearch);
	}

}
