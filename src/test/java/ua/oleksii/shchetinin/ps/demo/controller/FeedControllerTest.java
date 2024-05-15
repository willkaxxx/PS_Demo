package ua.oleksii.shchetinin.ps.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.oleksii.shchetinin.ps.demo.PrincipalDetailsArgumentResolver;
import ua.oleksii.shchetinin.ps.demo.model.Comment;
import ua.oleksii.shchetinin.ps.demo.model.Like;
import ua.oleksii.shchetinin.ps.demo.model.Post;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.repository.CommentRepository;
import ua.oleksii.shchetinin.ps.demo.repository.LikeRepository;
import ua.oleksii.shchetinin.ps.demo.repository.PostRepository;
import ua.oleksii.shchetinin.ps.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.org.webcompere.modelassert.json.JsonAssertions.assertJson;

@SpringBootTest
@AutoConfigureMockMvc
class FeedControllerTest {

    @Autowired
    private FeedController feedController;

    @MockBean
    private UserRepository userRepositoryMock;
    @MockBean
    private LikeRepository likeRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private PostRepository postRepository;

    @Test
    void testGetMyFeedIsAsExpected() throws Exception {
        User user = User.builder()
                .username("User")
                .password("{bcrypt}$2a$10$Aoyh8t8osPrkGM4WnVtxMuz0GgG36fx76Q3VKy6BE.TbavXP6m8Cu")
                .email("newEmail@gmail.com")
                .favoritePosts(Set.of("663fb04c5f4ab024e84ed1ad"))
                .subscriptions(Set.of("Oleksii2"))
                .build();
        doReturn(Optional.of(user))
                .when(userRepositoryMock).findByUsername("User");
        doReturn(Optional.of(User.builder()
                .username("Oleksii2")
                .password("{bcrypt}$2a$10$Wr1020KEYEKcaPH.SmOguugbEGgjvZrm.f/QYFaZkS5cGaEo0duLu")
                .email("newEmail@gmail.com")
                .subscriptions(Set.of("Oleksii"))
                .build()))
                .when(userRepositoryMock).findByUsername("Oleksii2");
        doReturn(List.of(Post.builder()
                .authorUsername("Oleksii2")
                .postText("Post about tests")
                .id("p1")
                .build()))
                .when(postRepository).findByAuthorUsername("Oleksii2");
        doReturn(List.of(Like.builder().username("User").postId("p1").build(),
                Like.builder().username("Oleksii2").postId("p1").build()))
                .when(likeRepository).getAllByPostId("p1");
        doReturn(List.of(Comment.builder()
                .postId("p1")
                .authorUsername("User")
                .text("Wow! Great post!")
                .id("c1").build()))
                .when(commentRepository).findAllByPostId("p1");

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(feedController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
                .build();

        var expected = """
                {
                  "feed": [
                    {
                      "post": {
                        "id": "p1",
                        "authorUsername": "Oleksii2",
                        "postText": "Post about tests"
                      },
                      "likedBy": [
                        "User",
                        "Oleksii2"
                      ],
                      "comments": [
                        {
                          "id": "c1",
                          "authorUsername": "User",
                          "postId": "p1",
                          "text": "Wow! Great post!"
                        }
                      ]
                    }
                  ]
                }""";

        mockMvc.perform(get("/v1/feed"))
                .andExpect(status().isOk())
                .andExpect(matcher -> assertJson(expected).isEqualTo(matcher.getResponse().getContentAsString()));
    }

    @Test
    void testGetOtherFeedIsAsExpected() throws Exception {
        User user = User.builder()
                .username("User")
                .password("{bcrypt}$2a$10$Aoyh8t8osPrkGM4WnVtxMuz0GgG36fx76Q3VKy6BE.TbavXP6m8Cu")
                .email("newEmail@gmail.com")
                .favoritePosts(Set.of("663fb04c5f4ab024e84ed1ad"))
                .subscriptions(Set.of("Oleksii2"))
                .build();
        doReturn(Optional.of(user))
                .when(userRepositoryMock).findByUsername("User");
        doReturn(Optional.of(User.builder()
                .username("Oleksii2")
                .password("{bcrypt}$2a$10$Wr1020KEYEKcaPH.SmOguugbEGgjvZrm.f/QYFaZkS5cGaEo0duLu")
                .email("newEmail@gmail.com")
                .subscriptions(Set.of("User"))
                .build()))
                .when(userRepositoryMock).findByUsername("Oleksii2");
        doReturn(List.of(Post.builder()
                .authorUsername("User")
                .postText("Post about tests")
                .id("p1")
                .build()))
                .when(postRepository).findByAuthorUsername("User");
        doReturn(List.of(Like.builder().username("Oleksii2").postId("p1").build()))
                .when(likeRepository).getAllByPostId("p1");
        doReturn(List.of(Comment.builder()
                .postId("p1")
                .authorUsername("User")
                .text("My post!")
                .id("c1").build()))
                .when(commentRepository).findAllByPostId("p1");

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(feedController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
                .build();

        var expected = """
                {
                  "feed": [
                    {
                      "id": "p1",
                      "authorUsername": "User",
                      "postText": "Post about tests"
                    }
                  ]
                }""";

        mockMvc.perform(get("/v1/feed/Oleksii2"))
                .andExpect(status().isOk())
                .andExpect(matcher -> assertJson(expected).isEqualTo(matcher.getResponse().getContentAsString()));
    }
}