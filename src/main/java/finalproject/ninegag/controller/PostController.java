package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import finalproject.ninegag.model.entity.Category;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController extends AbstractController{

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";
    private static final String STORAGE_ABSOLUTE_PATH  = "D:\\Git\\uploads\\";

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/posts")
    public ReadyPostDTO uploadPost(@RequestPart(name = "file") MultipartFile file,
                           @RequestParam String title,
                           @RequestParam long categoryId,
                           HttpSession session) throws IOException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }
        if(file == null){
            throw new NotFoundException("The file in not found!");
        }

        String extension = file.getOriginalFilename();
        String pattern = "yyyy-MM-dd-hh-mm-ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String postUrl = STORAGE_ABSOLUTE_PATH + LocalDateTime.now().format(formatter)+extension;
        Path path = Paths.get(postUrl);
        byte[] bytes = file.getBytes();

        Files.write(path, bytes);

        Post post = new Post();
        post.setPoints(0);
        //todo get category from db usingthe id
        post.setCategory(new Category(categoryId));
        post.setDateUploaded(LocalDateTime.now());
        post.setTitle(title);
        post.setUser(user);
        post.setImageUrl(postUrl);

        postRepository.save(post);
        ReadyPostDTO postDTO = new ReadyPostDTO(post);
        return postDTO;
    }

    @PostMapping("/posts/create")
    public ReadyPostDTO addPost(@RequestBody MakePostDTO postDTO, HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER) == null){
            throw new AuthorizationException("You must be logged in first!");
        }
        User currentUser = (User)session.getAttribute(SESSION_KEY_LOGGED_USER);
        Post post = new Post(postDTO,currentUser);
        //add to database
        postRepository.save(post);

        ReadyPostDTO readyPostDTO =new ReadyPostDTO(post);
        return readyPostDTO;
    }

    public ReadyPostDTO getPost(long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            return new ReadyPostDTO(post);
        }
        throw new NotFoundException("Post not found!");

    }

    @GetMapping("/posts/{id}")
    public ReadyPostDTO getPostById(@PathVariable long id){
        Optional<Post> post = postRepository.findById(id);
        if(!post.isPresent()){
            throw new NotFoundException("No such post found");
        }
        return new ReadyPostDTO(post.get());
    }

    @GetMapping("/posts")
    public List<ReadyPostDTO> getAllPosts(){
        List<Post> posts =this.postRepository.findAll();
        List<ReadyPostDTO> readyPosts = new ArrayList<>();
        for(Post post: posts){
            readyPosts.add(new ReadyPostDTO(post));
        }
        return readyPosts;
    }

    @GetMapping("/posts/getByDate")
    public List<ReadyPostDTO> getAllByDateDesc(){
        List<Post> posts = this.postRepository.findAllByOrderByDateUploadedDesc();
        List<ReadyPostDTO> readyPosts = new ArrayList<>();
        for(Post post: posts){
            readyPosts.add(new ReadyPostDTO(post));
        }
        return readyPosts;
    }

    @GetMapping("/posts/titles/{title}")
    public List<ReadyPostDTO> getPostByTitle(@PathVariable String title){
        List<Post> posts =this.postRepository.getByTitle(title);
        if(posts != null){
            List<ReadyPostDTO> readyPosts = new ArrayList<>();
            for(Post post: posts){
                readyPosts.add(new ReadyPostDTO(post));
            }
            return readyPosts;
        }
        throw new NotFoundException("Not found such post!");
    }

    @PostMapping("/posts/downvote/{post_id}")
    public ResponseEntity<String> downvotePost(@PathVariable long post_id,HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER) == null){
            throw new AuthorizationException("You must be logged in first!");
        }
        User currentUser = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Optional<Post> optionalPost = this.postRepository.findById(post_id);
        if(!optionalPost.isPresent()){
            throw  new NotFoundException("No such post found!");
        }
        Post post = optionalPost.get();
        if(post.getDownvoters().contains(currentUser)){
            post.removeDownvoter(currentUser);
            post.setPoints(post.getPoints()+1);
        }
        else{
            post.addDownvoter(currentUser);
            post.setPoints(Math.max(post.getPoints() - 1, 0));
        }
        this.postRepository.save(post);

        return new ResponseEntity<>("downvoting done correctly!", HttpStatus.OK);
    }

    @PostMapping("/posts/upvote/{post_id}")
    public ResponseEntity<String> upvotePost(@PathVariable long post_id,HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER) == null){
            throw new AuthorizationException("You must be logged in first!");
        }
        User currentUser = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Optional<Post> optionalPost = this.postRepository.findById(post_id);
        if(!optionalPost.isPresent()){
            throw  new NotFoundException("No such post found!");
        }
        Post post = optionalPost.get();
        if(post.getUpvoters().contains(currentUser)){
            post.removeUpvoter(currentUser);
            post.setPoints(Math.max(post.getPoints() - 1, 0));
        } else{
            post.setPoints(post.getPoints()+1);
            post.addUpvoter(currentUser);
        }
        this.postRepository.save(post);
        return new ResponseEntity<>("upvoting done correctly!", HttpStatus.OK);
    }

    @DeleteMapping("/posts/delete/{post_id}")
    public ResponseEntity<String> deleteUser(@PathVariable long post_id,HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER)== null){
            throw new AuthorizationException("You must login first!");
        }
        User currentUser = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Optional<Post> currentPost = this.postRepository.findById(post_id);
        if(!currentPost.isPresent()){
            throw new BadRequestException("No such post found!");
        }
        else if(currentPost.get().getUser().getId() != currentUser.getId()){
            throw  new AuthorizationException("You cannot delete this post.Ownership is mandatory for this operation!");
        }
        this.postRepository.delete(currentPost.get());
        return new ResponseEntity<>("Deletion successful!",HttpStatus.OK);
    }

}
