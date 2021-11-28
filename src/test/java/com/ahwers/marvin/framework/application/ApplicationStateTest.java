package com.ahwers.marvin.framework.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ApplicationStateTest {

   /**
    * Test Cases:
    */

   private class TestApplicationState extends ApplicationState {

      private String test = "test";

      public String getTest() {
         return this.test;
      }

      public void setTest(String test) {
         this.test = test;
      }

      @Override
      public boolean isSameAs(ApplicationState appState) {
         TestApplicationState testApplicationState = (TestApplicationState) appState;
         return testApplicationState.getTest().equals(this.test);
      }

   }

   @Test
   public void cloneTest() throws CloneNotSupportedException {
      TestApplicationState testState = new TestApplicationState();
      testState.setTest("cloned test");
      TestApplicationState clonedState = (TestApplicationState) testState.clone();
      assertAll(
         () -> assertTrue(testState != clonedState),
         () -> assertTrue(testState.isSameAs(clonedState))
      );
   }

}
