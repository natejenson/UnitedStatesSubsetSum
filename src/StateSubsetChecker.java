import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class StateSubsetChecker {
	public static String[] states;
	public static boolean[][] opt;
	public static int[] areas;
	
	private static boolean[][] createStateAreaTable(int[] areas){
		//Sum up all of the states areas
		int totalArea = 0;
		for(int state = 0; state < 50; state++){
			totalArea += areas[state];
		}
		
		//Set areaPortion to be 47% of the total area of the US.
		int areaPortion = (int)(totalArea*0.47 + 0.5);

		boolean[][] result = new boolean[50][areaPortion + 1]; //add one to areaPortion to include zero area.
		//Fill Base Cases
		for(int v = 1; v < result[0].length; v++){
			result[0][v] = (areas[0] == v);
		}
		for(int i = 0; i < 50; i++){
			result[i][0] = true;
		}
		//Fill remaining
		for(int i = 1; i < 50; i++){
			for(int v = 1; v < result[0].length; v++){
				if(areas[i] > v){
					result[i][v] = result[i-1][v];
				}
				else{
					//areas[i] <= v
					if(result[i-1][v]){
						result[i][v] = result[i-1][v];
					} 
					else{
						result[i][v] = result[i-1][v-areas[i]];
					}
				}
			}
		}
		return result;
	}
	
	public static void printStateSubset(){
		//Print state subset starting from opt(m,t)
		printStateSubsetHelper(opt.length-1, opt[0].length-1);
	}
	public static void printStateSubsetHelper(int i, int v){
		if(v == 0){
			return;
		}
		else{
			if(i == 0){
				if(v == areas[0]){
					System.out.println(states[i] + '}');
				}
			}
			else{
				if(i > 0 && areas[i] > v && v > 0){
					printStateSubsetHelper(i-1,v);
				}
				else{
					if(opt[i-1][v]){
						printStateSubsetHelper(i-1,v);
					}
					else{
						System.out.print(states[i] + ", ");
						printStateSubsetHelper(i-1,v-areas[i]);
					}
				}
			}
		}
	}
	public static void main(String[] args){
		Scanner sc;
		try {
			sc = new Scanner(new FileInputStream("resources/stateAreas.txt"));
		} catch (FileNotFoundException e) {
			sc = new Scanner("");
			System.out.println("File not found");
			System.exit(0);
		}
		//Initialize Global Arrays
		states = new String[50];
		areas = new int[50];
		// Fill Arrays
		for(int i = 0; i < 50; i++){
			String line = sc.nextLine();
			String[] lineParts = line.split(",");
			states[i] = lineParts[0];
			areas[i] = Integer.parseInt(lineParts[1]);
		}
		sc.close();

		opt = createStateAreaTable(areas);
		if(opt[opt.length-1][opt[0].length-1]){
			System.out.println("Yes, there is such a subset.");
			System.out.print('{');
			printStateSubset();
		}
		else{
			System.out.println("No, there is not such a subset.");
		}
	}
}
