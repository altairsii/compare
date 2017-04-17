package com.wyb.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;

import com.wyb.compare.model.Similarityable;

public class CompareTest {

	/**
	 * 测试实体类
	 * 
	 * @author wangyongbing
	 *
	 */
	public class CompareModel implements Similarityable<CompareModel> {

		private int id;

		private String name;

		private String sex;

		private int age;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "CompareModel [id=" + id + ", " + (name != null ? "name=" + name + ", " : "")
					+ (sex != null ? "sex=" + sex + ", " : "") + "age=" + age + "]";
		}

	}

	@Test
	public void compareSimilarityListTest() {
		List<CompareModel> source = new ArrayList<>();
		List<CompareModel> target = new ArrayList<>();
		CompareModel sh = null;
		for (int i = 0; i < 10; i++) {
			sh = new CompareModel();
			sh.setId(i);
			sh.setAge(i * 10);
			sh.setName(i + "test");
			sh.setSex(i + "");
			source.add(sh);
			target.add(sh);
		}
		source.addAll(target);
		Map<Integer, List<CompareModel>> m = source.parallelStream()
				.collect(Collectors.groupingBy(CompareModel::getId, TreeMap::new, Collectors.toList()));
		m.forEach((a, c) -> {
			System.out.println(a);
			System.out.println(c);
		});

		//		List<Similarity<CompareModel>> l = CompareUtil.compareSimilarityList(source, target);
		//
		//		
		//		for (Similarity<CompareModel> s : l) {
		//			s.sort(SimilaritySort.DESC);
		//			System.out.println(s.getObj());
		//			System.out.println(s.getNode(0));
		//			System.out.println(s.getSimilarity(0));
		//		}
	}

	@Test
	public void atanTest() {
		double pi_2 = Math.PI / 2;
		int max = 1000000;
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(12080))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(15008))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(93094))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(23086))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(119322))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(236))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(48820))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(7229))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(34988))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(6726))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(67536))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(6244))));
		System.out.println(1000 * (pi_2 - Math.atan(Math.sqrt(1694))));
		System.out.println(pi_2 - Math.atan(Math.sqrt(119322)));
		System.out.println(pi_2 - Math.atan(Math.sqrt(236)));
		System.out.println(pi_2 - Math.atan(Math.sqrt(1694)));
		System.out.println(pi_2 - Math.atan(Math.sqrt(6244)));
		System.out.println(pi_2 - Math.atan(Math.sqrt(67536)));
		System.out.println(pi_2 - Math.atan(Math.sqrt(6726)));
		System.out.println(((pi_2 - Math.atan(Math.sqrt(1.78)))));
		System.out.println(pi_2 - Math.atan(Math.sqrt(Math.sqrt(1193220))));
		System.out.println(pi_2 - Math.atan(Math.sqrt(Math.sqrt(11932200))));
		System.out.println(pi_2 - Math.atan(Math.sqrt(Math.sqrt(236))));
	}
}
