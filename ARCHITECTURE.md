# Hotel Management System: OOP Architecture Explanation

This document outlines the architecture of the Hotel Management System, focusing on the application of Object-Oriented Programming (OOP) principles.

## Core OOP Principles Applied

The project effectively utilizes several core OOP principles:

1.  **Encapsulation:** Achieved by bundling data (attributes) and methods (behaviors) that operate on the data within a single unit (class). Access modifiers (`private`, `public`, `protected`) are used to control visibility and prevent direct manipulation of internal state.
2.  **Abstraction:** Implemented through abstract classes and interfaces, hiding complex implementation details and showing only essential features.
3.  **Inheritance:** Allows new classes (subclasses) to inherit properties and behaviors from existing classes (superclasses), promoting code reusability and establishing a natural hierarchy.
4.  **Polymorphism:** Enables objects of different classes to be treated as objects of a common type. This is evident in method overriding and interface implementations.

## Package Structure

The project is organized into two main packages:

*   `hotel`: Contains the core business logic and data models for the hotel system.
*   `ui`: Contains the graphical user interface components.

---

## `hotel` Package Analysis

This package defines the fundamental entities and operations of the hotel.

### `AmenitiesInfo.java`

*   **Purpose:** Represents the amenities available for a room.
*   **OOP Principles:**
    *   **Encapsulation:** The fields (`poolAccess`, `gymAccess`, `restaurantAccess`) are now `private`, and access is controlled through the `getStatus()` method (for reading a formatted string) and `updateAmenities()` method (for modifying). This demonstrates proper encapsulation by protecting the internal state of the object.

### `Bookable.java`

*   **Purpose:** An interface defining the contract for any entity that can be booked, checked in, and checked out.
*   **OOP Principles:**
    *   **Abstraction:** This is a pure abstraction. It declares methods (`checkAvailability`, `checkIn`, `checkOut`) without providing any implementation, forcing any class that `implements` it to define these behaviors.
    *   **Interface:** Explicitly defined as an `interface`.

### `Room.java` (Abstract Class)

*   **Purpose:** Serves as the abstract base class for all types of rooms in the hotel. It defines common properties and behaviors that all rooms share.
*   **OOP Principles:**
    *   **Abstraction:** Declared as `abstract`, meaning it cannot be instantiated directly. It contains an `abstract` method `setupAmenities()` which must be implemented by its concrete subclasses. This forces specific amenity configurations for each room type.
    *   **Encapsulation:** All attributes (`roomNumber`, `roomType`, `isAvailable`, `isClean`, `suppliesStocked`, `amenities`, `currentGuestName`) are `private`, and access is provided through `public` getter and setter methods (e.g., `getRoomNumber()`, `setClean()`). This protects the internal state of a `Room` object.
    *   **Inheritance:** Designed to be extended by specific room types (e.g., `StandardRoom`, `DeluxeRoom`, `SuiteRoom`).
    *   **Polymorphism:**
        *   The `setupAmenities()` method is a prime example. It's declared `abstract` in `Room` and then *overridden* in each subclass to provide specific amenity configurations. When `setupAmenities()` is called in the `Room` constructor, the version specific to the concrete subclass (e.g., `StandardRoom`'s `setupAmenities()`) is executed.
        *   It `implements` the `Bookable` interface, providing concrete implementations for `checkAvailability()`, `checkIn()`, and `checkOut()`. This means `Room` objects (and thus its subclasses) can be treated as `Bookable` entities.

### `StandardRoom.java`, `DeluxeRoom.java`, `SuiteRoom.java`

*   **Purpose:** Concrete implementations of different room types, each with specific amenity configurations.
*   **OOP Principles:**
    *   **Inheritance:** Each class `extends Room`, inheriting all its properties and the implementations of the `Bookable` interface methods.
    *   **Polymorphism:** Each class *overrides* the `protected abstract void setupAmenities()` method from the `Room` class to define the unique amenities for that specific room type. This is a clear demonstration of polymorphism, where the same method call (`setupAmenities()`) behaves differently based on the actual type of the `Room` object.
    *   **Encapsulation:** They rely on the `Room` class's encapsulation for their shared properties.

### `CheckInSystem.java`

*   **Purpose:** Manages the collection of rooms, handles check-in/check-out processes, and interacts with `CleaningTeam` and `SupplyTracker`. This acts as a central orchestrator for hotel operations.
*   **OOP Principles:**
    *   **Encapsulation:** It holds a `private ArrayList<Room> rooms`, `private CleaningTeam cleaningTeam`, and `private SupplyTracker supplyTracker`, controlling access to these internal components. Methods like `processCheckInRandomRoom()` or `processCheckout()` provide a controlled interface for interacting with the system.
    *   **Polymorphism:** When iterating through `rooms` (which are `Room` objects), methods like `r.isAvailable()`, `r.getRoomType()`, `r.checkIn()`, `r.checkOut()` are called. Even though `r` is declared as `Room`, the specific implementations from `StandardRoom`, `DeluxeRoom`, or `SuiteRoom` are implicitly used if they were overridden (though in this case, `checkIn` and `checkOut` are implemented in the abstract `Room` class, and `getRoomType` is also from `Room`). The `setupAmenities()` call within the `Room` constructor is the primary polymorphic behavior here.
    *   **Composition:** It "has-a" `CleaningTeam` and a `SupplyTracker`, demonstrating composition where objects are built from other objects.

### `CleaningTeam.java` and `SupplyTracker.java`

*   **Purpose:** Represent services responsible for cleaning rooms and restocking supplies, respectively.
*   **OOP Principles:**
    *   **Encapsulation:** These classes encapsulate the logic for their specific tasks. The `clean()` and `restock()` methods modify the state of a `Room` object through its public setters (`setClean`, `setSuppliesStocked`), respecting the `Room`'s encapsulation.
    *   **Collaboration:** They collaborate with `CheckInSystem` and `Room` objects to perform their duties.

---

## `ui` Package Analysis

This package handles the graphical user interface using JavaFX.

### `AppLauncher.java`

*   **Purpose:** A simple class to launch the JavaFX application.
*   **OOP Principles:** Minimal OOP here, primarily a utility class with a `main` method.

### `HotelGUI.java`

*   **Purpose:** The main class for the graphical user interface, responsible for rendering the hotel management system and handling user interactions.
*   **OOP Principles:**
    *   **Inheritance:** `HotelGUI extends Application`. The `Application` class is an abstract class from JavaFX, and `HotelGUI` *inherits* its functionality and *overrides* the `start(Stage primaryStage)` method. This is a fundamental aspect of JavaFX application development, demonstrating inheritance and polymorphism. The `start` method is the entry point for the JavaFX application lifecycle.
    *   **Encapsulation:** It holds a `private CheckInSystem system` instance, `private BorderPane rootLayout`, etc., encapsulating the UI components and the backend system. Methods like `createLandingPage()`, `createDashboardPage()`, `switchPage()`, `showAlert()` encapsulate specific UI behaviors and logic.
    *   **Composition:** It "has-a" `CheckInSystem` object, demonstrating how the UI layer interacts with the business logic layer.
    *   **Polymorphism:** The `switchPage` method takes a `Node` as an argument. `Node` is the base class for all visual elements in JavaFX. By accepting a `Node`, the `switchPage` method can handle various UI components (like `VBox`, `BorderPane`, `StackPane`) that is a subclass of `Node`, applying common transitions without needing to know the specific type of each component.

## Thorough Explanation of Abstract Classes and Interfaces

### `Room` (Abstract Class)

The `Room` class is a cornerstone of the `hotel` package's architecture, demonstrating **abstraction** and **polymorphism** effectively.

*   **Abstraction:**
    *   It's declared `public abstract class Room`. This means you cannot create an instance of `Room` directly (e.g., `new Room(...)` would cause a compile-time error). It exists solely to provide a common blueprint for its subclasses.
    *   It contains an `abstract` method: `protected abstract void setupAmenities();`. This method has no body in the `Room` class. Its purpose is to declare that *every concrete subclass of `Room` MUST provide an implementation for `setupAmenities()`*. This forces specific amenity configurations for each room type, ensuring that no room is created without its amenities being properly defined. This hides the complexity of *how* amenities are set up from the `Room` class itself, abstracting it away.

*   **Polymorphism:**
    *   The `setupAmenities()` method is called within the `Room` constructor: `this.amenities = new AmenitiesInfo(); setupAmenities();`.
    *   When you create an object like `new StandardRoom(101)`, the `StandardRoom` constructor implicitly calls the `Room` constructor (`super(roomNumber, "standard")`).
    *   Inside the `Room` constructor, `setupAmenities()` is called. At this point, because the object being constructed is actually a `StandardRoom`, the *overridden* `setupAmenities()` method from `StandardRoom` is executed, not some generic `Room` version (which doesn't exist).
    *   This is a classic example of **polymorphism through method overriding** and the **Template Method pattern**, where the abstract class defines the overall structure (call `setupAmenities()`) but defers the specific implementation to its subclasses.

### `Bookable` (Interface)

The `Bookable` interface is another example of **abstraction**.

*   **Abstraction & Interface:**
    *   It defines a contract: `boolean checkAvailability(); void checkIn(String guestName); void checkOut();`.
    *   It specifies *what* an object can do (be booked, checked in, checked out) without dictating *how* it does it.
    *   The `Room` class `implements Bookable`. This means `Room` (and by extension, all its concrete subclasses) *promises* to provide implementations for all three methods declared in `Bookable`.
    *   This allows the `CheckInSystem` to interact with any `Room` object through the `Bookable` interface, treating all rooms uniformly when it comes to these core booking operations, regardless of whether they are `StandardRoom`, `DeluxeRoom`, or `SuiteRoom`. This is **polymorphism through interface implementation**.

### `HotelGUI` (Inheritance and Polymorphism with JavaFX `Application`)

The `HotelGUI` class demonstrates **inheritance** and **polymorphism** in the context of a JavaFX application.

*   **Inheritance:**
    *   `public class HotelGUI extends Application`. `Application` is an abstract class provided by JavaFX. By extending it, `HotelGUI` inherits the fundamental structure and lifecycle methods required for a JavaFX application.
    *   This allows the JavaFX runtime to recognize `HotelGUI` as a valid application entry point.

*   **Polymorphism:**
    *   The `start(Stage primaryStage)` method is an *override* of an abstract method declared in the `Application` class. The JavaFX framework calls this `start` method polymorphically when the application is launched. The `Application` class defines the contract, and `HotelGUI` provides the specific implementation for *this* hotel management application.
    *   Similarly, methods like `switchPage(Node page)` utilize polymorphism. `Node` is the base class for all visual elements in JavaFX. By accepting a `Node`, the `switchPage` method can handle any UI component (e.g., `VBox`, `BorderPane`, `StackPane`) that is a subclass of `Node`, applying common transitions without needing to know the specific type of each component.

## Summary of OOP in the Project

*   **Encapsulation** is consistently applied with `private` fields and `public` getters/setters in `Room`, `CheckInSystem`, `CleaningTeam`, and `SupplyTracker`.
*   **Abstraction** is evident in the `Bookable` interface (defining a contract) and the `Room` abstract class (providing a common blueprint with deferred implementation for `setupAmenities`).
*   **Inheritance** is used to create a hierarchy of room types (`StandardRoom`, `DeluxeRoom`, `SuiteRoom` extending `Room`) and for the GUI (`HotelGUI` extending `Application`).
*   **Polymorphism** is demonstrated through method overriding (`setupAmenities()` in room subclasses, `start()` in `HotelGUI`) and interface implementation (`Room` implementing `Bookable`), allowing for flexible and extensible code.

This architecture provides a clear separation of concerns, reusability of code, and a robust foundation for extending the hotel management system with new room types or features.