package delivery;

import java.util.*;
import java.util.stream.Collectors;

public class Delivery {
	// R1

	private List<String> categories = new LinkedList<>();
	private Map<String,Restaurant> restaurants = new HashMap<>();
	private int orderId=0;
	private Map<Integer,Order> orders = new HashMap<>();
	
	/**
	 * adds one category to the list of categories managed by the service.
	 * 
	 * @param category name of the category
	 * @throws DeliveryException if the category is already available.
	 */
	public void addCategory(String category) throws DeliveryException {
		if(categories.contains(category)) throw new DeliveryException();
		categories.add(category);
	}

	/**
	 * retrieves the list of defined categories.
	 * 
	 * @return list of category names
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * register a new restaurant to the service with a related category
	 * 
	 * @param name     name of the restaurant
	 * @param category category of the restaurant
	 * @throws DeliveryException if the category is not defined.
	 */
	public void addRestaurant(String name, String category) throws DeliveryException {
		if(!categories.contains(category)) throw new DeliveryException();

		Restaurant restaurant = new Restaurant(name,category);
		restaurants.put(name,restaurant);
	}

	/**
	 * retrieves an ordered list by name of the restaurants of a given category. It
	 * returns an empty list in there are no restaurants in the selected category or
	 * the category does not exist.
	 * 
	 * @param category name of the category
	 * @return sorted list of restaurant names
	 */
	public List<String> getRestaurantsForCategory(String category) {
		return restaurants.entrySet().stream().filter(e->e.getValue().getCategory().equals(category))
				.map(e->e.getValue())
				.sorted(Comparator.comparing(Restaurant::getName))
				.map(e->e.getName()).collect(Collectors.toList());
	}

	// R2

	/**
	 * adds a dish to the list of dishes of a restaurant. Every dish has a given
	 * price.
	 * 
	 * @param name           name of the dish
	 * @param restaurantName name of the restaurant
	 * @param price          price of the dish
	 * @throws DeliveryException if the dish name already exists
	 */
	public void addDish(String name, String restaurantName, float price) throws DeliveryException {
		Restaurant restaurant = restaurants.get(restaurantName);

		if (restaurant != null) {

			Dish dish = new Dish(restaurantName, name, price);

			if (restaurant.getDishes().stream().anyMatch(e->e.getName().equals(name))) throw new DeliveryException();

			restaurant.addDishes(dish);

		}
	}

	/**
	 * returns a map associating the name of each restaurant with the list of dish
	 * names whose price is in the provided range of price (limits included). If the
	 * restaurant has no dishes in the range, it does not appear in the map.
	 * 
	 * @param minPrice minimum price (included)
	 * @param maxPrice maximum price (included)
	 * @return map restaurant -> dishes
	 */
	public Map<String, List<String>> getDishesByPrice(float minPrice, float maxPrice) {
          return restaurants.values()
				  .stream()
				  .flatMap(e->e.getDishes().stream())
				  .filter(e->e.getPrice()>=minPrice && e.getPrice()<=maxPrice)
				  .collect(Collectors.groupingBy(Dish::getRestaurantName,
						  Collectors.mapping(Dish::getName,Collectors.toList())));

	}

	/**
	 * retrieve the ordered list of the names of dishes sold by a restaurant. If the
	 * restaurant does not exist or does not sell any dishes the method must return
	 * an empty list.
	 * 
	 * @param restaurantName name of the restaurant
	 * @return alphabetically sorted list of dish names
	 */
	public List<String> getDishesForRestaurant(String restaurantName) {

		Restaurant restaurant = restaurants.get(restaurantName);

		if(restaurant!=null && restaurant.getDishes()!=null){

			return restaurant
					.getDishes()
					.stream()
					.sorted(Comparator.comparing(Dish::getName))
					.map(Dish::getName)
					.collect(Collectors.toList());

		}

		return new LinkedList<>();
	}

	/**
	 * retrieves the list of all dishes sold by all restaurants belonging to the
	 * given category. If the category is not defined or there are no dishes in the
	 * category the method must return and empty list.
	 * 
	 * @param category the category
	 * @return
	 */
	public List<String> getDishesByCategory(String category) {

	List<String> listOfDishes=restaurants.values().stream()
			.filter(e->e.getCategory().equals(category))
				.flatMap(e->e.getDishes().stream())
				.map(Dish::getName)
				.collect(Collectors.toList());

		if(!categories.contains(category) || listOfDishes.isEmpty()){
			return new LinkedList<>();
		}

		return restaurants.values().stream()
				.filter(e->e.getCategory().equals(category))
				.flatMap(e->e.getDishes().stream())
				.map(Dish::getName)
				.collect(Collectors.toList());
	}

	// R3

	/**
	 * creates a delivery order. Each order may contain more than one product with
	 * the related quantity. The delivery time is indicated with a number in the
	 * range 8 to 23. The delivery distance is expressed in kilometers. Each order
	 * is assigned a progressive order ID, the first order has number 1.
	 * 
	 * @param dishNames        names of the dishes
	 * @param quantities       relative quantity of dishes
	 * @param customerName     name of the customer
	 * @param restaurantName   name of the restaurant
	 * @param deliveryTime     time of delivery (8-23)
	 * @param deliveryDistance distance of delivery
	 * 
	 * @return order ID
	 */
	public int addOrder(String dishNames[], int quantities[], String customerName, String restaurantName,
			int deliveryTime, int deliveryDistance) {
		Map<String,Integer> dishes = new HashMap<>();
for(String dish : dishNames)
	for(int quantity : quantities)
		dishes.put(dish,quantity);

	Restaurant restaurant = restaurants.get(restaurantName);

	++orderId;
	Order order = new Order(orderId,customerName,restaurant,dishes,deliveryTime,deliveryDistance);
	orders.put(orderId,order);


		return orderId;
	}

	/**
	 * retrieves the IDs of the orders that satisfy the given constraints. Only the
	 * first {@code maxOrders} (according to the orders arrival time) are returned
	 * they must be scheduled to be delivered at {@code deliveryTime} whose
	 * {@code deliveryDistance} is lower or equal that {@code maxDistance}. Once
	 * returned by the method the orders must be marked as assigned so that they
	 * will not be considered if the method is called again. The method returns an
	 * empty list if there are no orders (not yet assigned) that meet the
	 * requirements.
	 * 
	 * @param deliveryTime required time of delivery
	 * @param maxDistance  maximum delivery distance
	 * @param maxOrders    maximum number of orders to retrieve
	 * @return list of order IDs
	 */
	public List<Integer> scheduleDelivery(int deliveryTime, int maxDistance, int maxOrders) {

		List<Integer> listOfOrderIds=orders.values().stream()
				.filter(e->e.getDeliveryTime()==deliveryTime && e.getDeliveryDistance()<=maxDistance)
				.map(e->e.getOrderId()).collect(Collectors.toList());


		List<Integer> maxIdNumbers = new LinkedList<>();


			if(listOfOrderIds.size()<=maxOrders) {
				for(int id : listOfOrderIds) {
					if (orders.get(id).getStatus().equals(orderStatus.ASSIGNED)) {
						return new LinkedList<>();
					} else {
						maxIdNumbers.add(id);
						orders.get(id).setStatus(orderStatus.ASSIGNED);
					}
				}
			} else {
				for(int i=0;i<listOfOrderIds.size()-maxOrders;++i) {
					if(orders.get(listOfOrderIds.get(i)).getStatus().equals(orderStatus.ASSIGNED)){
						return new LinkedList<>();
					} else {
						maxIdNumbers.add(listOfOrderIds.get(i));
						orders.get(listOfOrderIds.get(i)).setStatus(orderStatus.ASSIGNED);
					}
				}
			}



		return maxIdNumbers;

	}

	/**
	 * retrieves the number of orders that still need to be assigned
	 * 
	 * @return the unassigned orders count
	 */
	public int getPendingOrders() {
		return orders.values().stream().filter(e->e.getStatus().equals(orderStatus.NOT_YET_ASSIGNED))
				.collect(Collectors.toList()).size();
	}

	// R4
	/**
	 * records a rating (a number between 0 and 5) of a restaurant. Ratings outside
	 * the valid range are discarded.
	 * 
	 * @param restaurantName name of the restaurant
	 * @param rating         rating
	 */
	public void setRatingForRestaurant(String restaurantName, int rating) {

		Restaurant restaurant = restaurants.get(restaurantName);

		if(rating>=0 && rating<=5){
			restaurant.addRating(rating);
		}
	}

	/**
	 * retrieves the ordered list of restaurant.
	 * 
	 * The restaurant must be ordered by decreasing average rating. The average
	 * rating of a restaurant is the sum of all rating divided by the number of
	 * ratings.
	 * 
	 * @return ordered list of restaurant names
	 */
	public List<String> restaurantsAverageRating() {

		return restaurants.values().stream().filter(e->e.getRatings().size()!=0)
				.sorted(Comparator.comparing(Restaurant::getAverageRating).reversed())
				.map(Restaurant::getName).collect(Collectors.toList());
	}

	// R5
	/**
	 * returns a map associating each category to the number of orders placed to any
	 * restaurant in that category. Also categories whose restaurants have not
	 * received any order must be included in the result.
	 * 
	 * @return map category -> order count
	 */
	public Map<String, Long> ordersPerCategory() {
		return orders.values()
				.stream()
				.collect(Collectors.groupingBy(e->e.getRestaurantName().getCategory(),
						Collectors.mapping(Order::getOrderId,Collectors.counting())));
	}

	/**
	 * retrieves the name of the restaurant that has received the higher average
	 * rating.
	 * 
	 * @return restaurant name
	 */
	public String bestRestaurant() {
		return restaurants.values()
				.stream()
				.max(Comparator.comparing(Restaurant::getAverageRating))
				.map(Restaurant::getName).get();
	}
}