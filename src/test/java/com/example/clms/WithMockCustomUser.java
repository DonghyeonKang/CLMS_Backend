package com.example.clms;

import com.example.clms.entity.user.Roles;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "donghyeon09@naver.com";

    String name() default "donghyeon";

    Roles roles() default Roles.USER;
}