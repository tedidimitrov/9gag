package finalproject.ninegag.controller;

import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.pojo.Post;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PostController extends AbstractController{

    @Autowired
    private PostDAO postDAO;

    

}
