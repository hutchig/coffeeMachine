package com.ibm.coffee;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("menu")
public class Menu {

	private static Map<Drink, Integer> menu = getMenu();	
	
    @GET @Produces(MediaType.TEXT_HTML)
    public String menu() {
    	String result = "";
    	
		for( Drink d: menu.keySet())
    	{
    		result += d.toString() + " $" + menu.get(d) + "<br>";
    	}
		return result;
    }

	public static boolean hasDrink(Drink d) {
		return d!=null && menu.keySet().contains(d);
	}

	public static Integer getCost(Drink d) {
		return menu.get(d);
	}
	
	private static Map<Drink, Integer> getMenu(){
		Map<Drink, Integer> m = new HashMap<>();
		
		m.put( Drink.LATTE, 3 );
		m.put( Drink.COFFEE, 2 );
		m.put( Drink.TEA, 1 );
		
		return m;
	}
 
}
