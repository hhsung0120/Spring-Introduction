package com.spring.introduction.service;

import com.spring.introduction.domain.Member;
import com.spring.introduction.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

//@Service
@Transactional
public class MemberService {

    //기존
//    private final MemberRepository memberRepository = new MemoryMemberRepository();

    //DI
    //2023-02-14 : MemberService 생성자에 MemberRepository 가 주입 되어 있는 상태고 MemberRepository 의 구현체는 SpringDataJpaMemberRepository 에 다중 상속 상태
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증

        long start = System.currentTimeMillis();
        try {
            memberRepository.save(member);
            return member.getId();
        } finally {
            long end = System.currentTimeMillis();
            long result = end - start;
            System.out.println("run time : " + result + "ms");
        }
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
