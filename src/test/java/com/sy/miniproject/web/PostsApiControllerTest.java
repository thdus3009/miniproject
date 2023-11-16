package com.sy.miniproject.web;

import com.sy.miniproject.domain.posts.Posts;
import com.sy.miniproject.domain.posts.PostsRepository;
import com.sy.miniproject.web.dto.PostsSaveRequestDto;
import com.sy.miniproject.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다(){
        // given
        LocalDateTime now = LocalDateTime.of(2023,11,16,0,0,0);
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:"+port+"/api/v1/posts";
        // when
        // 해당 코드가 제대로 등록되었는지, 정상인지 확인하기 위해 RestTemplate 사용
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class); // 저장(url, request, response 값)
        // 확인할 포인트가 등록부분이 아니라면 Repository 사용해도 된다.
//        postsRepository.save(Posts.builder()
//                .title(title)
//                .content(content)
//                .author("author")
//                .build());

        // then
        /**
         * 상태코드가 정상인지 확인 & 등록이 되었는지 확인(0개이상)
         */
        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        /**
         * 제대로된 값이 들어갔는지 확인
         */
        List<Posts> all = postsRepository.findAll();
        Posts posts = all.get(0);
        System.out.println(">>>>>> createDate="+posts.getCreatedDate()+", modifiedDate="+posts.getUpdatedDate());

        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_수정한다() throws Exception {
        // 영속성 컨텍스트에 저장
        Posts savedPost = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()
        );

        // given
        Long updateId = savedPost.getId();
        String chtitle = "수정한 title";
        String chcontent = "수정한 content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(chtitle)
                .content(chcontent)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts/"+updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        /**
         * RestTemplate.exchange의 '요청데이터'는 entity(HttpEntity)형태여야 한다.
         */
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(chtitle);;
        assertThat(all.get(0).getContent()).isEqualTo(chcontent);;


    }
}
