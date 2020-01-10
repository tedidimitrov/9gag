package finalproject.ninegag.model.repository;

import finalproject.ninegag.model.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    public List<Post> findAllByUser_Id(long id);
    public Post getById(long id);
}
