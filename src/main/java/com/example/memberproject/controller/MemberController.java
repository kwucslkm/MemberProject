package com.example.memberproject.controller;

import com.example.memberproject.dto.MemberDTO;
import com.example.memberproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "index";
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("list", memberDTOList);
        return "memberPages/memberList";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/member/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/list";
    }

    @GetMapping("/member/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);

        return "memberPages/updateForm";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return "redirect:/list";
    }

    @GetMapping("/login")
    public String loginform() {
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        Model model) {
        int loginResult = memberService.loginChk(memberDTO);
        if (loginResult == 1) {
            System.out.println("로그인성공");
            session.setAttribute("loginEmail",memberDTO.getMemberEmail());
            model.addAttribute("loginResult",loginResult);
            return "memberPages/memberMain";
        } else {
            System.out.println("로그인실패");
            return "index";
        }

    }
    @PostMapping("/member/emailChk")
    public ResponseEntity loginChk(@RequestParam String memberEmail, Model model){

        System.out.println("memberEmail = " + memberEmail);
        MemberDTO memberDTO = memberService.memberEmailChk(memberEmail);
        if (memberDTO.length() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (memberDTO == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
