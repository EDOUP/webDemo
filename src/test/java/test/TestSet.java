package test;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestSet {
	
	@Autowired
	private RedisTemplate  redisTemplate;
	
	@Test
	public void setValue() {
		redisTemplate.boundSetOps("nameset").add("曹操");
		redisTemplate.boundSetOps("nameset").add("孙权");
		redisTemplate.boundSetOps("nameset").add("刘备");
	}
	
	@Test
	public void getValue() {
		Set set = redisTemplate.boundSetOps("nameset").members();
		System.out.println(set);
	}
	@Test
	public void removeValue() {
		redisTemplate.boundSetOps("nameset").remove("孙权");
	}
	@Test
	public void delete() {
		redisTemplate.delete("nameset");
	}
}
