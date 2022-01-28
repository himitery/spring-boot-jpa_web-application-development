package study.springboot.webapplicationdevelopment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springboot.webapplicationdevelopment.domain.Member;
import study.springboot.webapplicationdevelopment.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;

	@Test
	public void 회원가입() throws Exception {
		// given
		Member member = new Member();
		member.setName("member");

		// when
		Long savedId = memberService.join(member);

		// then
		assertEquals(member, memberRepository.findOne(savedId));
	}

	@Test
	public void 중복_회원_예외() throws Exception {
		// given
		Member memberA = new Member();
		memberA.setName("member");

		Member memberB = new Member();
		memberB.setName("member");

		// when
		memberService.join(memberA);

		// then
		assertThrows(IllegalStateException.class, () -> {
			memberService.join(memberB);
		});
	}
}