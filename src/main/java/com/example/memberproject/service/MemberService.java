package com.example.memberproject.service;

import com.example.memberproject.dto.MemberDTO;
import com.example.memberproject.entity.MemberEntity;
import com.example.memberproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity e : memberEntities) {
            memberDTOList.add(MemberDTO.ToDTO(e));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findById(id);
        if (memberEntityOptional.isPresent()) {
            System.out.println("있다.");
            MemberEntity memberEntity = memberEntityOptional.get();
            MemberDTO memberDTO = MemberDTO.ToDTO(memberEntity);
            return memberDTO;
        } else {
            return null;
        }
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public void update(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveMemberEntityId(memberDTO);
        memberRepository.save(memberEntity);
    }

    public int loginChk(MemberDTO memberDTO) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword());
        if(memberEntityOptional.isPresent()){
            System.out.println("로그인성공");
            return 1;
        }else {
            System.out.println("로그인실패");
            return 0;
        }
    }

    public MemberDTO memberEmailChk(String memberEmail) {
        Optional<MemberEntity> memberEntityOptional = memberRepository.findByMemberEmail(memberEmail);
        if(memberEntityOptional.isPresent()){
            System.out.println("이메일 있음 중복");
            return MemberDTO.ToDTO(memberEntityOptional.get());
        }else{
            return null;
        }
    }
}