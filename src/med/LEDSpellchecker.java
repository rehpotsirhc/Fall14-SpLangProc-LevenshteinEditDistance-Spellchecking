package med;

import java.io.*;
import java.util.*;


public class LEDSpellchecker
	{
		private ArrayList<String>	dictionary;
		private int[][]				costs;
		private LinkedList<Edit>	edits;
		private int					totalCost;
		private int					insCost;
		private int					delCost;
		private int					subsCost;
		private int					maxEditCost;

		/*
		 * Creates a new LEDSpellchecker with insertion, deletion, and
		 * substitution costs each set to 1 The edit cost threshold is set to 8.
		 * That means only potential corrections within an edit cost of 8 or
		 * less are considered as an action correction The dictionary is used to
		 * determine miss spellings
		 */
		public LEDSpellchecker(String dictionaryFilePath) throws FileNotFoundException
			{
				this(dictionaryFilePath, 1, 1, 1, 7);
			}

		/*
		 * Creates a new LEDSpellchecker with the specified insertion, deletion,
		 * and substitution costs and edit cost threshold Only potential
		 * corrections with an edit cost at or less than the threshold are
		 * considered as an action correction The dictionary is used to
		 * determine miss spellings
		 */
		public LEDSpellchecker(String dictionaryFilePath, int ins_cost, int del_cost, int subs_cost, int editCostThreshold) throws FileNotFoundException
			{
				this.maxEditCost = editCostThreshold;
				dictionary = new ArrayList<String>();
				for (String line : ReadFileLines(dictionaryFilePath))
					{
						dictionary.add(line);
					}
				insCost = ins_cost;
				delCost = del_cost;
				subsCost = subs_cost;
			}

		/*
		 * input_file_path: file path containing a list of potentially
		 * misspelled words (one word per line) output_file_path: writes the
		 * suggested spelling corrections (one word per line) of each of the
		 * words contained in the other file writes "NULL" if no correction is
		 * found
		 */
		public void spellcheckFile(String input_file_path, String output_file_path) throws IOException
			{
				List<String> corrections = new LinkedList<String>();
				for (String line : ReadFileLines(input_file_path))
					{
						corrections.add(spellCheckWord(line, false));
					}
				WriteFileLines(output_file_path, corrections);
			}

		/*
		 * Returns the spelling suggestion for the given potentially misspelled
		 * word If findEditSteps is true, a list of steps to transform the word
		 * into the suggestion is generated 
		 * These edit steps can be printed
		 * using the toString() method
		 * RETURNS a type of null if no correction is found
		 */
		public String spellCheckWord(String word, boolean findEditSteps)
			{
				int min = Integer.MAX_VALUE;
				String closestWord = "NULL";
				for (String dictWord : dictionary)
					{
						int tmp = led(word, dictWord);
						if (tmp < min && tmp <= maxEditCost)
							{
								min = tmp;
								closestWord = dictWord;
							}
					}
				if (closestWord != "NULL")
					{
						if (findEditSteps) getEdits(word, closestWord);
						return closestWord;
					}
				else return null;
			}

		/*
		 * Generates a list of steps with the lowest possible cost to transform
		 * the source word into the target word These edit steps can be printed
		 * using the toString() method
		 */
		public void getEdits(String source, String target)
			{
				getEdits(source, target, insCost, delCost, subsCost);
			}

		/*
		 * Generates a list of steps with the lowest possible cost to transform
		 * the source word into the target word These edit steps can be printed
		 * using the toString() method Allows you to override the costs of
		 * insertion, deletion, and substitution
		 */
		public void getEdits(String source, String target, int ins_cost, int del_cost, int subs_cost)
			{
				led(source, target, ins_cost, del_cost, subs_cost);
				recoverEditSteps(source, target, ins_cost, del_cost, subs_cost);
			}

		/*
		 * Uses the Levenshtein edit distance algorithm to determine the edit
		 * cost of transforming the source word into the target word
		 */
		public int led(String source, String target)
			{
				return led(source, target, insCost, delCost, subsCost);
			}

		/*
		 * Uses the Levenshtein edit distance algorithm to determine the edit
		 * cost of transforming the source word into the target word Allows you
		 * to override the costs of insertion, deletion, and substitution
		 */
		public int led(String source, String target, int ins_cost, int del_cost, int subs_cost)
			{
				edits = new LinkedList<Edit>();
				costs = new int[source.length() + 1][target.length() + 1];
				if (source.length() == 0)
					{
						if (target.length() == 0)
							return 0;
						else
							return target.length() * ins_cost;
					} else
					{
						if (target.length() == 0)
							return source.length() * del_cost;
						// both target and source contain at least 1 character
						// each
						else
							{
								int newWithSub = 0;
								int newWithDel = 0;
								int newWithIns = 0;
								int minimum = 0;
								for (int r = 0; r < costs.length; r++)
									{
										for (int c = 0; c < costs[0].length; c++)
											{
												if (r == 0)
													{
														costs[0][c] = c * ins_cost;
														continue;
													} else if (c == 0)
													{
														costs[r][0] = r * del_cost;
														continue;
													}
												// r -1 and c - 1 because the
												// column and row indices are
												// each 1 place ahead of the
												// index in the two strings
												// this is because the algorithm
												// uses an empty string as the
												// beginning character of each
												// word, but that's not really
												// in the string
												if (source.charAt(r - 1) == target.charAt(c - 1))
													{
														costs[r][c] = costs[r - 1][c - 1];
													} else
													{
														newWithSub = costs[r - 1][c - 1] + subs_cost;
														newWithDel = costs[r - 1][c] + del_cost;
														newWithIns = costs[r][c - 1] + ins_cost;
														minimum = Math.min(Math.min(newWithSub, newWithDel), newWithIns);
														if (minimum == newWithSub)
															{
																costs[r][c] = newWithSub;
															} else if (minimum == newWithDel)
															{
																costs[r][c] = newWithDel;
															} else if (minimum == newWithIns)
															{
																costs[r][c] = newWithIns;
															}
													}
											}
									}
								totalCost = costs[source.length()][target.length()];
								return totalCost;
							}
					}
			}

		/*
		 * Generates the list of edit steps of the most recently computed
		 */
		private void recoverEditSteps(String source, String target, int ins_cost, int del_cost, int subs_cost)
			{
				int r = costs.length - 1;
				int c = costs[0].length - 1;
				while (r > 0 || c > 0)
					{
						int insert = Integer.MAX_VALUE;
						if (c > 0) insert = costs[r][c - 1];
						int del = Integer.MAX_VALUE;
						if (r > 0) del = costs[r - 1][c];
						int subs = Integer.MAX_VALUE;
						if (r > 0 && c > 0) subs = costs[r - 1][c - 1];
						int minimum = Math.min(Math.min(subs, del), insert);
						if (subs == minimum)
							{
								if (source.charAt(r - 1) != target.charAt(c - 1))
									{
										edits.add(Edit.CreateSubs(r - 1, source.charAt(r - 1), c - 1, target.charAt(c - 1), subs_cost));
									}
								r = r - 1;
								c = c - 1;
							} else if (del == minimum)
							{
								edits.add(Edit.CreateDel(r - 1, source.charAt(r - 1), del_cost));
								r = r - 1;
							} else
							{
								edits.add(Edit.CreateIns(r - 1, target.charAt(c - 1), ins_cost));
								c = c - 1;
							}
					}
			}

		/*
		 * Reads each line from the file
		 */
		private List<String> ReadFileLines(String filePath) throws FileNotFoundException
			{
				Scanner s = new Scanner(new FileReader(filePath));
				List<String> lines = new LinkedList<String>();
				while (s.hasNextLine())
					{
						lines.add(s.nextLine().trim());
					}
				s.close();
				return lines;
			}

		/*
		 * Writes each string within lines to a separate line of the file
		 * filepath
		 */
		private void WriteFileLines(String filePath, List<String> lines) throws IOException
			{
				Writer w = new FileWriter(filePath);
				String newLine = System.getProperty("line.separator");
				for (String line : lines)
					{
						w.write(line + newLine);
					}
				w.close();
			}

		@Override
		public String toString()
			{
				String s = "COST: " + totalCost + "\n";
				s += "EDITS:\n";
				Iterator<Edit> iter = edits.descendingIterator();
				while (iter.hasNext())
					{
						s += iter.next().toString() + "\n";
					}
				return s;
			}
	}
