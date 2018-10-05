package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestValue {
	
	@Autowired
	private RedisTemplate  redisTemplate;
	
	@Test
	public void setValue() {
		redisTemplate.boundValueOps("name").set("itcast");
	}
	@Test
	public void  getValue() {
		String str = (String) redisTemplate.boundValueOps("name").get();
		System.out.println(str);
	}
	@Test
	public void deleteValue() {
		redisTemplate.delete("name");
	}
}
