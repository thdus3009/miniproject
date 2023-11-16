package com.sy.miniproject;

import com.sy.miniproject.web.dto.HelloResponseDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDtoTest {
    @Test
    public void lombokTest(){
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount); // 생성자가 있다면 값출력됨

        //then
        assertThat(dto.getName().equals(name));
        assertThat(dto.getAmount()==(amount));

    }
}
