package com.wyb.compare;

import com.wyb.compare.model.Similarityable;

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
