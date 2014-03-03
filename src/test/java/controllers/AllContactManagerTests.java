package controllers;

import models.ContactTest;
import models.MeetingTest;
import models.PastMeetingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import util.DataManagerTests;

@RunWith(Suite.class)
@SuiteClasses({ ContactManagerTest.class, ContactTest.class,
		DataManagerTests.class, MeetingTest.class, PastMeetingTest.class })
public class AllContactManagerTests {

}
