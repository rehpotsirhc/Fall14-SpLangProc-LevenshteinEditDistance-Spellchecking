package med;

public class Subs extends Edit
	{
		
		protected Chr target;
		
		
		protected Subs(int indexOfReplaced, char charOfReplaced, int indexOfReplacement, char charOfReplacement, int cost, EditType t)
			{
				super(indexOfReplaced, charOfReplaced, cost, t);
				this.target = new Chr(indexOfReplacement, charOfReplacement);
			}


		@Override
		public String toString()
			{
				
				return "Substitute '"+getChr().getChar()+"' at index "+getChr().getIndex()+" in the source for '"+target.getChar()+"'";
				
				//subs:
				//replacment character (and its index in target)
				//replaced character (and its index in source - the location of the replacement
			}
			
		
		
		
		
	}
