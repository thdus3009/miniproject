package com.sy.miniproject.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> { // JpaRepository<Entity 클래스, PK 타입>


}
