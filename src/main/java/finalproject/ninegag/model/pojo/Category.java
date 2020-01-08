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

    FUNNY(1),
    ANIMALS(2),
    ANIME_MANGA(3),
    AWESOME(4),
    GAMING(5),
    CAR(6),
    COSPLAY(7),
    MEME(8),
    GIRLS(9),
    COMIC_WEBTOON(10),
    LEAGUE_OF_LEGENDS(11);

    private static final long FIRST_ID = 1;
    private static final long LAST_ID = 11;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    Category(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static boolean isValidId(long id) {
        if (id < FIRST_ID || id > LAST_ID) {
            return false;
        }
        return true;
    }
}