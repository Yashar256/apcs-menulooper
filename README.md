# MenuLooper
Tired of spending a considerable portion of classtime writing code to loop through a menu? Tired of <a href="https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html"> Scanner </a> being annoying? MenuLooper allows you to write code to efficiently test and show off your classwork so you can spend more time programming novel features that don't require copy and pasting your last classworks code and tweaking it until it works.

## The basics
You can add menu options to an MenuLooper instance which executes a function when selected by the user. MenuLooper uses its own variation of the Scanner class which:

- Gets rid of bulky `try {...} catch` blocks around methods like scan.nextInt() and automatically prompts users to enter the correct type if a wrong type was entered by accident
- Contains nextBoolean() which can recognise variations of true/false or yes/no
- (No promises) but hopefully this can fix the next()/nextLine() bugginess at some point
## Example

```
Select an option from the list bellow
[1] - Print the entire list
[2] - Sort the list
[3] - Add an element to the list
[4] - Remove an element from the list
[5] - Quit the program
```

An interactive selection list such as the one above can be generated with the following code

```java
MenuLooper menu = new MenuLooper();

menu.addOption("Print the entire list", () -> list.forEach(System.out::println));
menu.addOption("Sort the list", () -> {
  Collections.sort(list);
});

// Optionally, you can pass in a Scanner object for more interactions
menu.addOption("Add an element to the list", list.addOption(Scanner::nextLine));
menu.addOption("Remove an element from the list", scanner -> {
  String element = scanner.nextString("Please enter the name of the element to remove");
  list.remove(element);
});

menu.run();
```
Notice that `[5] - Quit the program` is automatically added for you!

## Instalation
Currently this project is in development, so it may only be downloaded from its github repository. It is planned to be released onto maven for easy installation on repl.it in the future.