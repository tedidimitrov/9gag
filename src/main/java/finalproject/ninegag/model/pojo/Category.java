package finalproject.ninegag.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public enum Category {

    FUNNY(0),
    ANIMALS(1),
    ANIME_MANGA(2),
    AWESOME(3),
    GAMING(4),
    CAR(5),
    COSPLAY(6),
    MEME(7),
    GIRLS(8),
    COMIC_WEBTOON(9),
    LEAGUE_OF_LEGENDS(10);

    private static final long FIRST_ID = 0;
    private static final long LAST_ID = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    Category(int id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static boolean isCorrectId(long id) {
        if (id < FIRST_ID || id > LAST_ID) {
            return false;
        }
        return true;
    }
}