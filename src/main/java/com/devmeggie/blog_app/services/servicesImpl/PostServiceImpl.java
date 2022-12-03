package com.devmeggie.blog_app.services.servicesImpl;

import com.devmeggie.blog_app.dtos.UpLoadPostDto;
import com.devmeggie.blog_app.enums.Role;
import com.devmeggie.blog_app.exceptions.NotFoundException;
import com.devmeggie.blog_app.exceptions.UnauthorizedException;
import com.devmeggie.blog_app.models.Category;
import com.devmeggie.blog_app.models.Post;
import com.devmeggie.blog_app.models.User;
import com.devmeggie.blog_app.pagination_criteria.PostPage;
import com.devmeggie.blog_app.repositories.CategoryRepo;
import com.devmeggie.blog_app.repositories.PostRepo;
import com.devmeggie.blog_app.repositories.UserRepo;
import com.devmeggie.blog_app.services.PostService;
import com.devmeggie.blog_app.services.UserService;
import com.devmeggie.blog_app.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final HttpSession httpSession;
    private final UserService userService;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;

    private final Util util;


    public Post uploadPost(UpLoadPostDto upLoadPostDto, Long categoryId) {
        Long userId = util.loggedInUserById();
        User user = util.findUserById(userId);

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Sorry! you do not have access");
        }
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("not found"));

        Post post = Post.builder()
                .title(upLoadPostDto.getTitle())
                .content(upLoadPostDto.getContent())
                .imageUrl(upLoadPostDto.getImageUrl())
                .comment(new ArrayList<>())
                .category(category)
                .build();
        postRepo.save(post);
        category.getPost().add(post);
        categoryRepo.save(category);
        return post;
    }

    @Override
    public Page<Post> viewAllPost(PostPage postPage) {
        List<Post> post = postRepo.findAll();
        if (post.isEmpty()) {
            throw new NotFoundException("there are no post");
        }
        Sort sort = Sort.by(postPage.getSortDirection(), postPage.getSortBy());
        Pageable pageable = PageRequest.of(postPage.getPageNumber(), postPage.getPageSize(), sort);
        return postRepo.findAll(pageable);


    }

    @Override
    public void deletePost(Long postId) {
        Long userId = util.loggedInUserById();
        User user = util.findUserById(userId);

        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Sorry! you dont have access");
        }
        Post post = postRepo.findById(postId).orElseThrow(() -> new NotFoundException("post doesn't"));
        postRepo.delete(post);
    }
}











