package test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/applicationContext-redis.xml")
public class TestList {
		
	@Autowired
	private RedisTemplate  redisTemplate;
	
	/**
	 * 右压栈
	 */
	@Test
	public void testSetValue() {
		redisTemplate.boundListOps("nameList").rightPush("1");
		redisTemplate.boundListOps("nameList").rightPush("2");
		redisTemplate.boundListOps("nameList").rightPush("3");
	}
	@Test
	public void testGetValue() {
		List list = redisTemplate.boundListOps("nameList").range(0, 10);
		System.out.println(list);
	}
	/**
	 * 左压栈
	 */
	@Test
	public void testSetValue2() {
		redisTemplate.boundListOps("nameList2").leftPush("1");
		redisTemplate.boundListOps("nameList2").leftPush("2");
		redisTemplate.boundListOps("nameList2").leftPush("3");
	}
	@Test
	public void testGetValue2() {
		List list = redisTemplate.boundListOps("nameList2").range(0, 10);
		System.out.println(list);
	}
	@Test
	public void searchByIndex() {
		String str=(String) redisTemplate.boundListOps("nameList").index(2);
		System.out.println(str);	
	}
	@Test
	public  void removeValue() {
		redisTemplate.boundListOps("nameList").remove(1, "2");//移除一个2
	}
	
}
