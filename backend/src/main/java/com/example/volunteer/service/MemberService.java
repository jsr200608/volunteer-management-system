
package com.example.volunteer.service;

import com.example.volunteer.dto.request.MemberCreateRequest;
import com.example.volunteer.dto.request.MemberUpdateRequest;
import com.example.volunteer.dto.response.MemberResponse;
import com.example.volunteer.dto.response.PageResponse;
import com.example.volunteer.entity.Member;
import com.example.volunteer.entity.User;
import com.example.volunteer.repository.MemberRepository;
import com.example.volunteer.repository.UserRepository;
import com.example.volunteer.util.PasswordUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    public MemberService(MemberRepository memberRepository, UserRepository userRepository, PasswordUtil passwordUtil) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        if (memberRepository.findByStudentId(request.getStudentId()).isPresent()) {
            throw new RuntimeException("学号已存在");
        }

        User user = new User();
        user.setUsername(request.getStudentId());
        user.setPassword(passwordUtil.encode("123456"));
        user.setRole("MEMBER");
        user.setStatus("ACTIVE");
        user = userRepository.save(user);

        Member member = new Member();
        member.setUserId(user.getId());
        member.setStudentId(request.getStudentId());
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setGrade(request.getGrade());
        member.setCollege(request.getCollege());
        member.setMajor(request.getMajor());
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setJoinDate(request.getJoinDate());
        member.setStatus("ACTIVE");
        member = memberRepository.save(member);

        return convertToResponse(member);
    }

    public PageResponse<MemberResponse> getMembers(int page, int size, String college, String grade, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Member> memberPage;

        if (college != null || grade != null || status != null) {
            memberPage = memberRepository.findByFilters(college, grade, status, pageable);
        } else {
            memberPage = memberRepository.findAll(pageable);
        }

        PageResponse<MemberResponse> response = new PageResponse<>();
        response.setContent(memberPage.getContent().stream().map(this::convertToResponse).toList());
        response.setPageNumber(memberPage.getNumber());
        response.setPageSize(memberPage.getSize());
        response.setTotalElements(memberPage.getTotalElements());
        response.setTotalPages(memberPage.getTotalPages());

        return response;
    }

    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("成员不存在"));
        return convertToResponse(member);
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("成员不存在"));

        if (request.getName() != null) member.setName(request.getName());
        if (request.getGender() != null) member.setGender(request.getGender());
        if (request.getGrade() != null) member.setGrade(request.getGrade());
        if (request.getCollege() != null) member.setCollege(request.getCollege());
        if (request.getMajor() != null) member.setMajor(request.getMajor());
        if (request.getPhone() != null) member.setPhone(request.getPhone());
        if (request.getEmail() != null) member.setEmail(request.getEmail());
        if (request.getJoinDate() != null) member.setJoinDate(request.getJoinDate());
        if (request.getStatus() != null) member.setStatus(request.getStatus());

        member = memberRepository.save(member);
        return convertToResponse(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("成员不存在"));
        member.setStatus("LEFT");
        memberRepository.save(member);

        User user = userRepository.findById(member.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus("INACTIVE");
        userRepository.save(user);
    }

    public PageResponse<MemberResponse> searchMembers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Member> memberPage = memberRepository.searchByKeyword(keyword, pageable);

        PageResponse<MemberResponse> response = new PageResponse<>();
        response.setContent(memberPage.getContent().stream().map(this::convertToResponse).toList());
        response.setPageNumber(memberPage.getNumber());
        response.setPageSize(memberPage.getSize());
        response.setTotalElements(memberPage.getTotalElements());
        response.setTotalPages(memberPage.getTotalPages());

        return response;
    }

    private MemberResponse convertToResponse(Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setStudentId(member.getStudentId());
        response.setName(member.getName());
        response.setGender(member.getGender());
        response.setGrade(member.getGrade());
        response.setCollege(member.getCollege());
        response.setMajor(member.getMajor());
        response.setPhone(member.getPhone());
        response.setEmail(member.getEmail());
        response.setVolunteerHours(member.getVolunteerHours());
        response.setJoinDate(member.getJoinDate());
        response.setStatus(member.getStatus());
        return response;
    }
}
