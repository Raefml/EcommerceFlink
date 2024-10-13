package dto;

public class Product {
    private String name;
    private String price;
    private String brand;
    private String description;
    private String availability;
    private String image;
    private String promotion;
    private String category;
    private String subcategory;
    private String id;

    public Product(String subcategory, String name, String price, String description, String availability, String image, String promotion, String category, String id) {
        this.subcategory = subcategory;
        this.name = name;
        this.price = price;
        this.description = description;
        this.availability = availability;
        this.image = image;
        this.promotion = promotion;
        this.category = category;
        this.id = id;

    }


    public Product() {}

    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, String price, String description, String availability, String image,
                   String promotion, String category, String subcategory) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.availability = availability;
        this.image = image;
        this.promotion = promotion;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Product(String name, String price, String brand, String description, String availability, String image, String promotion, String category, String subcategory, String id) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.availability = availability;
        this.image = image;
        this.promotion = promotion;
        this.category = category;
        this.subcategory = subcategory;
        this.id = id;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    // Getters and Setters...
}