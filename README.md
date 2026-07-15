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







