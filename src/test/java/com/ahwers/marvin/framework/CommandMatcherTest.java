package com.ahwers.marvin.framework;

import org.junit.Test;

// TODO: Rename the fuzzy matcher class to CommandMatcher.
public class CommandMatcherTest {

// argument extraction
// command encapsulation (a command that is a subset of another command will not be matched by the super-set command). Maybe the ^$ things for commands will do this.
// arbitrarily repeated arguments e.g: hard coded bit (?<repeatable_variable_bit>.+? )+ would match "hard coded bit first_variable second_variable" with the two arguments "repeatable_variable_bit_1" and "repeatable_variable_bit_2"

    // TODO: Delete.
    // public static void main(String args[]) {
	// 	Pattern pattern = Pattern.compile("(6)+$", Pattern.CASE_INSENSITIVE);
	//     FuzzyMatcher matcher = new FuzzyMatcher(pattern.matcher("666"));
	//     if (matcher.find()) {
	//     	for (int i = 1; i < matcher.groupCount() + 1; i++) {
	//     		System.out.println(matcher.groupName(i) + " : " + matcher.group(i));
	//     	}
	//     }
	//     else {
	//     	System.out.println("cry");
	//     }
	// }

    @Test
    public void argumentExtractionTest() {

    }

    @Test
    public void commandEncapsulationTest() {

    }

    @Test
    public void repeatableArgumentExtractionTest() {

    }
}
