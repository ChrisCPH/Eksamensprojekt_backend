package dtos;

import entities.Meals;

import java.util.ArrayList;

public class MealsDTO {

    private Meals[] meals;
    private String url = "https://www.themealdb.com/api/json/v1/1/filter.php?i=";

    public MealsDTO(Meals[] meals, String url) {
        this.meals = meals;
        this.url = url;
    }

    public MealsDTO() {
    }

    public Meals[] getMeals() {
        return meals;
    }

    public void setMeals(Meals[] meals) {
        this.meals = meals;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
