package com.ahwers.marvin.framework.application.state;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ApplicationStateTest {

   @Test
   public void getName() {
      TestApplicationState testState = new TestApplicationState("Test", 0);
      assertTrue(testState.getApplicationName().equals("Test"));
   }

   @Test
   public void getVersion() {
      TestApplicationState testState = new TestApplicationState("Test", 0);
      assertTrue(testState.getVersion() == 0);
   }

   @Test
   public void incrementVersion() {
      TestApplicationState testState = new TestApplicationState("Test", 0);
      testState.incrementVersion();
      assertTrue(testState.getVersion() == 1);
   }

   @Test
   public void isFresherThanSameVersionButDifferentContent() {
      TestApplicationState testState1 = new TestApplicationState("Test", 0);
      TestApplicationState testState2 = new TestApplicationState("Test", 0);
      testState2.setTest("new_test");
      assertTrue(testState2.isFresherThan(testState1));
   }

   @Test
   public void isFresherThanNullOperand() {
      TestApplicationState testState = new TestApplicationState("Test", 0);
      assertTrue(testState.isFresherThan(null));
   }

   @Test
   public void isFresherThanOperandHasGreaterVersion() {
      TestApplicationState testState1 = new TestApplicationState("Test", 0);
      TestApplicationState testState2 = new TestApplicationState("Test", 1);
      assertFalse(testState1.isFresherThan(testState2));
   }

   @Test
   public void isFresherThanOperandHasLowerVersion() {
      TestApplicationState testState1 = new TestApplicationState("Test", 0);
      TestApplicationState testState2 = new TestApplicationState("Test", 1);
      assertTrue(testState2.isFresherThan(testState1));
   }

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
