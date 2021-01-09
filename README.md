# JCalculator

**api** :

***Calculator:***

constructors:
	
```Calculator(String calculation);```

calculation is the actual calculation you want to calculate.

public methods:

```double calculate(int debug_level)```

returns the calculated value in double.
debug_level = 0: nothing printed during calculation.
debug_level = 1: only simplifications are printed.
debug_level = 2: all behind the scenes are printed.

```double calculate()```

returns the calculated value in double.
and nothing printed during calculation.
	
public variables:

```boolean IS_SOLVABLE;```

if solvable, returns true; otherwise false.


***BigCalculator:***
constructors:

```BigCalculator(String calculation);```
	
calculation is the actual calculation you want to calculate.

public methods:

```BigDecimal calculate(int debug_level)```
	
returns the calculated value in BigDecimal.
debug_level = 0: nothing printed during calculation.
debug_level = 1: only simplifications are printed.
debug_level = 2: all behind the scenes are printed.

```BigDecimal calculate()```
	
returns the calculated value in BigDecimal.
and nothing printed during calculation.

public variables:

```boolean IS_SOLVABLE;```

if solvable, returns true; otherwise false.


**usage** :

write

```Calculator cal = new Calculator("12+3/2");```

to declare a calculation variable. (you can choose your own equation instead of "12+3/2" ).

to check whether it's solvable or not:

```if(cal.IS_SOLVABLE)```


to write the equation:
```
system.out.println(cal.calculate());
```
it returns a double variable, so you cal also consider it as a new variable:
```
double r = cal.calculate();
```

BigCalculate is the same, only that it returns a BigDecimal instead of a double. so you can do the same thing with BigCalculate with only difference being to change the ```double r``` to ```BigDecimal r```.


***EXAMPLE:***

	Scanner sc = new Scanner(System.in);
	System.out.print("write equation:\t");
	BigCalculation cal = new BigCalculation(sc.nextline());
	
	if(cal.IS_SOLVABLE) System.println("=\t"+cal.calculate);
	else System.println("ERROR WITH INPUT");
