package com.roadjava.AdoptersView;

public class Pet {
    private int id;  // 宠物ID
    private String name;  // 宠物名称
    private String type;  // 宠物类别（如 狗、猫）
    private String breed;  // 宠物品种
    private int age;  // 宠物年龄
    private String gender;  // 宠物性别（雄/雌）
    private boolean availableForAdoption;  // 是否可领养

    // 构造方法
    public Pet(int id, String name, String type, String breed, int age, String gender, boolean availableForAdoption) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.availableForAdoption = availableForAdoption;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAvailableForAdoption() {
        return availableForAdoption;
    }

    public void setAvailableForAdoption(boolean availableForAdoption) {
        this.availableForAdoption = availableForAdoption;
    }
}
