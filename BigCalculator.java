
import java.util.*;
import java.math.*;

public class BigCalculator
{
	
	private ArrayList<String> input;

	final boolean IS_WRONG;

	public BigCalculator(String s)
	{
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

	public BigDecimal calculate(int debuglevel)
	{
		return simplor(input, debuglevel);
	}
	public BigDecimal calculate()
	{
		return simplor(input, 0);
	}

	private BigDecimal simplor(ArrayList<String> s, int debuglevel)
	{
		// return 0 if not solvable
		if (IS_WRONG) return BigDecimal.ZERO;

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

	private BigDecimal cal(ArrayList<String> s, boolean debug)
	{
		for (int i=0;i < s.size();i++)
		{
			if (s.get(i).equals("^"))
			{
				if (debug) print(s, 1);

				BigDecimal a = new BigDecimal(s.get(i - 1));
				BigDecimal b = new BigDecimal(s.get(i + 1));
				BigDecimal r = pow(a, b);
				s.set(i - 1, r.toString());
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
				BigDecimal a = new BigDecimal(s.get(i - 1));
				BigDecimal b = new BigDecimal(s.get(i + 1));
				BigDecimal r = a.multiply(b);
				s.set(i - 1, r.toString());
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
			if (s.get(i).equals("/"))
			{
				if (debug) print(s, 1);

				BigDecimal a = new BigDecimal(s.get(i - 1));
				BigDecimal b = new BigDecimal(s.get(i + 1));
				BigDecimal r = a.divide(b);
				s.set(i - 1, r.toString());
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

				BigDecimal a = new BigDecimal(s.get(i - 1));
				BigDecimal b = new BigDecimal(s.get(i + 1));
				BigDecimal r = a.add(b);
				s.set(i - 1, r.toString());
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
			if (s.get(i).equals("-"))
			{
				if (debug) print(s);

				BigDecimal a = new BigDecimal(s.get(i - 1));
				BigDecimal b = new BigDecimal(s.get(i + 1));
				BigDecimal r = a.subtract(b);
				s.set(i - 1, r.toString());
				s.remove(i + 1);s.remove(i);

				if (debug) print(s, 1);
				i = 0; continue;
			}
		}
		return new BigDecimal(s.get(0));
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
					BigDecimal tmp = new BigDecimal(s.get(i - 1) + "." + s.get(i + 1));
					s.set(i - 1, tmp.toString());
					s.remove(i + 1);s.remove(i--);
				}
				catch (NumberFormatException e)
				{
					try
					{
						BigDecimal tmp = new BigDecimal("0." + s.get(i + 1));
						s.set(i, tmp.toString());
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
			if (CHAR.contains(s.get(i))) s.set(i, "3.141592653589793");
			else
			{
				// euler
				CHAR = new ArrayList<String>(Arrays.asList(new String[]{"e","\\e"}));
				if (CHAR.contains(s.get(i))) s.set(i, "2.718281828459045");
				else
				{
					// Feigenbaum a & d
					CHAR = new ArrayList<String>(Arrays.asList(new String[]{"α","\\feigenbauma"}));
					if (CHAR.contains(s.get(i))) s.set(i, "2.502907875095892");
					else
					{
						CHAR = new ArrayList<String>(Arrays.asList(new String[]{"δ","\\feigenbaumd"}));
						if (CHAR.contains(s.get(i))) s.set(i, "4.669201609102990");}}}
		}

		// merging "-"s and "+"s to their owners : {(,-,2,)}>>{(,-2,)} && {(,1,,,-,1,)}>>{(,1,,,-1,)}
		for (int i=1;i < s.size() - 1;i++)
		{
			if (s.get(i).equals("-") || s.get(i).equals("+"))
			{
				if(!s.get(i-1).equals("(")&&!s.get(i-1).equals(",")) continue;
				try
				{
					BigDecimal a = new BigDecimal(s.get(i) + s.get(i + 1));
					s.set(i, a.toString());
					s.remove(i + 1);
				}catch (NumberFormatException e){}
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
				try
				{
					if (s.get(i + 1).equals(")")) return false;
					if (s.get(i + 1).equals(",")) return false;
				}
				catch (Exception e)
				{}
			}

			//[1]check for paranthsis
			if (s.get(i).equals("(")) a++;
			else if (s.get(i).equals(")")) a--;
		}

		if (a != 0) return false;//[1]

		return true;
	}


	// MATH FUNCTIONS
	private BigDecimal pow(BigDecimal a, BigDecimal b)
	{
		if (b.compareTo(new BigDecimal(b.toBigInteger().toString())) == 0)
		{
			if (b.compareTo(BigDecimal.ZERO) >= 0)
			{
				BigDecimal r = BigDecimal.ONE;
				while (b.compareTo(BigDecimal.ZERO) > 0){
					r = r.multiply(a);
					b = b.subtract(BigDecimal.ONE);
				}
				return r;	
			}
			else
			{
				BigDecimal r = BigDecimal.ONE;
				while (b.compareTo(BigDecimal.ZERO) < 0){
					r = r.multiply(a);
					b = b.add(BigDecimal.ONE);
				}
				return BigDecimal.ONE.divide(r);
			}
		}
		else
		{
			return BigDecimal.ZERO;
		}
	}

}
