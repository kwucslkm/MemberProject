package com.example.memberproject.entity;

import com.example.memberproject.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(length = 50, unique = true)
    public String memberEmail;
    @Column(length = 30)
    public String memberPassword;
    @Column(length = 30)
    public String memberName;
    @Column(length = 20)
    public String memberBirth;
    @Column(length = 20)
    public String memberMobile;

public static MemberEntity toSaveMemberEntity(MemberDTO memberDTO){
    return toSaveMemberEntityNoId(memberDTO);
}
    public static MemberEntity toSaveMemberEntityId(MemberDTO memberDTO) {
    MemberEntity memberEntity = toSaveMemberEntityNoId(memberDTO);
    memberEntity.setId(memberDTO.getId());
    return memberEntity;
    }

public static MemberEntity toSaveMemberEntityNoId(MemberDTO memberDTO){
    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setMemberEmail(memberDTO.getMemberEmail());
    memberEntity.setMemberPassword(memberDTO.getMemberPassword());
    memberEntity.setMemberName(memberDTO.getMemberName());
    memberEntity.setMemberBirth(memberDTO.getMemberBirth());
    memberEntity.setMemberMobile(memberDTO.getMemberMobile());
    return memberEntity;
}

}
