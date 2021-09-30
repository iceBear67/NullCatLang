# NullCat Lang
Simple toy lang

# Syntax
```
using std.io

@main
fn main(args: Array<String>){
  std.println("hello world")
  io.writeFile("test","aa")
}
```

with classes:
```
class myClass : Runnable {
  (a: String) {
    std.println(a)
  }
  fn method(): Int{
    return 0
  }
}
```