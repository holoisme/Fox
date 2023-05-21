# Fox: A fast and beginner-friendly programming language.
<p align="center">
  <a href="https://github.com/HoloInJava/Fox">
    <img src="https://github.com/HoloInJava/Fox/assets/77677018/5df00ff4-d88e-4182-b0b9-ac7781b1ff66" width="220">
  </a>
</p>
<p align="center">
  <i align="center">
    Isn't he <b>cute</b>?
  </i>
</p>

## Overview
[**Fox**](https://github.com/HoloInJava/Fox) is a **simple** yet **fully fletched programming language**, with **beginner-friendly syntax and library**, and finally offers a **transparent API** with Java ! <br><br>
If your user has to write some code in your Java software (e.g. Game engine, or mod support), it can be quite a challenge to make it work properly without bloating your code or reinventing the wheel; and **reinventing the wheel I did**. 🦊<br>
Offering you an **extremely easy way** to connect your already existing Java project to Fox, it offers what we call a "*transparent API*" (see below) 🔥

### Quick sample 📃
```javascript

var name = "John Doe";
var chars = for(c in name): c; // [J, o, h, n,  , D, o, e]

var sum = (a, b) -> a + b;
function generateRandomValues(): for(var i = 0; i < 20000; i++): random(); // creates and populates a list with random values

var instance = generateRandomValues();

class Vector {
  constructor() {
    this.x = this.y = this.z = 0;
  }

  constructor(x, y, z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  function crossProduct(other): x * other.x + y * other.y + z * other.z;
  
  static function stringify(vector) {
    var str = "";
    str += "x: " + vector.x;
    str += "y: " + vector.y;
    str += "z: " + vector.z;
    return str;
  }
}

enum TokenType {
  PLUS('+'), MINUS('-'), MULTIPLY('*'), DIVIDE('/');
  
  constructor(symbol): this.symbol = symbol;
}
```

### Java API ☕
Let's say you want to include the following **Java class** into the Fox environnement, this is how we proceed :
```java
class Player {
  private String name;
  private float damage;
  
  public Player(String name) {
    this.name = name;
  }
  
  public void setDamage(float damage) { this.damage = damage; }
  
  public void present() {
    System.out.println("My name is " + name + " and I do " + damage + " of damage ! Rwar!");
  }
  
  @FoxIgnore // This will assure that Fox doesn't access this
  public void someSensitiveContent() {
    /* bip boop boop */
  }
}
```
We can simply add the following line :
```java
interpreter.includeClass(Player.class);
```
And you're done !<br>
Your end-users have now full access to that class from the Fox scripts :
```javascript
var p = new Player("John");
p.setDamage(12.5);
p.present();
```
The real point of [**Fox**](https://github.com/HoloInJava/Fox) is that it is an **interpreted programming language**, meaning that everything happens in **runtime**, which is perfect for Game Engines, mod support, plugin development, sandboxes etc.<br>
<br>
**Please enjoy it, I did :)**
