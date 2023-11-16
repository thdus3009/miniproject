package com.sy.miniproject.service.posts;

import com.sy.miniproject.domain.posts.Posts;
import com.sy.miniproject.domain.posts.PostsRepository;
import com.sy.miniproject.web.dto.PostsResponseDto;
import com.sy.miniproject.web.dto.PostsSaveRequestDto;
import com.sy.miniproject.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

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
}
