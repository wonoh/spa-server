package com.spa.wonoh.member;


import com.spa.wonoh.model.Role;
import com.spa.wonoh.model.RoleName;
import com.spa.wonoh.repository.RoleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InsertTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void InsertRoles(){
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        roleRepository.save(role);

    }
}
