package study.springboot.webapplicationdevelopment.repository;

import lombok.Getter;
import lombok.Setter;
import study.springboot.webapplicationdevelopment.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {

	private String memberName;

	private OrderStatus orderStatus;
}
