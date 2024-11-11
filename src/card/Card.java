package card;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public abstract class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean hasAttacked;
    private ObjectMapper mapper = new ObjectMapper();

    public Card(final int mana, final int health, final String description,
                final ArrayList<String> colors, final String name,
                final boolean hasAttacked) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.hasAttacked = hasAttacked;
    }
}
