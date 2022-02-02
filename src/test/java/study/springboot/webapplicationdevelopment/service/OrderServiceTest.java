package study.springboot.webapplicationdevelopment.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springboot.webapplicationdevelopment.domain.Address;
import study.springboot.webapplicationdevelopment.domain.Member;
import study.springboot.webapplicationdevelopment.domain.Order;
import study.springboot.webapplicationdevelopment.domain.OrderStatus;
import study.springboot.webapplicationdevelopment.domain.item.Book;
import study.springboot.webapplicationdevelopment.domain.item.Item;
import study.springboot.webapplicationdevelopment.exception.NotEnoughStockException;
import study.springboot.webapplicationdevelopment.repository.OrderRepository;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	public void 상품주문() throws Exception {
		// given
		Member member = createMember("member");
		Item book = createBook("book", 10000, 10);

		int orderCount = 2;

		// when
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
		assertEquals("주문한 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
	}

	@Test
	public void 상품주문_재고수량초과() throws Exception {
		// given
		Member member = createMember("member");
		Item book = createBook("book", 10000, 10);

		int orderCount = 11;

		// when -> then
		assertThrows(
			NotEnoughStockException.class,
			() -> orderService.order(member.getId(), book.getId(), orderCount),
			"재고 수량 부족 예외가 발생해야 한다."
		);
	}

	@Test
	public void 주문취소() throws Exception {
		// given
		Member member = createMember("name");
		Item book = createBook("book", 10000, 10);

		int orderCount = 2;

		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order getOrder = orderRepository.findOne(orderId);

		assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
	}

	private Member createMember(String name) {
		Member member = new Member();
		member.setName(name);
		member.setAddress(new Address("서울", "테해란로", "000-000"));
		em.persist(member);
		return member;
	}

	private Item createBook(String name, int price, int stockQuantity) {
		Item book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}
}