package com.relive.authentication;

import com.relive.utils.JsonUtils;
import org.junit.jupiter.api.Test;

/**
 * @author: ReLive
 * @date: 2021/12/5 4:40 下午
 */
public class JsonTest {

    @Test
    public void toObjectTest(){
        User user=new User();
        user.setPassword("123");
        user.setUsername("admin");

        String json = JsonUtils.objectToJson(user);
        User user1 = JsonUtils.jsonToObject(json, User.class);
    }
}
