package com.wyb.compare.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 比较结果存储对象
 * 
 * @author wangyongbing
 *
 */
public class Similarity<T> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private T obj;

	private volatile List<Node<T>> orderSimilatity = new ArrayList<Node<T>>();

	public void put(T t, int similarity) {
		Node<T> n = new Node<T>();
		n.setValue(t);
		n.setSimilarity(similarity);
		orderSimilatity.add(n);
	}

	/**
	 * 获取相似度最高的元素
	 * 
	 * @return
	 */
	public T outputInconsistent() {
		if (orderSimilatity == null || orderSimilatity.isEmpty()) {
			return null;
		}
		return orderSimilatity.get(0).getValue();
	}

	/**
	 * 获取索引元素的相似度元素
	 * 
	 * @param index
	 * @return
	 */
	public T getNode(int index) {
		return orderSimilatity.get(index).getValue();
	}

	/**
	 * 获取索引元素的相似度
	 * 
	 * @param index
	 * @return
	 */
	public int getSimilarity(int index) {
		if (orderSimilatity == null || orderSimilatity.isEmpty()) {
			return 0;
		}
		return orderSimilatity.get(index).getSimilarity();
	}

	/**
	 * 对相似元素用相似度排序</br>
	 * 默认逆序
	 */
	public Similarity<T> sort(SimilaritySort sort) {
		orderSimilatity.sort((c1, c2) -> {
			switch (sort) {
			case ASC:
				return c1.getSimilarity() - c2.getSimilarity();
			case DESC:
				return c2.getSimilarity() - c1.getSimilarity();
			default:
				return c2.getSimilarity() - c1.getSimilarity();
			}

		});
		return this;
	}

	/**
	 * 相似度对象
	 * 
	 * @author wangyongbing
	 *
	 * @param <T>
	 */
	@SuppressWarnings("hiding")
	public class Node<T> {

		int similarity;

		T value;

		public int getSimilarity() {
			return similarity;
		}

		public void setSimilarity(int similarity) {
			this.similarity = similarity;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}

	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public List<Node<T>> getOrderSimilatity() {
		return orderSimilatity;
	}

	public void showWeight() {
		orderSimilatity.forEach(s -> {
			logger.info("weight:{}", s.getSimilarity());
		});
	}

	public void setOrderSimilatity(List<Node<T>> orderSimilatity) {
		this.orderSimilatity = orderSimilatity;
	}

}
