
public class IncomeType {
	
	private String type;  
	
	//accessor or getter method
	public String getType() {
		return type;
	}
	
	public IncomeType(String type) {
		this.type = type;
	}
	
	//this is used to return the available income types with their respective name 
	public static IncomeType[] getArray() {
		IncomeType fulltime = new IncomeType("Full-Time");
		IncomeType parttime = new IncomeType("Part-Time");
		IncomeType pocket = new IncomeType("Pocket Money");
		IncomeType allowance = new IncomeType("Allowance");
		IncomeType bonus = new IncomeType("Bonus");
		IncomeType other = new IncomeType("Other");
		IncomeType []availableType = {fulltime,parttime,allowance,pocket,bonus,other};
		return availableType;
	}
	
}
