import java.io.FileNotFoundException;

public abstract class Statistics {
	
	//using protected so that the child class can access the instance variable
	protected double totalIncomes, totalExpenses, netBal, dailyAverageInc, dailyAverageExp;
	protected double fulltimeInc, parttimeInc, allowanceInc, pocketInc, bonusInc, otherInc;
	protected double foodExp, socialExp, petsExp, transportExp, cultureExp, householdExp, apparelExp, beautyExp, healthExp, educationExp, giftExp, otherExp;
	
	//initialization for all the statistical information to 0 at the beginning (for convenient and prevent redundant and repeating code to initialize)
	public Statistics () {
		this.totalIncomes = 0;
		this.totalExpenses = 0;
		this.netBal = 0;
		this.dailyAverageInc = 0;
		this.dailyAverageExp = 0;
		this.fulltimeInc = 0;
		this.parttimeInc = 0; 
		this.allowanceInc = 0;
		this.pocketInc = 0;
		this.bonusInc = 0;
		this.otherInc = 0;
		this.foodExp = 0;
		this.socialExp = 0;
		this.petsExp = 0;
		this.transportExp = 0;
		this.cultureExp = 0;
		this.householdExp = 0;
		this.apparelExp = 0;
		this.beautyExp = 0; 
		this.healthExp = 0; 
		this.educationExp = 0;
		this.giftExp = 0;
		this.otherExp = 0;
	}
	
	public abstract void calculateStatistics() throws FileNotFoundException; //will be used to calculate statistic for either monthly or daily
}
