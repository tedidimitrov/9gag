package finalproject.ninegag.model.repository;

import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    public List<Post> findAllByUser_Id(long id);

    public Optional<Post> findById(long id);

    public List<Post> findAllByOrderByDateUploadedDesc();

    @Query("Select c from Post c where c.title like %:title%")
    public List<Post> getByTitle(String title);

}
