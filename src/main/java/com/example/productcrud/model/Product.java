package com.example.productcrud.model;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_product_categories"))
    private Categories categories;

    // Perubahan: long -> Long
    private Long price;

    // Perubahan: int -> Integer
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean active;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Product() {
    }

    // Perubahan: Constructor disesuaikan dengan tipe data baru
    public Product(Long id, String name, Categories categories, Long price, Integer stock,
                   String description, boolean active, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
    }

    // Lifecycle callback untuk mengisi tanggal otomatis
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
    }

    // Getters and Setters
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

    public Categories getCategories() { // Sebelumnya getCategory
        return categories;
    }

    public void setCategories(Categories categories) { // Sebelumnya setCategory
        this.categories = categories;
    }

    // Perubahan: Getter & Setter disesuaikan
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    // Perubahan: Getter & Setter disesuaikan
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}