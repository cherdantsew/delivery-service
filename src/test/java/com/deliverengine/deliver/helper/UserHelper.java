package com.deliverengine.deliver.helper;

import com.deliverengine.deliver.model.entity.User;
import java.math.BigDecimal;

import static kafka.utils.TestUtils.randomString;

public class UserHelper {
    private UserHelper(){}

    public static User createUser(BigDecimal balance){
        return User.builder()
            .login(randomString(10))
            .accountBalance(balance)
            .build();
    }
}
