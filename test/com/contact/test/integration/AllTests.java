package com.contact.test.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PersonIntegrationTest.class, TimeIntegrationTest.class })
public class AllTests {

}
