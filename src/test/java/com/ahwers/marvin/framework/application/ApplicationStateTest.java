package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ApplicationStateTest {

   // TODO: More tests
   /**
    * Test Cases:
    */

   @Test
   public void cloneTest() throws CloneNotSupportedException {
      TestApplicationState testState = new TestApplicationState("Test", 0);
      testState.setTest("cloned test");
      TestApplicationState clonedState = (TestApplicationState) testState.clone();
      assertAll(
         () -> assertTrue(testState != clonedState),
         () -> assertTrue(testState.isSameAs(clonedState))
      );
   }

}
