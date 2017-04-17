package com.wyb.compare;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wyb.compare.annotation.IgnoreField;
import com.wyb.compare.annotation.Weights;
import com.wyb.compare.model.FieldWeight;
import com.wyb.compare.model.Similarity;
import com.wyb.compare.model.Similarityable;

public class CompareUtil {

	private static Logger logger = LoggerFactory.getLogger(CompareUtil.class);

	/**
	 * 权重记录
	 */
	public volatile static Map<Field, Double> weights = new ConcurrentHashMap<Field, Double>();

	/**
	 * PI/2
	 */
	private static final double pi_2 = Math.PI / 2;

	/**
	 * 最大权重
	 */
	public volatile static double MAX_WEIGHTS = 0;

	/**
	 * 最小权重
	 */
	public volatile static double MIN_WEIGHTS = 0;

	/**
	 * 相似度比较
	 * 
	 * @param <T>
	 * @param source
	 * @param target
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<Similarity<T>> compareSimilarityList(List<T> source, List<T> target) {
		List<Similarity<T>> similarityList = new ArrayList<>();
		source.stream().forEach(c -> {
			Similarity<T> s = new Similarity<T>();
			s.setObj(c);
			target.stream().forEach(p -> {
				s.put(p, ((Similarityable) c).similarityable((Similarityable) p));
			});
			similarityList.add(s);
		});
		return similarityList;
	}

	/**
	 * 自学习权重相似度比较
	 * 
	 * @param <T>
	 * @param source
	 * @param target
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<Similarity<T>> compareSimilarityByWeightsList(List<T> source, List<T> target) {
		List<Similarity<T>> similarityList = new ArrayList<>();
		source.stream().forEach(c -> {
			Similarity<T> s = new Similarity<T>();
			s.setObj(c);
			target.stream().forEach(p -> {
				s.put(p, ((Similarityable) c).similarityableByWeights((Similarityable) p));
			});
			similarityList.add(s);
		});
		return similarityList;
	}

	/**
	 * 返回一个类型的所有属性（包含父类）
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getClassField(Class<?> clazz, Field[] field) {
		if (clazz == null) {
			return null;
		}
		field = ArrayUtils.addAll(field, clazz.getDeclaredFields());
		if (clazz.getSuperclass() == Object.class) {
			return field;
		} else {
			return getClassField(clazz.getSuperclass(), field);
		}
	}

	/**
	 * 更新权重值
	 * 
	 * @param f
	 */
	private static int updateWeights(Field f) {
		if (weights.containsKey(f)) {
			double index = weights.get(f);
			index += pi_2 - Math.atan(Math.sqrt(index));
			weights.put(f, index);
			return (int) index;
			// return (int) ((pi_2 - Math.atan(Math.sqrt(Math.sqrt(index)))) *
			// 1000);
		} else {
			weights.put(f, 1d);
			return 1;
		}
	}

	/**
	 * 比较两个对象的相似度
	 * 
	 * @param obj1
	 * @param obj2
	 * @param isOutput
	 *            是否输出不一致属性
	 * @return
	 */
	public static int compareBean(Object obj1, Object obj2) {
		Class<?> clazz1 = obj1.getClass();
		Class<?> clazz2 = obj2.getClass();
		if (!clazz1.equals(clazz2)) {
			logger.error("obj1`s class {} is not compare obj2`class {}.", clazz1, clazz2);
			return 0;
		}

		return Arrays.asList(getClassField(clazz1, null)).stream()
				.collect(() -> new ArrayList<FieldWeight>(), (a, b) -> {
					a.add(new FieldWeight(b));
				}, (a1, b1) -> {
					a1.addAll(b1);
				}).parallelStream().filter(s -> s.getField() != null)
				.filter(s -> s.getField().getAnnotation(IgnoreField.class) == null)
				.mapToInt(f -> f.getWeight(obj1, obj2)).sum();

	}

	/**
	 * 获取权重
	 * 
	 * @param count
	 * @param f
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static int getWeights(Field f, Object o1, Object o2) {
		if (compare(o1, o2)) {
			Weights w = null;
			if ((w = f.getAnnotation(Weights.class)) != null) {
				return w.value();
			}
			return 1;
		}
		return 0;
	}

	/**
	 * 获取权重
	 * 
	 * @param count
	 * @param f
	 * @param o1
	 * @param o2
	 * @return
	 */
	private static int getWeights(int count, Field f, Object o1, Object o2) {
		if (compare(o1, o2)) {
			Weights w = null;
			int o = 1;
			if ((w = f.getAnnotation(Weights.class)) != null) {
				o = w.value();
			}
			count += o;
		}
		return count;
	}

	/**
	 * 比较两个对象的同一个属性值是否相等
	 * 
	 * @param f
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean compareField(Field f, Object obj1, Object obj2) {
		try {
			f.setAccessible(true);
			Object o1 = f.get(obj1);
			Object o2 = f.get(obj2);
			return CompareUtil.compare(o1, o2);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取权重并输出不一致的元素
	 * 
	 * @param count
	 * @param f
	 * @param o1
	 * @param o2
	 * @return
	 */

	private static int getWeightsAndOutPutInconsistent(int count, Field f, Object o1, Object o2, Object obj1) {
		if (compare(o1, o2)) {
			Weights w = null;
			int o = 1;
			if ((w = f.getAnnotation(Weights.class)) != null) {
				o = w.value();
			}
			count += o;
		} else if (o1 != null || o2 != null) {
			logger.info("Field name ={},oldData={},newData={},Bean={}", f.getName(), o1, o2, obj1);
		}
		return count;
	}

	/**
	 * 比较两个对象是否相等
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean compare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		try {
			return o1 != null && o2 != null && (o1.equals(o2) || ((Comparable) o1).compareTo((Comparable) o2) == 0);
		} catch (Exception e) {
			logger.error("CompareUtil.compare is error.", e);
		}
		return false;
	}

	/**
	 * 自适配权重比较两个对象的相似度
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static int compareBeanWeights(Object obj1, Object obj2) {
		Class<?> clazz1 = obj1.getClass();
		Class<?> clazz2 = obj2.getClass();
		if (!clazz1.equals(clazz2)) {
			logger.error("obj1`s class {} is not compare obj2`class {}.", clazz1, clazz2);
			return 0;
		}
		int count = 0;
		Field[] objField = getClassField(clazz1, null);
		try {
			for (Field f : objField) {
				if (f.getAnnotation(IgnoreField.class) != null) {
					continue;
				}
				f.setAccessible(true);
				Object o1, o2;

				if ((o1 = f.get(obj1)) != null && (o2 = f.get(obj2)) != null && o1.equals(o2)) {
					count += updateWeights(f);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("CompareUtils.compareBean is error", e);
			e.printStackTrace();
		}

		return count;
	}
}
