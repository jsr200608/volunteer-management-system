
package com.example.volunteer.controller;

import com.example.volunteer.dto.request.MemberCreateRequest;
import com.example.volunteer.dto.request.MemberUpdateRequest;
import com.example.volunteer.dto.response.ApiResponse;
import com.example.volunteer.dto.response.MemberResponse;
import com.example.volunteer.dto.response.PageResponse;
import com.example.volunteer.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ApiResponse<PageResponse<MemberResponse>> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String status) {
        PageResponse<MemberResponse> response = memberService.getMembers(page, size, college, grade, status);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse response = memberService.getMemberById(id);
        return ApiResponse.success(response);
    }

    @PostMapping
    public ApiResponse<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ApiResponse.success("成员添加成功", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
        MemberResponse response = memberService.updateMember(id, request);
        return ApiResponse.success("成员信息更新成功", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ApiResponse.success("成员删除成功", null);
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<MemberResponse>> searchMembers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<MemberResponse> response = memberService.searchMembers(keyword, page, size);
        return ApiResponse.success(response);
    }
}
