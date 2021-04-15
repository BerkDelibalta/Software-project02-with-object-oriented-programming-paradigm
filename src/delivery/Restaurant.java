//Authored by Berk Delibalta

package delivery;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Restaurant {

    private String name;
    private String category;
    private List<Dish> dishes = new LinkedList<>();
    private List<Integer> ratings;

    public Restaurant(String name, String category) {
        this.name=name;
        this.category=category;
        ratings = new LinkedList<>();
    }


    public String getName() {
        return name;
    }
    
    public String getCategory(){
        return category;
    }


    public List<Dish> getDishes() {
        return dishes;
    }

    public void addDishes(Dish dish) {
        dishes.add(dish);
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public List<Integer> getRatings(){
        return ratings;
    }

public int getAverageRating(){
    int sumOfRatings=ratings.stream().mapToInt(Integer::intValue).sum();
    int numberOfRatings=ratings.size();

    return sumOfRatings / numberOfRatings;
}

}
