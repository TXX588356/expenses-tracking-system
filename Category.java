
public class Category {
	
	private String type;  
	
	//accessor or getter method
	public String getType() {
		return type;
	}
	
	public Category(String type) {
		this.type = type;
	}
	
	//this is used to return the available category with their respective type
	public static Category[] getArray() {
		Category food = new Category ("Food");
		Category social = new Category("Social Life");
		Category pets = new Category("Pets");
		Category transport = new Category ("Transport");
		Category culture = new Category ("Culture");
		Category household = new Category ("Household");
		Category apparel = new Category("Apparel");
		Category beauty = new Category ("Beauty");
		Category health = new Category("Health");
		Category education = new Category("Education");
		Category gift = new Category("Gift");
		Category other = new Category("Other");
		Category []availablecat= {food,social,pets,transport,culture,household,apparel,beauty,health,education,gift,other};
		return availablecat;
	}
	
}
