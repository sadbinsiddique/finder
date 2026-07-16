# SOLID

## Single Responsibility Principle

Every software component should have one and only one responsibility.

Normal:

```java
// High level of cohesion between "calculateArea() & calculatePerimeter()"
// High level of cohesion between "draw() & rotate(int degree)"

public class Square {
    int side = 5;

    // component 1 
    public int calculateArea() {
        return side * side;
    }

    //component 2
    public int calculatePerimeter() {
        return side * 4;
    }

    //component 3
    public void draw() {
        if (highResolutionMonitor) {
            // render normal image of a square
        } else {
            // render high regulation image of a square
        }
    }

    //component 4
    public void rotate(int degree) {
        //rotate the image in clockwise to 
        //the required degree and re-render
    }
}

// But Low level cohesion in "Square" class
```

Single Responsibility :

```java
public class Square {
    int side = 5;

    // component 1 
    public int calculateArea() {
        return side * side;
    }

    //component 2
    public int calculatePerimeter() {
        return side * 4;
    }
}

// Level cohesion in "Square" class is ++
// responsibility: Measurements of squares
```

```java
public class SquareUI {
    //component 3
    public void draw() {
        if (highResolutionMonitor) {
            // render normal image of a square
        } else {
            // render high regulation image of a square
        }
    }

    //component 4
    public void rotate(int degree) {
        //rotate the image in clockwise to 
        //the required degree and re-render
    }
}
// Level cohesion in "SquareUI" class is ++
// responsibility: Rendering images of squares
```

> High Cohesion helps attain batter adherence to the single responsibility principle

# Liskov

We should be to substitite base class objects with child class object & this should not alter
behavior/characteristics of problem

Example:

```java
// A Rectangle == Square but
// A Square != Rectangle 
public class Rectangle {
    private int width;
    private int height;

    //constructor 
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Getter Method 
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //setter method
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
```

```java
public class Square extends Rectangle {

    // behavior/characteristics has been chang ( 1X )
    public Square(int side) {
        super(side, side);
    }

    // Overlap method as Rectangle
    @Override
    public void setWidth(int width) {
        setSide(width);
    }

    // Overlap method as Rectangle
    @Override
    public void setHeight(int height) {
        setSide(height);
    }

    // behavior/characteristics has been chang ( 2X )
    public void setSide(int side) {
        super.setWidth(side);
        super.setHeight(side);
    }
}
```

> It violated The Rules of Liskov

Fix :

Interface for commonness for two class for avoid config

```java
public interface Shape {
    int computeArea();
}
```

```java 
public class Rectangle implements Shape {
    private int width;
    private int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int computeArea() {
        return height * width;
    }
}
```

```java
public class Square extends Shape {

    private int side;

    public Square(int side) {
        this.side = side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    @Override
    public int computeArea() {
        return side * side;
    }
}
```



