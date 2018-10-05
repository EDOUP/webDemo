package test;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class testHash {
		
	@Autowired
	private RedisTemplate  redisTemplate;
	
	@Test
	public void testSetValue() {
		redisTemplate.boundHashOps("nameList").put("1","a");
		redisTemplate.boundHashOps("nameList").put("2","b");
		redisTemplate.boundHashOps("nameList").put("3","c");
		redisTemplate.boundHashOps("nameList").put("4","d");
		redisTemplate.boundHashOps("nameList").put("5","e");
	}
	
	@Test
	public void testGetKeys() {
		Set keys = redisTemplate.boundHashOps("nameList").keys();
		System.out.println(keys);
	}
	
	@Test
	public void testGetValues() {
		List values = redisTemplate.boundHashOps("nameList").values();
		System.out.println(values);
	}
	@Test
	public void searchValueByKey() {
		String str = (String) redisTemplate.boundHashOps("nameList").get("2");
		System.out.println(str);
	}
	@Test
	public void removeValue() {
		redisTemplate.boundHashOps("nameList").delete("2");
	}
	
}
