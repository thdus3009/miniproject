package com.sy.miniproject.service.posts;

import com.sy.miniproject.domain.posts.Posts;
import com.sy.miniproject.domain.posts.PostsRepository;
import com.sy.miniproject.web.dto.PostsListResponseDto;
import com.sy.miniproject.web.dto.PostsResponseDto;
import com.sy.miniproject.web.dto.PostsSaveRequestDto;
import com.sy.miniproject.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional(readOnly = true) // 트랜잭션 범위는 유지되고 조회 속도가 개선된다.
    public List<PostsListResponseDto> findAll() {
        // List<Posts> => List<PostsListResponseDto>
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // map(posts -> new PostsListResponseDto(posts)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id = "+id)
        );
        return new PostsResponseDto(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id = "+id)
        );
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return postsRepository.save(posts).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(
            ()-> new IllegalArgumentException("해당 게시글이 없습니다. id = "+id)
        );
        postsRepository.delete(posts);
    }
}
