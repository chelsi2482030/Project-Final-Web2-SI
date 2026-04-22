package com.example.productcrud.model;

<<<<<<< HEAD
public enum Category {
    ELEKTRONIK("Elektronik"),
    BUKU("Buku"),
    MAKANAN("Makanan"),
    PAKAIAN("Pakaian");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
=======
import jakarta.persistence.*;

@Entity
@Table(name = "category") // opsional tapi bagus
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
>>>>>>> 3f3a164bbfbeea432ca1fff3d3b20c60e52e0659
