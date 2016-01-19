package med;





public class Edit
	{
		
		//insert:
		//index in source, char to insert
		
		//delete:
		//index in source, character being deleted
		
		
		
		
		
		private int cost;
		private Chr chr;
		private EditType type;
		
		

		protected Edit(int index, char c, int cost, EditType t)
		{
			this.cost = cost;
			this.chr = new Chr(index, c);
			this.type = t;
		}
		protected Chr getChr()
			{
				return chr;
			}
		
		
		public static Edit CreateDel(int index, char c, int cost)
		{

			return new Edit(index, c, cost, EditType.Del);
		}
		public static Edit CreateIns(int index, char c, int cost)
		{
			return new Edit(index, c, cost, EditType.Ins);
		}
		
		public static Edit CreateSubs(int indexOfReplaced, char charOfReplaced, int indexOfReplacement, char charOfReplacement, int cost)
		{
			return new Subs(indexOfReplaced, charOfReplaced, indexOfReplacement, charOfReplacement, cost, EditType.Sub);
		}
		
		
		public int getCost()
		{
			return cost;
		}
		
	




		@Override
		public String toString()
			{
				if(type == EditType.Del)
					{
						return "Delete '"+chr.getChar()+"' at index "+chr.getIndex()+" from the source";
					}
				else if(type == EditType.Ins)
					{
						return "Insert '"+chr.getChar()+"'  at index "+chr.getIndex()+" into the source";
					}
				return "";
			}
		
	
		
			
			
		
	}
