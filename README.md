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

# Dependency Inversion principal

1) High level modules should not depend upon low level moduls. Both should depend upon abstractions

2) Abstractions should not depend upon details. Details should depend upon abstractions

```java
import org.apache.juli.JsonFormatter;
import org.apache.logging.log4j.message.Message;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;

public class MassagePrinter {
    public void writeMessage(Message msg, String filename) throws IOException {
        Formatter formatter = new JsonFormatter(); // Create formater
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {//create print writer 
            writer.println(formatter.format(msg)); //formats and writes message
            writer.flush();
        }
    }
}

//
// Main Class
//
public class Main {
    static void main(String[] args) throws IOException {
        Message msg = new Message("This is the message");
        MessagePrinter printer = new MessagePrinter();
        printer.writeMessage(msg, "test.txt");
    }
}
```

Fix:

```java
// Formatter form another class
// PrintWriter from another class

public class MassagePrinter {
    public void writeMessage(Message msg, String filename, Formatter formatterm PrintWriter writer) throws IOException {
        writer.println(formatter.format(msg)); //formats and writes message
        writer.flush();
    }
}


//
// Main
//

public class Main {
    static void main(String[] args) throws IOException {
        Message msg = new Message("This is the message 2");
        MessagePrinter printer = new MessagePrinter();


    }
}
```
