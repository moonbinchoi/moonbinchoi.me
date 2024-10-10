package me.moonbinchoi.blog.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.moonbinchoi.blog.board.domain.Post;
import me.moonbinchoi.blog.board.dto.request.PostRequest;
import me.moonbinchoi.blog.board.dto.response.PostResponse;
import me.moonbinchoi.blog.board.service.component.PostService;
import me.moonbinchoi.blog.global.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private static final int INDEX_PAGE_POSTS_LIMIT = 5;


    @GetMapping
    public String board(final Model model) {
        List<PostResponse> posts = postService.findLatestPosts(INDEX_PAGE_POSTS_LIMIT).stream()
                .map(PostResponse::fromWithTruncatedContent)
                .toList();
        model.addAttribute("posts", posts);
        return "pages/home/index";
    }

    @GetMapping("/create")
    public String create(final Model model) {
        model.addAttribute("request", new PostRequest.Create());
        return "pages/post/create";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("request") PostRequest.Create request,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("BindingResult Error: {}", bindingResult);
            return "redirect:/create";
        }

        log.info("Create post: {}", request);
        Post savedPost = postService.createPost(request.toEntity(), request.getTagIds());
        return "redirect:/" + savedPost.getId();
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId, Model model) {
        Post post = EntityUtils.getEntityOrThrow(postService.findPost(postId), postId);
        model.addAttribute("post", PostResponse.from(post));
        return "pages/post/post";
    }

    @GetMapping("/{postId}/edit")
    public String edit(@PathVariable Long postId, Model model) {
        Post post = EntityUtils.getEntityOrThrow(postService.findPost(postId), postId);
        model.addAttribute("post", PostResponse.from(post));
        return "pages/post/edit";
    }

    @PostMapping("/{postId}/edit")
    public String edit(@PathVariable Long postId, PostRequest.Update request) {
        postService.updatePost(postId, request.toEntity(), request.getTagIds());
        return "redirect:/" + postId;
    }

    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId) {
        log.info("Delete post: {}", postId);
        postService.deletePost(postId);
        return "redirect:/" + postId;
    }
}
