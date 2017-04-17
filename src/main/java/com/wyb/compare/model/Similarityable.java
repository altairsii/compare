package com.wyb.compare.model;

import java.util.Arrays;

import com.wyb.compare.CompareUtil;
import com.wyb.compare.annotation.IgnoreField;

public interface Similarityable<T> {

	/**
	 * 比较相似度
	 * 
	 * @param t
	 * @return
	 */
	default int similarityable(T t) {
		return CompareUtil.compareBean(this, t);
	};

	/**
	 * 自学习权重比较相似度
	 * 
	 * @param t
	 * @return
	 */
	default int similarityableByWeights(T t) {
		return CompareUtil.compareBeanWeights(this, t);
	};

	/**
	 * 比较两个数据并输出值不一致的属性
	 * 
	 * @param t
	 * @return
	 */
	default String compare(T t) {
		return Arrays.asList(CompareUtil.getClassField(t.getClass(), null)).parallelStream().filter(f -> {
			return f.getAnnotation(IgnoreField.class) == null && !CompareUtil.compareField(f, this, t);
		}).collect(() -> new StringBuffer(), (s, v) -> {
			try {
				s.append("Field: " + v.getName() + ",newData=" + v.get(this) + ",oldData=" + v.get(t) + ",");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}, (s1, v1) -> {
			s1.append(v1);
		}).toString();
	}

}
