package com.contact.test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DataModelTest.class, EventBusTest.class, ProductSearchTest.class })
public class AllTests {

}
