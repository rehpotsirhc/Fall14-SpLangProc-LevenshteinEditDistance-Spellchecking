package med;
import java.io.IOException;


public class Driver
	{
		// added "cracker", "less", "carbohydrate", and "organic" to the
		// dictionary
		// because "lass", "leas", "crackev", carhohydrate, and urganio were
		// being corrected
		// as "fat", "per", "trans", "calories", and "trans" respectively
		public static void main(String[] args) throws IOException
			{
				LEDSpellchecker sc = new LEDSpellchecker("dictionary.txt");
				sc.getEdits("chris", "kris");
				System.out.println(sc.toString());
				sc.getEdits("christopher", "chris");
				System.out.println(sc.toString());
				sc.getEdits("intention", "execution");
				System.out.println(sc.toString());
				sc.getEdits("gdasvwsa23412", "89725jkodfs");
				System.out.println(sc.toString());
				sc.getEdits("execution", "intention");
				System.out.println(sc.toString());
				sc.getEdits("melanie", "melony");
				System.out.println(sc.toString());
				sc.getEdits("telephonecaul", "telefoncall");
				System.out.println(sc.toString());
				sc.getEdits("gbbc", "gbc");
				System.out.println(sc.toString());
				sc.getEdits("gbba", "abg");
				System.out.println(sc.toString());
				sc.getEdits("abg", "gbba");
				System.out.println(sc.toString());
				sc.getEdits("abbg", "gba");
				System.out.println(sc.toString());
				sc.spellcheckFile("misspellings.txt", "corrections.txt");
				String correction = sc.spellCheckWord("karhohidritsss", true);
				if (correction != null)
					{
						System.out.println(correction);
						System.out.println(sc.toString());
					}
				correction = sc.spellCheckWord("karhohidritsssX", true);
				if (correction != null)
					{
						System.out.println(correction);
						System.out.println(sc.toString());
					}
				else System.out.println("NO CORRECTION FOUND");
			}
	}
