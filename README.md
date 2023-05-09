# Fox: A fast and beginner-friendly programming language.
<p align="center">
  <a href="https://github.com/HoloInJava/Fox">
    <img src="https://github.com/HoloInJava/Fox/assets/77677018/5df00ff4-d88e-4182-b0b9-ac7781b1ff66" width="256">
  </a>
</p>
<p align="center">
  <i align="center">
    Isn't he <b>cute</b>?
  </i>
</p>

# Overview
[**Fox**](https://github.com/HoloInJava/Fox) is a **simple** yet fully fletched programming language, with **beginner-friendly syntax** and a **transparent API** with Java. <br>
If your user has to write some code in your Java software (e.g. Game engine, or mod support), it can be quite a challenge to make it work properly without bloating your code or reinventing the wheel; and **reinventing the wheel we did**. 🦊<br>
Offering you an **extremly easy way** to connect your already existing project to Fox, it offers what we call a [*transparent API*](https://github.com/HoloInJava/Fox) (see below) 🔥

## A quick sample
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
