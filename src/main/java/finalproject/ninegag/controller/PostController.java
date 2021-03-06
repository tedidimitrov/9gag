package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dto.ChangePostTitleDTO;
import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import finalproject.ninegag.model.entity.Category;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.CategoryRepository;
import finalproject.ninegag.model.repository.PostRepository;
import finalproject.ninegag.utilities.SessionManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class PostController extends AbstractController{

    private static final String STORAGE_ABSOLUTE_PATH  = "D:\\Git\\uploads\\";
    private static final List<String> AVAILABLE_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "video/mp4");

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/posts")
    public ReadyPostDTO uploadPost(@Valid @RequestPart(name = "file") MultipartFile file,
                           @Valid @RequestParam (name = "title", defaultValue = "")String title,
                           @Valid @RequestParam long categoryId,
                           HttpSession session) throws IOException {
        User user = SessionManager.getLoggedUser(session);
        if(file.isEmpty()){
            throw new NotFoundException("The file is not found!");
        }
        if(!AVAILABLE_FILE_TYPES.contains(file.getContentType())){
            throw new BadRequestException("Invalid file type");
        }
        title = title.trim();
        if(title.equals("")){
            throw new BadRequestException("You must enter post title");
        }

        String extension = file.getOriginalFilename();
        String pattern = "yyyy-MM-dd-hh-mm-ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String postUrl = LocalDateTime.now().format(formatter)+extension;
        Path path = Paths.get(STORAGE_ABSOLUTE_PATH + postUrl);
        byte[] bytes = file.getBytes();
        Files.write(path, bytes);

        Post post = new Post();
        post.setPoints(0);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()) {
            post.setCategory(category.get());
            post.setDateUploaded(LocalDateTime.now());
            post.setTitle(title);
            post.setUser(user);
            post.setImageUrl(postUrl);
            postRepository.save(post);
            return new ReadyPostDTO(post);
        }
        else{
            throw new NotFoundException("No such category found");
        }
    }

    @PostMapping("/posts/create")
    public ReadyPostDTO addPost(@RequestBody MakePostDTO postDTO, HttpSession session){

        User currentUser = SessionManager.getLoggedUser(session);
        Post post = new Post(postDTO,currentUser);
        //add to database
        postRepository.save(post);

        ReadyPostDTO readyPostDTO =new ReadyPostDTO(post);
        return readyPostDTO;
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

    @GetMapping("/posts/titles")
    public List<ReadyPostDTO> getPostByTitle(@RequestParam(required = false) String title){
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

    @PostMapping("/posts/downvote/{postId}")
    public ResponseEntity<String> downvotePost(@PathVariable long postId,HttpSession session){
        User currentUser = SessionManager.getLoggedUser(session);
        Optional<Post> optionalPost = this.postRepository.findById(postId);
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

    @PostMapping("/posts/upvote/{postId}")
    public ResponseEntity<String> upvotePost(@PathVariable long postId,HttpSession session){
        User currentUser = SessionManager.getLoggedUser(session);
        Optional<Post> optionalPost = this.postRepository.findById(postId);
        if(!optionalPost.isPresent()){
            throw new NotFoundException("No such post found!");
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

    @SneakyThrows
    @DeleteMapping("/posts/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable long postId,HttpSession session){
        User currentUser = SessionManager.getLoggedUser(session);
        Optional<Post> currentPost = this.postRepository.findById(postId);
        if(!currentPost.isPresent()){
            throw new NotFoundException("No such post found!");
        }
        else if(currentPost.get().getUser().getId() != currentUser.getId()){
            throw new AuthorizationException("You cannot delete this post.Ownership is mandatory for this operation!");
        }
        File file = new File(STORAGE_ABSOLUTE_PATH + currentPost.get().getImageUrl());
        if(file.delete()){
            this.postRepository.delete(currentPost.get());
            return new ResponseEntity<>("Deletion successful!",HttpStatus.OK);
        }
        else {
            throw new IOException("The file was not deleted properly!");
        }
    }

    @SneakyThrows
    @GetMapping("/posts/category")
    public List<ReadyPostDTO> getPostsByCategory(@RequestParam(required = false) String category){
        List<Post> posts = new ArrayList<>();
        if(category.isEmpty()){
            posts = this.postRepository.findAll();
        }else{
            posts = this.postRepository.findAllByCategory_CategoryName(category);
        }
        if(!posts.isEmpty()) {
            List<ReadyPostDTO> readyPosts = new ArrayList<>();
            for (Post post : posts) {
                readyPosts.add(new ReadyPostDTO(post));
            }
            return readyPosts;
        }else{
            throw new NotFoundException("No post of such category found!");
        }
    }

    @PutMapping("/posts/editTitle")
    public ResponseEntity<String> editPost(HttpSession session, @Valid @RequestBody ChangePostTitleDTO post, Errors errors){
        User currentUser = SessionManager.getLoggedUser(session);
        if(errors.hasErrors()){
            throw  new BadRequestException("Fields must not be empty!");
        }
        Post currentPost = this.postRepository.findByTitle(post.getCurrentTitle());
        if(currentPost == null){
            throw  new BadRequestException("No post with such title found!");
        }
        else{
            currentPost.setTitle(post.getNewTitle());
            this.postRepository.save(currentPost);
            return new ResponseEntity<>("editing post successfully!",HttpStatus.OK);
        }
    }

}
