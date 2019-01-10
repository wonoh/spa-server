package com.spa.wonoh.member;

import com.spa.wonoh.model.Member;
import com.spa.wonoh.model.Role;
import com.spa.wonoh.model.RoleName;
import com.spa.wonoh.repository.MemberRepository;
import com.spa.wonoh.repository.RoleRepository;
import com.spa.wonoh.security.jwt.JwtAuthTokenFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberRepositoryTest {
    private static final Logger logger = LogManager.getLogger(MemberRepositoryTest.class);
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;

    @After
    public void cleanup(){
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 멤버조회확인(){
        Role role = new Role();
        role.setName(RoleName.ROLE_ADMIN);
        roleRepository.save(role);

        Set<Role> set = new HashSet<>();
        set.add(role);
        Member member = Member.builder().email("dasfasdf@nasvlakjdf.com").userId("test").password("testtest").roles(set).build();

        memberRepository.save(member);

        List<Member> list = memberRepository.findAll();
        logger.info("d");
        Member test = list.get(0);
        Set<Role> testRoles = test.getRoles();

        Assert.assertEquals(test.getEmail(), "dasfasdf@nasvlakjdf.com");
        Assert.assertEquals(test.getUserId(), "test");
        Assert.assertEquals(testRoles, set);
        Assert.assertEquals(test.getPassword(), "testtest");
    }
}
