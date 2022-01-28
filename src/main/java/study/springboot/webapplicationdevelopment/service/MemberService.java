package study.springboot.webapplicationdevelopment.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springboot.webapplicationdevelopment.domain.Member;
import study.springboot.webapplicationdevelopment.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	/*
	 * 회원 가입
	 * */
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	/*
	 * 회원 목록 조회
	 * */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	/*
	 * 회원 조회
	 * */
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

	/*
	 * 중복 회원 검증
	 * */
	private void validateDuplicateMember(Member member) {
		List<Member> findMembers = memberRepository.findByName(member.getName());
		if (!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
}
