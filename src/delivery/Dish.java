//Authored by Berk Delibalta

package delivery;

public class Dish {

   private String name;
   private float price;
   private String restaurantName;

    public Dish(String restaurantName, String name, float price) {
        this.name = name;
        this.price=price;
        this.restaurantName=restaurantName;
    }


    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getRestaurantName(){
        return restaurantName;
    }

}
