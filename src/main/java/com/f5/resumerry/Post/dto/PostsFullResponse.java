package com.f5.resumerry.Post.dto;

import com.f5.resumerry.Post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostsFullResponse {
    private List<PostsDTO> contents;
    private Integer totalPages;

    public PostsFullResponse(Page<PostsDTO> contents, Integer totalPages) {
        this.contents = contents.getContent();
        this.totalPages = totalPages;
    }
}

