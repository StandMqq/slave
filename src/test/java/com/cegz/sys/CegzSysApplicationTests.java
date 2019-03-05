package com.cegz.sys;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cegz.sys.service.adver.impl.DrivingRegistrationServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CegzSysApplicationTests {

	private DrivingRegistrationServiceImpl dr;
	@Test
	public void contextLoads() {
//		dr.getDrivingRegistrationBySponsorId(1L,"","", 1, 5);
	}

}
