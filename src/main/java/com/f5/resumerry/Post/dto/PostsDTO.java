package com.f5.resumerry.Post.dto;

import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PostsDTO {

    private Long postId;
    private String title;
    private String contents;
    private Integer commentCnt;
    private Integer viewCnt;
    private Boolean isAnonymous;
    private String imageSrc;
    private Long memberId;
    private String nickname;
    private LocalDateTime modifiedDate;
    private CategoryEnum category;

    public static PostsDTO of(Post post) {
        return new PostsDTO(
                post.getId(),
                post.getTitle(),
                post.getContents(),
                post.getPostCommentList().size(),
                post.getViewCnt(),
                post.getIsAnonymous(),
                post.getMember().getImageSrc(),
                post.getMemberId(),
                post.getMember().getNickname(),
                post.getModifiedDate(),
                post.getCategory()
        );
    }

    public static List<PostsDTO> toList(List<Post> posts) {
        return posts.stream()
                .map(PostsDTO::of)
                .collect(Collectors.toList());
    }


}
