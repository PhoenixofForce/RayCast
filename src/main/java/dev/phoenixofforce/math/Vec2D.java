package dev.phoenixofforce.math;

public class Vec2D {

	public static void main(String[] args) {
		Vec2D v = new Vec2D(40.5, 40.2);
		System.out.println(v.hashCode());
		v = new Vec2D(40.2, 40.5);
		System.out.println(v.hashCode());
	}

	public double x, y;

	public Vec2D() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public Vec2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec2D(Vec2D in) {
		this(in.x, in.y);
	}

	public Vec2D add(Vec2D v) {
		this.x += v.x;
		this.y += v.y;

		return this;
	}

	public Vec2D add(double x, double y) {
		this.x += x;
		this.y += y;

		return this;
	}

	public Vec2D sub(Vec2D v) {
		this.x -= v.x;
		this.y -= v.y;

		return this;
	}

	public Vec2D sub(double x, double y) {
		this.x -= x;
		this.y -= y;

		return this;
	}

	public Vec2D mult(double r) {
		this.x *= r;
		this.y *= r;

		return this;
	}

	public Vec2D div(double r) {
		this.x /= r;
		this.y /= r;

		return this;
	}

	public Vec2D pow(double e) {
		this.x = Math.pow(this.x, e);
		this.y = Math.pow(this.y, e);

		return this;
	}

	public double scalar(Vec2D second) {
		return second.x * this.x + second.y * this.y;
	}

	public double cross(Vec2D v) {
		return this.x * v.y - this.y * v.x;
	}

	public double distanceTo(Vec2D v) {
		return Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2));
	}

	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public Vec2D normalize() {
		double l = length();
		if(l != 0) div(l);
		else {
			this.x = 0;
			this.y = 0;
		}
		return this;
	}

	public Vec2D normalize(double norm) {
		normalize();
		mult(norm);
		return this;
	}

	public Vec2D normalizeMin(double norm) {
		if(length() > norm) return normalize(norm);
		return this;
	}

	public Vec2D normalizeMax(double norm) {
		if(length() < norm) return normalize(norm);
		return this;
	}

	public double degAngle() {
		return degAngle(new Vec2D(1, 0));
	}

	public double radAngle() {
		return radAngle(new Vec2D(1, 0));
	}

	public double degAngle(Vec2D second) {
		return Math.toDegrees(radAngle(second));
		//return Math.toDegrees(Math.acos(scalar(second) / (length() * second.length())));
	}

	public double radAngle(Vec2D second) {
		return -Math.atan2(this.x * second.y - this.y * second.x, this.x * second.x + this.y * second.y);
		//return Math.acos(scalar(second) / (length() * second.length()));
	}

	public double degAngle(Vec2D origin, Vec2D heading) {
		return this.clone().sub(origin).degAngle(heading);
	}

	public double radAngle(Vec2D origin, Vec2D heading) {
		return this.clone().sub(origin).radAngle(heading);
	}

	public double degAngle(Vec2D origin, double headingDeg) {
		return this.clone().sub(origin).degAngle(degToVec(headingDeg));
	}

	public double radAngle(Vec2D origin, double headingDeg) {
		return this.clone().sub(origin).radAngle(degToVec(headingDeg));
	}

	public Vec2D min(double x1, double y1) {
		this.x = Math.min(this.x, x1);
		this.y = Math.min(this.y, y1);

		return this;
	}

	public Vec2D min(double v) {
		return min(v, v);
	}

	public Vec2D max(double x1, double y1) {
		this.x = Math.max(this.x, x1);
		this.y = Math.max(this.y, y1);

		return this;
	}

	public Vec2D max(double v) {
		return max(v, v);
	}

	public Vec2D degRotate(double deg) {
		return radRotate(Math.toRadians(deg));
	}

	public Vec2D radRotate(double rad) {
		double s = Math.sin(rad);
		double c = Math.cos(rad);

		double newX = x*c - y*s;
		double newY = x*s + y*c;

		x = newX;
		y = newY;

		return this;
	}

	public Vec2D degRotateAround(Vec2D middle, double deg) {
		return radRotateAround(middle, Math.toRadians(deg));
	}

	public Vec2D radRotateAround(Vec2D middle, double rad) {
		sub(middle);
		radRotate(rad);
		add(middle);
		return this;
	}

	public int intX() {
		return (int) x;
	}

	public int intY() {
		return (int) y;
	}

	@Override
	public Vec2D clone() {
		return new Vec2D(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vec2D) {
			Vec2D b = (Vec2D) obj;
			return x == b.x && y == b.y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		long hash = 17L;
		hash = hash*31 + Double.hashCode(x);
		hash = hash*31 + Double.hashCode(y);

		return (int) hash;
	}

	@Override
	public String toString() {
		return String.format("(%f | %f)", x, y);
	}

	public static Vec2D degToVec(double deg) {
		return radToVec(Math.toRadians(deg));
	}

	public static Vec2D degToVec(double deg, double length) {
		return radToVec(Math.toRadians(deg), length);
	}

	public static Vec2D radToVec(double rad) {
		return new Vec2D(Math.cos(rad), Math.sin(rad));
	}

	public static Vec2D radToVec(double rad, double length) {
		return new Vec2D(Math.cos(rad), Math.sin(rad)).mult(length);
	}

	public static Vec2D getOrthogonalVector(Vec2D v) {
		return new Vec2D(v.y, -v.x);
	}

	public static Vec2D getOrthogonalVector(double x, double y) {
		return new Vec2D(y, -x);
	}
}
