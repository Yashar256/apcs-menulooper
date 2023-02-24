// **IMPORTANT**: Do NOT include this package statement if you are copying and pasting this code into your classwork
package io.github.yashar256.looper;

/*
 *  MIT License
 *
 *  Copyright (c) 2023 Yashar Azemoon
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * The MenuLooper class is the main class for creating an MenuLooper. Options can be added using the addOption methods, and the MenuLooper can be started by calling options.startLoop();
 * Note that this class uses a wrapper for {@link Scanner}
 */
public class MenuLooper implements Runnable {
    private final Option quitOption = new Option("Exit", () -> {});
    private final List<Option> options = new ArrayList<>();

    /**
     * Adds an option to the menu.
     * <b>Note:</b> An option to quit the program is automatically added to the end of the menu.
     * @param name The name of the option, displayed on the menu
     * @param runnable A callback runnable function to be executed when the user selects the option, or `null` to not do anything on selection
     */
    public void addOption(String name, Runnable runnable) {
        if (runnable == null) {
          runnable = () -> {};
        }
        options.add(new Option(name, runnable));
    }

    /**
     * Adds an option to the menu.
     * <b>Note:</b> An option to quit the program is automatically added to the end of the menu.
     * @param name The name of the option, displayed on the menu
     * @param consumer A callback consumer function to be executed when the user selects the option, or `null` to not do anything on selection.
     */
    public void addOption(String name, Consumer<Scanner> consumer) {
        options.add(new Option(name, consumer));
    }

    /**
     * Starts the option looper. This method should be the last thing called in your program.
     */
    @Override
    public void run() {
        options.add(quitOption);

        StringBuilder menuBuilder = new StringBuilder("Select an option from the list bellow");
        for (int i = 0; i < options.size(); i++) {
            menuBuilder.append(String.format("%n[%d] - %s", i + 1, options.get(i)));
        }
        
        String menuText = menuBuilder.toString();
        Scanner scanner = new Scanner();
        
        while (true) {
            int option = scanner.nextInt(menuText) - 1;
            while (option < 0 || option >= options.size()) {
                System.out.println("Could not recognise that option. Please enter an option from the list above");
                option = scanner.nextInt() - 1;
            }

            if (option == options.size() - 1) {
              return;
            } else {
              options.get(option).callOption(scanner);
            }
        }
    }

    /**
     * A wrapper for {@link java.util.Scanner} which is used by {@link MenuLooper} to read user input from the console.
     *
     * The methods are for the most part analogous to those used in {@link java.util.Scanner}, with a few exceptions:
     * <ul>
     *   <li> Methods such as {@link #nextInt} or {@link #nextBoolean} which require specifics formats to be parsed automatically ask the user to re-enter their input if a parsing error occurs. </li>
     *   <li> {@link #nextBoolean} can recognise more inputs, including acronyms for "yes" and "no" </li>
     *   <li> Each method has a mirror which takes in a String which is automatically displayed to the user before input. For example, writing 
     *     <pre>
     *       int i = scanner.nextInt("What's your favourite number?");
     *     </pre>
     *     is analogous to
     *    <pre>
     *      System.out.println("What's your favourite number?");
     *      int i = nextInt();
     *    </pre>
     *   </li>
     * </ul>
     */
    public static class Scanner {
        private static final Pattern booleanPattern = Pattern.compile("[tTfFyYnN].*");
      
        private final java.util.Scanner stdScanner = new java.util.Scanner(System.in);
        private boolean shouldCallNextLineTwice = false;
        

        /**
         * Prompts the user to enter a word.
         * <b>This is not the same as {@link #nextLine} which enters the next complete line entered.</b>
         * @param The prompt
         * @return The next word
         */
        public String next(String prompt) {
            System.out.println(prompt);
            return next();
        }

        /**
         * Returns the next word entered. 
         * <b>This is not the same as {@link #nextLine} which enters the next complete line entered.</b>
         * @return The next word
         */
        public String next() {
            // Only used to fix nextLine() blank input bug, not input validation
            return properNextType("string", stdScanner::next);
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next integer as a BigInteger
         */
        public BigInteger nextBigInteger(String prompt) {
            System.out.println(prompt);
            return nextBigInteger();
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next integer as a BigInteger
         */
        public BigInteger nextBigInteger() {
            return properNextType("integer", stdScanner::nextBigInteger);
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @param radix The radix
         * @return The next integer as a BigInteger
         */
        public BigInteger nextBigInteger(String prompt, int radix) {
            System.out.println(prompt);
            return nextBigInteger(radix);
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @param radix The radix
         * @return The next integer as a BigInteger
         */
        public BigInteger nextBigInteger(int radix) {
            return properNextType("integer", () -> stdScanner.nextBigInteger(radix));
        }

        /**
         * Prompts the user to input a decimal. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next integer as a BigDecimal
         */
        public BigDecimal nextBigDecimal(String prompt) {
            System.out.println(prompt);
            return nextBigDecimal();
        }

        /**
         * Returns the next decimal inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next decimal as a BigDecimal
         */
        public BigDecimal nextBigDecimal() {
            return properNextType("decimal", stdScanner::nextBigDecimal);
        }

        /**
         * Prompts the user to input a boolean, Any word starting with "T" or "Y" (regardless of case) will be interpreted as true, and vice versa for "F" and "N". Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next true/false or yes/no as a boolean
         */
        public boolean nextBoolean(String prompt) {
            System.out.println(prompt);
            return nextBoolean();
        }

        /**
         * Returns the next boolean inputted by the user. Any word starting with "T" or "Y" (regardless of case) will be interpreted as true, and vice versa for "F" and "N". Automatically asks again if the user does not enter a valid response.
         * @return The next integer as a BigInteger
         */
        public boolean nextBoolean() {
            String rawBoolean = properNextType("boolean", () -> stdScanner.next(booleanPattern)).toLowerCase();
            return rawBoolean.startsWith("t") || rawBoolean.startsWith("y");
        }

        /**
         * Prompts the user to input a byte. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next integer as a byte
         */
        public byte nextByte(String prompt) {
            System.out.println(prompt);
            return nextByte();
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next integer as a byte
         */
        public byte nextByte() {
            return properNextType("integer", stdScanner::nextByte);
        }

        /**
         * Prompts the user to input a byte. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @param radix The radix
         * @return The next integer as a byte
         */
        public byte nextByte(String prompt, int radix) {
            System.out.println(prompt);
            return nextByte(radix);
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @param radix The radix
         * @return The next integer as a byte
         */
        public byte nextByte(int radix) {
            return properNextType("integer", () -> stdScanner.nextByte(radix));
        }

        /**
         * Prompts the user to input a decimal. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next decimal as a double
         */
        public double nextDouble(String prompt) {
            System.out.println(prompt);
            return nextDouble();
        }

        /**
         * Returns the next decimal inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next decimal as a double
         */
        public double nextDouble() {
            return properNextType("decimal", stdScanner::nextDouble);
        }

        /**
         * Prompts the user to input a decimal. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next decimal as a float
         */
        public float nextFloat(String prompt) {
            System.out.println(prompt);
            return nextFloat();
        }

        /**
         * Returns the next decimal inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next decimal as a float
         */
        public float nextFloat() {
            return properNextType("decimal", stdScanner::nextFloat);
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next integer as an int
         */
        public int nextInt(String prompt) {
            System.out.println(prompt);
            return nextInt();
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next integer as an int
         */
        public int nextInt() {
            return properNextType("integer", stdScanner::nextInt);
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @param radix The radix
         * @return The next integer as an int
         */
        public int nextInt(String prompt, int radix) {
            System.out.println(prompt);
            return nextInt(radix);
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @param radix The radix
         * @return The next integer as an int
         */
        public int nextInt(int radix) {
            return properNextType("integer", () -> stdScanner.nextInt(radix));
        }

        /**
         * Prompts the user to enter a String on the console
         * @param prompt The prompt
         * @return The next line entered
         */
        public String nextLine(String prompt) {
            System.out.println(prompt);
            return nextLine();
        }

        /**
         * Returns the next line entered by the user
         * @return The next integer as a BigInteger
         */
        public String nextLine() {
            if (shouldCallNextLineTwice) {
              stdScanner.nextLine();
              shouldCallNextLineTwice = false;
            }
          
            return stdScanner.nextLine();
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next integer as a long
         */
        public long nextLong(String prompt) {
            System.out.println(prompt);
            return nextLong();
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next integer as a long
         */
        public long nextLong() {
            return properNextType("integer", stdScanner::nextLong);
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @param radix The radix
         * @return The next integer as a long
         */
        public long nextLong(String prompt, int radix) {
            System.out.println(prompt);
            return nextLong(radix);
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @param radix The radix
         * @return The next integer as a long
         */
        public long nextLong(int radix) {
            return properNextType("integer", () -> stdScanner.nextLong(radix));
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @return The next integer as a BigInteger
         */
        public short nextShort(String prompt) {
            System.out.println(prompt);
            return nextShort();
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @return The next integer as a short
         */
        public short nextShort() {
            return properNextType("integer", stdScanner::nextShort);
        }

        /**
         * Prompts the user to input an integer. Automatically asks again if the user does not enter a valid response.
         * @param prompt The prompt
         * @param radix The radix
         * @return The next integer as a BigInteger
         */
        public short nextShort(String prompt, int radix) {
            System.out.println(prompt);
            return nextShort(radix);
        }

        /**
         * Returns the next integer inputted by the user. Automatically asks again if the user does not enter a valid response.
         * @param radix The radix
         * @return The next integer as a short
         */
        public short nextShort(int radix) {
            return properNextType("integer", () -> stdScanner.nextShort(radix));
        }

        private <T> T properNextType(String typeName, Supplier<T> nextFunction) {
            shouldCallNextLineTwice = true;
            T input;
            while (true) {
                try {
                    input = nextFunction.get();
                    break;
                } catch (InputMismatchException e) {
                    System.err.printf("Please enter a valid %s and try again%n", typeName);
                    stdScanner.nextLine();
                }
            }

            return input;
        }
    }

    private static class Option {
        private String name;
        private Runnable runnable;
        private Consumer<Scanner> consumer;

        Option(String name, Runnable runnable) {
            this.name = name;
            this.runnable = runnable;
        }

        Option(String name, Consumer<Scanner> consumer) {
            this.name = name;
            this.consumer = consumer;
        }

        void callOption(Scanner scanner) {
            if (runnable != null) {
                runnable.run();
            } else if (consumer != null) {
                consumer.accept(scanner);
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
