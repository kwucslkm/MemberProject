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
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody MemberDTO memberDTO) {
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return new ResponseEntity(memberDTO,HttpStatus.OK);
    }
//@PostMapping("/save")
//    public String save(@ModelAttribute MemberDTO memberDTO) {
//        memberService.save(memberDTO);
//        return "index";
//    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("list", memberDTOList);
        return "memberPages/memberList";
    }


    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) throws Exception {
        System.out.println("넘어온 아이디" + id);
        MemberDTO memberDTO = memberService.findById(id);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }
//    @GetMapping("/axios/{id}")
//    public ResponseEntity detailAxios(@PathVariable Long id) throws Exception {
//        MemberDTO memberDTO = memberService.findById(id);
//        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
//    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")//세션으로 findbyemail로 바꿔보기
    public ResponseEntity updateForm(@PathVariable Long id, Model model) {
        System.out.println("수정할라고id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
//        model.addAttribute("member", memberDTO);

        return new ResponseEntity<>(memberDTO,HttpStatus.OK);
    }
//public String updateForm(@PathVariable Long id, Model model) {
//        System.out.println("id = " + id);
//        MemberDTO memberDTO = memberService.findById(id);
//        model.addAttribute("member", memberDTO);
//
//        return "memberPages/memberUpdate";
//    }

    @PostMapping("/axios/update")
    public ResponseEntity memberUpdate(@RequestBody MemberDTO memberDTO) {
//        System.out.println("memberDTO = " + memberDTO);
        memberService.update(memberDTO);
//        return "redirect:/member/list";
        return new ResponseEntity<>(memberDTO,HttpStatus.OK);
    }
//    public String update(@ModelAttribute MemberDTO memberDTO) {
//        System.out.println("memberDTO = " + memberDTO);
//        memberService.update(memberDTO);
//        return "redirect:/member/list";
//    }
    @GetMapping("/login")
//    public String loginForm() {
//        return "memberPages/memberLogin";
//    }
    public String loginForm(@RequestParam(value = "redirectURI",defaultValue = "/member/mypage") String redirectURI
            ,Model model) {
        model.addAttribute("redirectURI",redirectURI);
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        @RequestParam("redirectURI") String redirectURI,Model model) {
        int loginResult = memberService.loginChk(memberDTO);
        if (loginResult == 1) {
            System.out.println("로그인성공");
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            model.addAttribute("loginResult", loginResult);
//            return "memberPages/memberMain";
            // 로그인 성공하면 사용자가 직전에 요청한 주소로 redirect
            // 인터셉터에 걸리지 않고 처음부터 로그인하는 사용자였다면
            // redirect:/member/mypage 로 요청되며, memberMain 화면으로 전환됨.
            return "redirect:" + redirectURI;
        } else {
            System.out.println("로그인실패");
            return "memberPages/memberLogin";
        }

    }

    @PostMapping("/login/axios")
    public ResponseEntity memberLogin(@RequestBody MemberDTO memberDTO, HttpSession session) throws Exception {
        memberService.loginAxios(memberDTO);
        session.setAttribute("loginEmail", memberDTO.getMemberEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/emailChk")
    public ResponseEntity loginChk(@RequestBody MemberDTO memberDTO){
        memberService.findByEmail(memberDTO.getMemberEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    public ResponseEntity loginChk(@RequestParam("memberEmail") String memberEmail, Model model) {
//
//        System.out.println("memberEmail = " + memberEmail);
//        MemberDTO memberDTO = memberService.memberEmailChk(memberEmail);
//        if (memberEmail.length() == 0) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else if (memberDTO == null) {
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//    }

    @GetMapping("/main")
    public String main() {
        return "memberPages/memberMain";
    }

    @GetMapping("/mypage/{loginEmail}")
    public String findByEmail(@PathVariable String loginEmail, Model model) {
        System.out.println("loginEmail = " + loginEmail);
        MemberDTO memberDTO = memberService.findByEmail(loginEmail);

        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/logout")
    public String memberLogOut(HttpSession session) {
        session.invalidate();

        return "redirect:/member/login";
    }

}
