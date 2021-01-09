//import Main;
import java.util.*;
import java.math.*;public class Calculator
{
	private ArrayList<String> input;

	final boolean IS_WRONG;

	public Calculator(String s)
	{
		BigDecimal i;
		input = new ArrayList<String>(Arrays.asList(s.split("(?<=[ ()\\[\\]{}+-/*\\^])|(?=[ ()\\[\\]{}+-/*\\^])")));
		input = strrepair(input);
		if (!issolvable(input))
		{
			System.out.println("Wrong input!");
			IS_WRONG = true;
		}
		else
		{ IS_WRONG = false;}
	}

	private void print(ArrayList<String> ss, int tabs)
	{ PRINT(ss, tabs);}
	private void print(ArrayList<String> ss)
	{ PRINT(ss, 0);}

	private void PRINT(ArrayList<String> ss, int tabs)
	{
		boolean b = tabs == 0;
		System.out.printf("\n");
		while (--tabs >= 0) System.out.printf("--------");

		System.out.printf((b ? ">>\t" : ">\t"));
		for (String s : ss)
			System.out.printf(" " + s + " ");
		System.out.printf("\n");
	}

	public double calculate(int debuglevel)
	{
		return simplor(input, debuglevel);
	}
	public double calculate()
	{
		return simplor(input, 0);
	}

	private double simplor(ArrayList<String> s, int debuglevel)
	{
		// return 0 if not solvable
		if (IS_WRONG) return 0;

		// looking for ')'s
		if (debuglevel > 0) print(s);
		for (int i=0;i < s.size();i++)
		{
			if (s.get(i).equals(")"))
			{
				s.remove(i);
				// get all the elements inside paranthesis
			 	// and delete from '(' till ')' including paranthesis
				ArrayList<String> tmp = new ArrayList<String>();
				int j;
				for (j = i - 1;j >= 0;j--)
				{
					if (s.get(j).equals("("))
					{ break;}
					tmp.add(0, s.get(j));
					s.remove(j);
				}
				s.set(j, String.valueOf(cal(tmp, (debuglevel == 2 ? true : false))));
				s = strrepair(s);
				if (debuglevel > 0) print(s);
				i = j;
			}
		}
		return cal(s, (debuglevel == 2 ? true : false));
	}

	private double cal(ArrayList<String> s, boolean debug)
	{
		for (int i=0;i < s.size();i++)
		{
			if (s.get(i).equals("^"))
			{
				if (debug) print(s, 1);

				double a = Double.parseDouble(s.get(i - 1));
				double b = Double.parseDouble(s.get(i + 1));
				double r = pow(a, b);
				s.set(i - 1, String.valueOf(r));
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
		}

		for (int i=0;i < s.size();i++)
		{

			if (s.get(i).equals("*"))
			{
				if (debug) print(s, 1);
				double a = Double.parseDouble(s.get(i - 1));
				double b = Double.parseDouble(s.get(i + 1));
				double r = a * b;
				s.set(i - 1, String.valueOf(r));
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
			if (s.get(i).equals("/"))
			{
				if (debug) print(s, 1);

				double a = Double.parseDouble(s.get(i - 1));
				double b = Double.parseDouble(s.get(i + 1));
				double r = a / b;
				s.set(i - 1, String.valueOf(r));
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
		}

		for (int i=0;i < s.size();i++)
		{
			if (s.get(i).equals("+"))
			{
				if (debug) print(s);

				double a = Double.parseDouble(s.get(i - 1));
				double b = Double.parseDouble(s.get(i + 1));
				double r = a + b;
				s.set(i - 1, String.valueOf(r));
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
			if (s.get(i).equals("-"))
			{
				if (debug) print(s);

				double a = Double.parseDouble(s.get(i - 1));
				double b = Double.parseDouble(s.get(i + 1));
				double r = a - b;
				s.set(i - 1, String.valueOf(r));
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
		}
		return Double.parseDouble(s.get(0));
	}

	// TECH FUNCTIONS
	private ArrayList<String> strrepair(ArrayList<String> s)
	{
		// removing the ""s from the arraylist
		for (int i=0;i < s.size();i++)
			if (s.get(i).equals("") || s.get(i).equals(" ")) s.remove(i--);

		// merging "."s to nighbors : {1,.,5}>>{1.5}
		for (int i=0;i < s.size() - 1;i++)
		{
			if (s.get(i).equals("."))
			{
				try
				{
					double tmp = Double.parseDouble(s.get(i - 1) + "." + s.get(i + 1));
					s.set(i - 1, String.valueOf(tmp));
					s.remove(i + 1);s.remove(i--);
				}
				catch (NumberFormatException e)
				{
					try
					{
						double tmp = Double.parseDouble("0." + s.get(i + 1));
						s.set(i, String.valueOf(tmp));
						s.remove(i + 1);
					}
					catch (NumberFormatException ex)
					{}
				}
			}
		}

		// special characters
		for (int i=0;i < s.size();i++)
		{
			// pi
			ArrayList<String> CHAR = new ArrayList<String>(Arrays.asList(new String[]{"π","\\pi"}));
			if (CHAR.contains(s.get(i))) s.set(i, "3.1415926536");
			else
			{
				// euler
				CHAR = new ArrayList<String>(Arrays.asList(new String[]{"e","\\e"}));
				if (CHAR.contains(s.get(i))) s.set(i, "2.7182818284");
				else
				{
					// Feigenbaum a & d
					CHAR = new ArrayList<String>(Arrays.asList(new String[]{"α","\\feigenbauma"}));
					if (CHAR.contains(s.get(i))) s.set(i, "2.5029");
					else
					{
						CHAR = new ArrayList<String>(Arrays.asList(new String[]{"δ","\\feigenbaumd"}));
						if (CHAR.contains(s.get(i))) s.set(i, "4.6692");}}}
		}

		// merging "-"s and "+"s to their owners : {(,-,2,)}>>{(,-2,)} && {(,1,,,-,1,)}>>{(,1,,,-1,)}
		for (int i=1;i < s.size() - 1;i++)
		{
			if (s.get(i).equals("-") || s.get(i).equals("+"))
			{
				if(!s.get(i-1).equals("(")&&!s.get(i-1).equals(",")) continue;
				try
				{
					double a = Double.parseDouble(s.get(i) + s.get(i + 1));
					s.set(i, String.valueOf(a));
					s.remove(i + 1);
				}
				catch (NumberFormatException e)
				{}
			}
		}

		// pow(a,b)>>(a)^(b)
		for (int i=0;i < s.size();i++)
		{
			if (s.get(i).equals(","))
			{
				for (int j=i - 1;j > 0;j--) if (s.get(j).equals("("))
						for (int k=i + 1;k < s.size();k++) if (s.get(k).equals(")"))
							{
								if (s.get(j - 1).equals("pow"))
								{
									// pow(A,B) >> pow(A,(B) >> pow(A,^(B) >> pow(A)^(B)
									s.add(i + 1, "(");
									s.add(i + 1, "^");
									s.set(i, ")");
									// pow(A)^(B) >> (A)^(B)
									s.remove(j - 1);
								}
							}
			}
		}
		return s;
	}

	private boolean issolvable(ArrayList<String> s)
	{
		int a=0;//[1]
		ArrayList<String> Actions = new ArrayList<String>(Arrays.asList(new String[]{"-","+","*","^","/"}));//[0]

		for (int i=0;i < s.size();i++)
		{
			//[0]check for actions's neighbors
			if (Actions.contains(s.get(i)))
			{
				if (i == 0 || i == s.size() - 1) return false;
				if (Actions.contains(s.get(i - 1))) return false;
				if (Actions.contains(s.get(i + 1))) return false;
				try{
					if(s.get(i+1).equals(")")) return false;
					if(s.get(i+1).equals(",")) return false;
				}catch(Exception e){}
			}

			//[1]check for paranthsis
			if (s.get(i).equals("(")) a++;
			else if (s.get(i).equals(")")) a--;
		}

		if (a != 0) return false;//[1]
		
		return true;
	}


	// MATH FUNCTIONS
	private double pow(double a, double b)
	{
		if (b == (int)b)
		{
			if (b >= 0)
			{
				double r = 1; while (--b >= 0) r *= a;
				return r;	
			}
			else
			{
				double r = 1; while (++b <= 0) r *= a;
				return 1 / r;
			}
		}
		else
		{
			return Math.pow(a, b);
		}
	}



}
